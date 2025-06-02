package com.tecknobit.gluky.helpers

import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.gluky.GlukyConfig

// TODO: TO SETUP CORRECTLY (SSL validation)
class GlukyRequester(
    userId: String? = null,
    userToken: String? = null,
    debugMode: Boolean = false,
) : EquinoxRequester(
    host = GlukyConfig.BACKEND_URL,
    userId = userId,
    userToken = userToken,
    debugMode = debugMode,
    connectionErrorMessage = DEFAULT_CONNECTION_ERROR_MESSAGE
)