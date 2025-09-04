package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.UpsertRuleRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.RulesRepository

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