package com.tecknobit.gluky.helpers

import android.annotation.SuppressLint
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.gluky.ui.components.imageLoader] instance
 */
actual fun customHttpClient(): HttpClient {
    val sslContext = SSLContext.getInstance("TLS")
    val certificates = validateSelfSignedCertificate()
    sslContext.init(null, certificates, SecureRandom())
    return HttpClient(OkHttp) {
        engine {
            config {
                sslSocketFactory(sslContext.socketFactory, certificates[0] as X509TrustManager)
                hostnameVerifier { _, _ -> true }
            }
        }
    }
}

/**
 * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
 *
 * @return list of trust managers as [Array] of [TrustManager]
 *
 * > [!WARNING]
 * > this method disable all checks on the SLL certificate validity, so is recommended to
 * > use for test only or in a private distribution on own infrastructure
 */
private fun validateSelfSignedCertificate(): Array<TrustManager> {
    return arrayOf(
        @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
            }
        })
}