package uz.tikoncha_parent.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ChildrenResponse(
    val success: Boolean,
    val data: ChildrenData?,
    val error: String? = null,
    val code:Int
)

@Serializable
data class ChildrenData(
    val children:List<UserInfoDto>
)
