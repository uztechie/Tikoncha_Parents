package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.RulesRepository

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