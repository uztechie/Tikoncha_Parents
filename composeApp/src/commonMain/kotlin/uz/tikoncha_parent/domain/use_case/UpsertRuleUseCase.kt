package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
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