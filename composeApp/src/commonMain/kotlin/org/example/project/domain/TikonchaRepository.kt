package org.example.project.domain

import org.example.project.data.remote.TikonchaClient


interface TikonchaRepository {
    suspend fun login(): List<String>

}