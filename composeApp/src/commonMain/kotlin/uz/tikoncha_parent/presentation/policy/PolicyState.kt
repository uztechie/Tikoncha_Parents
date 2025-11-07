package uz.tikoncha_parent.presentation.policy

import uz.tikoncha_parent.domain.model.UserInfo


data class PolicyState(
    val selectedChild: UserInfo? = null,
)
