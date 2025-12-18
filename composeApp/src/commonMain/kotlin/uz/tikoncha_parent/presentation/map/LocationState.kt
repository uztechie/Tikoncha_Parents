package uz.tikoncha_parent.presentation.map

import dev.icerock.moko.geo.LatLng
import uz.tikoncha_parent.data.remote.model.ChildrenLocationItemDto
import uz.tikoncha_parent.domain.model.SubscriptionLimit
import uz.tikoncha_parent.domain.model.UserInfo

data class LocationState(
    val showGpsDialog: Boolean? = null,
    val locationData: LatLng? = null,
    val childrenLocationList: List<ChildrenLocationItemDto> = emptyList(),
    val childrenLocationError: String = "",
    val childrenLocationLoading: Boolean = false,
    val childrenList: List<UserInfo> = emptyList(),
    val selectedChild: UserInfo? = null,
    val subscriptionLimit: SubscriptionLimit = SubscriptionLimit()
)