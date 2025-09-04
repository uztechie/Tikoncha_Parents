package uz.tikoncha_parent.domain.use_case

import uz.tikoncha_parent.data.remote.model.GetRulesData
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.RulesRepository

class RefreshRulesUseCase(
    private val rulesRepository: RulesRepository
) {
    suspend operator fun invoke(userId: String): Resource<GetRulesData>{
        return try {
            val response = rulesRepository.getRules(userId)
            if (response.success && response.data != null){
                Resource.Success(response.data)
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