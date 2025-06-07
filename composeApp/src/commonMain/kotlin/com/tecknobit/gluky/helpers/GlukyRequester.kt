package com.tecknobit.gluky.helpers

import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.BASE_EQUINOX_ENDPOINT
import com.tecknobit.equinoxcore.network.RequestMethod.DELETE
import com.tecknobit.equinoxcore.network.RequestMethod.GET
import com.tecknobit.equinoxcore.network.RequestMethod.PUT
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.BACKEND_URL
import com.tecknobit.gluky.ui.screens.analyses.data.Report
import com.tecknobit.glukycore.BASAL_INSULIN_KEY
import com.tecknobit.glukycore.CONTENT_KEY
import com.tecknobit.glukycore.DAILY_NOTES_KEY
import com.tecknobit.glukycore.FROM_DATE_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.GLYCEMIC_TREND_GROUPING_DAY_KEY
import com.tecknobit.glukycore.GLYCEMIC_TREND_PERIOD_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.MEALS_KEY
import com.tecknobit.glukycore.MEASUREMENTS_KEY
import com.tecknobit.glukycore.POST_PRANDIAL_GLYCEMIA_KEY
import com.tecknobit.glukycore.TO_DATE_KEY
import com.tecknobit.glukycore.enums.GlycemicTrendGroupingDay
import com.tecknobit.glukycore.enums.GlycemicTrendPeriod
import com.tecknobit.glukycore.enums.MeasurementType
import com.tecknobit.glukycore.helpers.GlukyEndpointsSet.ANALYSES_ENDPOINT
import com.tecknobit.glukycore.helpers.GlukyEndpointsSet.REPORTS_ENDPOINT
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

/**
 * The `GlukyRequester` class is useful to communicate with the Gluky's backend
 *
 * @param userId The user identifier
 * @param userToken The user token
 * @param debugMode Whether the requester is still in development and who is developing needs the log of the requester's
 * workflow, if it is enabled all the details of the requests sent and the errors occurred will be printed in the console
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see com.tecknobit.equinoxcore.network.Requester
 * @see EquinoxRequester
 */
