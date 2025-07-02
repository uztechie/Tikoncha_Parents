package org.example.project.core

import io.ktor.client.engine.HttpClientEngine

expect class HttpClientEngineFactory() {
    fun getHttpEngine(): HttpClientEngine
}