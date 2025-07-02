package org.example.project.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual class HttpClientEngineFactory {
    actual fun getHttpEngine(): HttpClientEngine {
        return OkHttp.create()
    }
}