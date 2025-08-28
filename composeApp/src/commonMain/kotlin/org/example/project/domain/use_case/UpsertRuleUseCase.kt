package org.example.project.domain.use_case

import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.GetRulesData
import org.example.project.data.remote.model.UpsertRuleRequest
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.RulesRepository

class UpsertRuleUseCase(
    private val rulesRepository: RulesRepository
) {
    suspend operator fun invoke(upsertRuleRequest: UpsertRuleRequest): Resource<Boolean>{
        return try {
            val response = rulesRepository.upsertRule(upsertRuleRequest)
            if (response.success){
                Resource.Success(true)
            }
            else{
                Resource.Error(response.error?: "")
            }


        }
        catch (e: Exception){
            Resource.Error("Xatolik")

        }
        catch (e: Exception){
            e.printStackTrace()
            Resource.Error("Xatolik")
        }

    }
}