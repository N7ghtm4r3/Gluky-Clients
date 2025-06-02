package com.tecknobit.gluky.helpers

import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.gluky.BACKEND_URL
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

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

}