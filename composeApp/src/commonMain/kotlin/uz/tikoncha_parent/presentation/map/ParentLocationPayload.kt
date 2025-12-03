package uz.tikoncha_parent.presentation.map

import kotlinx.serialization.Serializable
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto

@Serializable
data class ParentLocationPayload(
    val name: String,
    val lat: Double?,
    val lng: Double?,
    val children: List<ChildLocationPayload>
)

@Serializable
data class ChildLocationPayload(
    val name: String,
    val updated_at: String,
    val lat: Double?,
    val lng: Double?
)


fun ChildrenLocationItemDto.toPayload(): ChildLocationPayload {
    return ChildLocationPayload(
        name = first_name?:"",
        updated_at = updated_at?:"",
        lat = lat,
        lng = lng
    )
}