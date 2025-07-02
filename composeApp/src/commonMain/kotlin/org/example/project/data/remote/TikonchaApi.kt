package org.example.project.data.remote

interface TikonchaApi {
    suspend fun login(): List<String>
}