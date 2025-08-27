package org.example.project.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import org.example.project.data.remote.model.GetRulesResponse

class RulesApiService(private val client: HttpClient) {


    suspend fun getRules(studentId: String): GetRulesResponse =
        client.safeRequest(
            method = HttpMethod.Get,
            url = "policies/students/{student_id}/effective",
            block = {
                parameter("student_id", studentId)
            }
        )



}