package com.tecknobit.gluky.helpers

import io.ktor.client.HttpClient

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.gluky.ui.components.imageLoader] instance
 */
expect fun customHttpClient(): HttpClient