package uz.tikoncha_parent.data.remote.model

data class GetPoliciesResponse(
    val success: Boolean,
    val data: ActivePoliciesData? = null,
    val error: String? = null,
    val code: Int? = null,
)

data class ActivePoliciesData(
    val policies: List<PolicyDto>
)

