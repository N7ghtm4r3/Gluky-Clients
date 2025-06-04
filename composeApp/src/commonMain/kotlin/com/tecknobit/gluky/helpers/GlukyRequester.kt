package com.tecknobit.gluky.helpers

import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.BASE_EQUINOX_ENDPOINT
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

// TODO: TO SETUP CORRECTLY (SSL validation)
class GlukyRequester(
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = BACKEND_URL,
    userId = userId,
    userToken = userToken,
    debugMode = debugMode,
    connectionErrorMessage = DEFAULT_CONNECTION_ERROR_MESSAGE
) {

    private companion object {

        const val TARGET_DAY_PATTERN = "dd-MM-yyyy"

    }

    // TODO: TO REMOVE WHEN LANGUAGE PARAMETER IMPLEMENTED BUILT-IN
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

    suspend fun getDailyMeasurements(
        targetDay: Long,
    ): JsonObject {
        return execGet(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay
            )
        )
    }

    suspend fun fillDay(
        targetDay: Long,
    ): JsonObject {
        return execPut(
            endpoint = assembleMeasurementsUrl(
                targetDay = targetDay
            )
        )
    }

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
                subEndpoint = REPORTS_ENDPOINT.removePrefix("/")
            ),
            query = query
        )
    }

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

    suspend fun deleteReport(
        report: Report,
    ): JsonObject {
        return execDelete(
            endpoint = assembleAnalysesUrl(
                subEndpoint = "$REPORTS_ENDPOINT/$report.id"
            )
        )
    }

    @Assembler
    private fun assembleAnalysesUrl(
        subEndpoint: String = "",
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = ANALYSES_ENDPOINT,
            subEndpoint = subEndpoint
        )
    }

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
            onDownloadCompleted = onDownloadCompleted
        )
    }

}