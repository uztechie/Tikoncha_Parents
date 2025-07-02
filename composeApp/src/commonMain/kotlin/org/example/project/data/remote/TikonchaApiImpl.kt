package org.example.project.data.remote

import io.ktor.client.HttpClient

class TikonchaApiImpl(private val client: HttpClient): TikonchaApi {
    override suspend fun login(): List<String> {
        return listOf("Ibroxim", "Odilov")
    }
}