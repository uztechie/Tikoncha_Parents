package org.example.project.domain.use_case

import org.example.project.data.remote.model.CreatePolicyRequest
import org.example.project.data.remote.model.CreatePolicyResponse
import org.example.project.data.remote.model.GetRulesData
import org.example.project.domain.model.Resource
import org.example.project.domain.repository.RulesRepository

class CreatePolicyUseCase(
    private val rulesRepository: RulesRepository
) {
    suspend operator fun invoke(createPolicyRequest: CreatePolicyRequest): Resource<String>{
        return try {
            val response = rulesRepository.createPolicy(createPolicyRequest)
            if (response.success && response.data != null){
                Resource.Success(response.data.id)
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