class GlukyRequester(
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = BACKEND_URL,
    userId = userId,
    userToken = userToken,
    debugMode = debugMode,
    connectionErrorMessage = DEFAULT_CONNECTION_ERROR_MESSAGE,
    byPassSSLValidation = true
) {

    private companion object {

        /**
         * `TARGET_DAY_PATTERN` the pattern used to format the dates
         */
        const val TARGET_DAY_PATTERN = "dd-MM-yyyy"

    }

    /**
     * Method used to create the payload for the [signIn] request
     *
     * @param email The email of the user
     * @param password The password of the user
     * @param custom The custom parameters added in a customization of the equinox user to execute a customized sign-in
     *
     * @return the payload for the request as [JsonObject]
     *
     */
    // TODO: TO REMOVE WHEN LANGUAGE PARAMETER IMPLEMENTED BUILT-IN
    @Assembler
    @CustomParametersOrder(LANGUAGE_KEY)
    override fun getSignInPayload(
        email: String,
        password: String,
        vararg custom: Any?,
    ): JsonObject {
        val payload = super.getSignInPayload(email, password, *custom).toMutableMap()
        payload[LANGUAGE_KEY] = Json.encodeToJsonElement(custom[0].toString())
        return Json.encodeToJsonElement(payload).jsonObject
    }

    /**
     * Request to get the daily measurement
     *
     * @param targetDay The target day
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/measurements/{target_day}", method = GET)
    suspend fun getDailyMeasurements(
        targetDay: Long,
    ): JsonObject {
        return execGet(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay
            )
        )
    }

    /**
     * Request to fill the specified [targetDay]
     *
     * @param targetDay The target day to fill
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/measurements/{target_day}", method = PUT)
    suspend fun fillDay(
        targetDay: Long,
    ): JsonObject {
        return execPut(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay
            )
        )
    }

    /**
     * Request to fill a meal
     *
     * @param targetDay The target day of the meal to fill
     * @param mealType The type of the meal to fill
     * @param glycemia The pre-prendial glycemia value
     * @param postPrandialGlycemia The post-prendial glycemia value
     * @param insulinUnits The administrated insulin units
     * @param content The content of the meal
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/measurements/{target_day}/meals/{type}",
        method = PUT
    )
    suspend fun fillMeal(
        targetDay: Long,
        mealType: MeasurementType,
        glycemia: String,
        postPrandialGlycemia: String,
        insulinUnits: Int,
        content: List<Pair<MutableState<String>, MutableState<String>>>,
    ): JsonObject {
        val payload = buildJsonObject {
            put(GLYCEMIA_KEY, glycemia)
            put(POST_PRANDIAL_GLYCEMIA_KEY, postPrandialGlycemia)
            put(INSULIN_UNITS_KEY, insulinUnits)
            putJsonObject(CONTENT_KEY) {
                content.forEach { entry ->
                    put(entry.first.value, entry.second.value)
                }
            }
        }
        return execPut(
            endpoint = assembleMealsUrl(
                targetDay = targetDay,
                mealType = mealType
            ),
            payload = payload
        )
    }

    /**
     * Method used to assemble the endpoint to make the requests related to the meals
     *
     * @param targetDay The target day
     * @param mealType The type of the meal
     *
     * @return an endpoint to make the request as [String]
     */
    @Wrapper
    @Assembler
    private fun assembleMealsUrl(
        targetDay: Long,
        mealType: MeasurementType?,
    ): String {
        var mealsUrl = assembleMeasurementsUrl(
            targetDay = targetDay,
            customPath = MEALS_KEY
        )
        mealType?.let {
            mealsUrl += "/$mealType"
        }
        return mealsUrl
    }

    /**
     * Request to fill a basal insulin record
     *
     * @param targetDay The target day of the basal insulin record to fill
     * @param glycemia The glycemia value
     * @param insulinUnits The administrated insulin units
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/measurements/{target_day}/basal_insulin",
        method = PUT
    )
    suspend fun fillBasalInsulin(
        targetDay: Long,
        glycemia: String,
        insulinUnits: Int,
    ): JsonObject {
        val payload = buildJsonObject {
            put(GLYCEMIA_KEY, glycemia)
            put(INSULIN_UNITS_KEY, insulinUnits)
        }
        return execPut(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay,
                customPath = BASAL_INSULIN_KEY
            ),
            payload = payload
        )
    }

    /**
     * Request to save the notes related to the target day
     *
     * @param targetDay The target day of the basal insulin record to fill
     * @param content The content of the daily notes
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/measurements/{target_day}/daily_notes",
        method = PUT
    )
    suspend fun saveDailyNotes(
        targetDay: Long,
        content: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(DAILY_NOTES_KEY, content)
        }
        return execPut(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay,
                customPath = DAILY_NOTES_KEY
            ),
            payload = payload
        )
    }

    /**
     * Method used to assemble the endpoint to make the requests to the measurements controller
     *
     * @param targetDay The target day
     * @param customPath Optional custom path to append
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleMeasurementsUrl(
        targetDay: Long,
        customPath: String? = null,
    ): String {
        var subEndpoint = targetDay.toDateString(
            pattern = TARGET_DAY_PATTERN
        )
        customPath?.let { path ->
            subEndpoint += "/$path"
        }
        return assembleCustomEndpointPath(
            customEndpoint = MEASUREMENTS_KEY,
            subEndpoint = subEndpoint
        )
    }

    /**
     * Request to retrieve the glycemic trend with the specified parameters
     *
     * @param period      The period to respect with the dates range
     * @param groupingDay The grouping day
     * @param from        The start date from retrieve the measurements
     * @param to          The end date to retrieve the measurements
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/analyses", method = GET)
    suspend fun getGlycemicTrend(
        period: GlycemicTrendPeriod,
        groupingDay: GlycemicTrendGroupingDay?,
        from: Long?,
        to: Long?,
    ): JsonObject {
        val query = createPeriodQuery(
            period = period,
            groupingDay = groupingDay,
            from = from,
            to = to
        )
        return execGet(
            endpoint = assembleAnalysesUrl(),
            query = query
        )
    }

    /**
     * Request to create a report related to a glycemic trend with the specified parameters
     *
     * @param period      The period to respect with the dates range
     * @param groupingDay The grouping day
     * @param from        The start date from retrieve the measurements
     * @param to          The end date to retrieve the measurements
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/analyses/reports", method = GET)
    suspend fun createReport(
        period: GlycemicTrendPeriod,
        groupingDay: GlycemicTrendGroupingDay?,
        from: Long?,
        to: Long?,
    ): JsonObject {
        val query = createPeriodQuery(
            period = period,
            groupingDay = groupingDay,
            from = from,
            to = to
        )
        return execGet(
            endpoint = assembleAnalysesUrl(
                subEndpoint = REPORTS_ENDPOINT
            ),
            query = query
        )
    }

    /**
     * Method used to create the query with the data related to a period
     *
     * @param period      The period to respect with the dates range
     * @param groupingDay The grouping day
     * @param from        The start date from retrieve the measurements
     * @param to          The end date to retrieve the measurements
     *
     * @return the query as [JsonObject]
     */
    @Assembler
    private fun createPeriodQuery(
        period: GlycemicTrendPeriod,
        groupingDay: GlycemicTrendGroupingDay?,
        from: Long?,
        to: Long?,
    ): JsonObject {
        return buildJsonObject {
            put(GLYCEMIC_TREND_PERIOD_KEY, period.name)
            groupingDay?.let { day ->
                put(GLYCEMIC_TREND_GROUPING_DAY_KEY, day.name)
            }
            from?.let {
                put(FROM_DATE_KEY, from)
                put(TO_DATE_KEY, to)
            }
        }
    }

    /**
     * Request to delete a report
     *
     * @param report The report to delete
     *
     * @return the result of the request as [JsonObject]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/analyses/reports/{report_id}", method = DELETE)
    suspend fun deleteReport(
        report: Report,
    ): JsonObject {
        return execDelete(
            endpoint = assembleAnalysesUrl(
                subEndpoint = "$REPORTS_ENDPOINT/${report.reportId}"
            )
        )
    }

    /**
     * Method used to assemble the endpoint to make the requests to the analyses controller
     *
     * @param subEndpoint Optional sub-endpoint to append
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleAnalysesUrl(
        subEndpoint: String = "",
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = ANALYSES_ENDPOINT,
            subEndpoint = subEndpoint.removePrefix("/")
        )
    }

    /**
     * Request to download a created request
     *
     * @param report The report to download
     * @param onDownloadCompleted The callback to invoke after the report downloaded
     */
    suspend fun downloadReport(
        report: Report,
        onDownloadCompleted: (String?) -> Unit,
    ) {
        val reportBytes: ByteArray = ktorClient.get(
            urlString = host.removeSuffix(BASE_EQUINOX_ENDPOINT) + "/" + report.reportUrl
        ).body()
        saveReport(
            reportBytes = reportBytes,
            reportName = report.name,
            onSave = onDownloadCompleted
        )
    }

}