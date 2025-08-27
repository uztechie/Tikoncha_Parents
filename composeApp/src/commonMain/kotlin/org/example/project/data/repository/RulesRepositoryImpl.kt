package org.example.project.data.repository

import org.example.project.data.remote.RulesApiService
import org.example.project.data.remote.model.GetRulesResponse
import org.example.project.domain.repository.RulesRepository

class RulesRepositoryImpl(private val api: RulesApiService): RulesRepository {
    override suspend fun getRules(studentId: String): GetRulesResponse {
        return api.getRules(studentId)
    }


}