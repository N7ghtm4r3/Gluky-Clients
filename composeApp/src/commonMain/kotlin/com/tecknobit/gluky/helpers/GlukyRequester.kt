package com.tecknobit.gluky.helpers

import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.gluky.BACKEND_URL
import com.tecknobit.glukycore.CONTENT_KEY
import com.tecknobit.glukycore.GLYCEMIA_KEY
import com.tecknobit.glukycore.INSULIN_UNITS_KEY
import com.tecknobit.glukycore.MEALS_KEY
import com.tecknobit.glukycore.MEASUREMENTS_KEY
import com.tecknobit.glukycore.POST_PRANDIAL_GLYCEMIA_KEY
import com.tecknobit.glukycore.enums.MeasurementType
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

    @Assembler
    private fun assembleMeasurementsUrl(
        targetDay: Long,
        customPath: String? = null,
    ): String {
        var subEndpoint = targetDay.toDateString(
            pattern = "dd-MM-yyyy"
        )
        customPath?.let { path ->
            subEndpoint += "/$path"
        }
        return assembleCustomEndpointPath(
            customEndpoint = MEASUREMENTS_KEY,
            subEndpoint = subEndpoint
        )
    }

}