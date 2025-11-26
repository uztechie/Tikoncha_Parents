package uz.tikoncha_parent.domain.use_case.policy

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.CreatePolicyRequest
import uz.tikoncha_parent.data.remote.model.UpdatePolicyRequest
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PolicyRepository

class UpdatePolicyUseCase(
    private val policyRepository: PolicyRepository
) {
    suspend operator fun invoke(updatePolicyRequest: UpdatePolicyRequest, ruleId: String): Resource<String> {
        return try {
            val response = policyRepository.updatePolicyInServer(updatePolicyRequest, ruleId)
            if (response.success){
                Resource.Success("")
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