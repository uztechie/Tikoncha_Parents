package uz.tikoncha_parent.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header

import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.presentation.profile.language.LanguagePrefs

class TikonchaClient(private val engine: HttpClientEngine) {

    companion object{
        val BASE_URL = "api.tikoncha.uz"
        val BASE_URL_WITH_HTTPS = "https://api.tikoncha.uz"
    }

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
        install(HttpTimeout) {
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