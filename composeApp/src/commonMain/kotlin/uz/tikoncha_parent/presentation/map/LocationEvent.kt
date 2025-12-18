package uz.tikoncha_parent.presentation.map

import uz.tikoncha_parent.domain.model.UserInfo

sealed class LocationEvent{
    data class SetSelectedChildId(val childId: String?): LocationEvent()
}
