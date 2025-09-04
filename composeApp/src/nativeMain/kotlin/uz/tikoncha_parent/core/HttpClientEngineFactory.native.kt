package uz.tikoncha_parent.project.core

import io.ktor.client.engine.darwin.Darwin

actual class HttpClientEngineFactory {
    actual fun getHttpEngine(): io.ktor.client.engine.HttpClientEngine {
        return Darwin.create()
    }
}
