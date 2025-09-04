package uz.tikoncha_parent.core

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual class HttpClientEngineFactory {
    actual fun getHttpEngine(): HttpClientEngine {
        return OkHttp.create()
    }
}