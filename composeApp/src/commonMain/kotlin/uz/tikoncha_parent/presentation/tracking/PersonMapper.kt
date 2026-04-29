package uz.tikoncha_parent.presentation.tracking

import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto
import uz.tikoncha_parent.presentation.map2.LatLng

internal fun ChildrenLocationItemDto.toPerson(): Person? {
    val id = child_user_id ?: return null
    val lat = lat
    val lon = lng
    val location = if (lat != null && lon != null) LatLng(lat, lon) else null

    val fullName = listOfNotNull(first_name, last_name)
        .filter { it.isNotBlank() }
        .joinToString(" ")
        .ifBlank { "User" }

    return Person(
        id = id,
        name = first_name?:"User",
        location = location,
        isSelf = false,
        lastSeen = this.updated_at?:"",
        avatarUrl = ""
    )
}