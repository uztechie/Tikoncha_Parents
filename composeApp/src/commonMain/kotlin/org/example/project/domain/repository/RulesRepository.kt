package org.example.project.domain.repository

import org.example.project.data.remote.model.AddChildRequest
import org.example.project.data.remote.model.AddChildResponse
import org.example.project.data.remote.model.AppUsageResponse
import org.example.project.data.remote.model.ChildrenResponse
import org.example.project.data.remote.model.GetRulesResponse

interface RulesRepository {

    suspend fun getRules(studentId: String): GetRulesResponse
}