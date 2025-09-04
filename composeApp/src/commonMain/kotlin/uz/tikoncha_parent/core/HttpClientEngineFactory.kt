package uz.tikoncha_parent.core

import io.ktor.client.engine.HttpClientEngine

expect class HttpClientEngineFactory() {
    fun getHttpEngine(): HttpClientEngine
}