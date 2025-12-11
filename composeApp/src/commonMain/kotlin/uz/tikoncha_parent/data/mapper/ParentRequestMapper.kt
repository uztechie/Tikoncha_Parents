package uz.tikoncha_parent.data.mapper

import uz.tikoncha_parent.common.DateTimeUtil.fromServerToLocalDateTime
import uz.tikoncha_parent.common.DateTimeUtil.toUIData
import uz.tikoncha_parent.data.remote.model.parent_requests.ParentRequestsResponseDto
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestStatus
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestType
import uz.tikoncha_parent.presentation.new_home.logout.ParentRequestUi

fun ParentRequestsResponseDto.toLogoutUi(): ParentRequestUi {
    val due = created_at.fromServerToLocalDateTime()
    return ParentRequestUi(
        requestId = id,
        type = ParentRequestType.fromString(action),
        createdAt = due.toUIData(),
        childName = first_name?:last_name?:"",
        userId = user_id,
        status = ParentRequestStatus.fromString(status)
    )
}