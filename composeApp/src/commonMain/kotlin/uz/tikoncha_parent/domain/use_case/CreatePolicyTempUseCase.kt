package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequestTemp
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.RulesRepository

class CreatePolicyTempUseCase(
    private val rulesRepository: RulesRepository
) {
    suspend operator fun invoke(createPolicyRequestTemp: CreatePolicyRequestTemp): Resource<String>{
        return try {
            val response = rulesRepository.createPolicy(createPolicyRequestTemp)
            if (response.success && response.data != null){
                Resource.Success(response.data.id)
            }
            else {
                Resource.Error(
                    message = response.error,
                    resId = Res.string.server_connection_error
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(
                resId = Res.string.server_connection_error,
                cause = e
            )
        }

    }
}