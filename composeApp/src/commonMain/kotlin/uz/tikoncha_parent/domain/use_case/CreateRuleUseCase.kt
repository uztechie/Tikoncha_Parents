package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.CreateRuleRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.RulesRepository
import kotlin.String

class CreateRuleUseCase(
    private val rulesRepository: RulesRepository
) {
    suspend operator fun invoke(createRuleRequest: CreateRuleRequest,  userId: String):Resource<Boolean> {
        if (AppSettings.policyId.isEmpty()){
            val createPolicyRes = createPolicy(userId)
            when(createPolicyRes){
                is Resource.Loading ->{
                    return Resource.Loading()
                }
                is Resource.Error -> {
                    return Resource.Error(createPolicyRes.message)
                }
                is Resource.Success -> {
                    AppSettings.policyId = createPolicyRes.data
                    return createRule(createRuleRequest)
                }
            }
        }
        else{
            return createRule(createRuleRequest)
        }
    }

    suspend  fun createPolicy(userId: String): Resource<String>{
        return try {
            val createPolicyRequest: CreatePolicyRequest = CreatePolicyRequest(
                scope_id = userId,
                scope_type = "PARENT_CHILD",
                is_active = true,
                name = "Parent"
            )

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

    private suspend fun createRule(createRuleRequest: CreateRuleRequest):Resource<Boolean>{

        return try {
            val response = rulesRepository.createRule(createRuleRequest.copy(
                policyId = AppSettings.policyId
            ))
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

