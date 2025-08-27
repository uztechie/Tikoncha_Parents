package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.project.data.local.AppSettings
import org.example.project.presentation.profile.language.LanguagePrefs

class TikonchaClient(private val engine: HttpClientEngine) {
    val BASE_URL = "api.tikoncha.uz"
    val client = HttpClient(engine){
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = object : Logger{
                override fun log(message: String) {
                    println("HTTP_LOG: $message")
                }
            }
            level = LogLevel.ALL
        }
        install(HttpRedirect){
            checkHttpMethod = false
            allowHttpsDowngrade = true
        }
        install(io.ktor.client.plugins.HttpTimeout) {
            requestTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 15_000
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
            }
            header("Accept", "application/json")
            header("Content-Type", "application/json")
            header("X-DEVICE-TYPE", "ANDROID")
            header("LANG-CODE", LanguagePrefs.loadOrDefault().languageCode)
            header("Authorization", "Bearer ${AppSettings.accessToken}")

        }

    }

}