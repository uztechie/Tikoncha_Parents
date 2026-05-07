package uz.tikoncha_parent.presentation.tracking

import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto
import uz.tikoncha_parent.presentation.map2.LatLng

internal fun ChildrenLocationItemDto.toPerson(): Person? {
    val id = child_user_id ?: return null
    val lat = lat
    val lon = lng
    val location = if (
        lat != null && lon != null &&
        !(lat == 0.0 && lon == 0.0)
    ) {
        LatLng(lat, lon)
    } else null

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
        lastSeenEpochMs = DateTimeUtil.toMillis(this.updated_at),
        avatarUrl = avatar_url
    )
}