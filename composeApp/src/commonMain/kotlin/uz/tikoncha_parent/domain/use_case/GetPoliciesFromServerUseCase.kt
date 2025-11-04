package uz.tikoncha_parent.domain.use_case

import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.server_connection_error
import uz.tikoncha_parent.data.remote.model.PolicyDto
import uz.tikoncha_parent.domain.model.Resource
import uz.tikoncha_parent.domain.repository.PolicyRepository

class GetPoliciesFromServerUseCase(
    private val policyRepository: PolicyRepository
) {
    suspend operator fun invoke(userId: String): Resource<List<PolicyDto>>{
        return try {
            val response = policyRepository.getPolicies(userId)
            if (response.success && response.data != null){
                Resource.Success(response.data.policies)
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