package uz.tikoncha_parent.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class PermissionItem(
    val title: StringResource,
    val isEnabled: Boolean,
    val icon: DrawableResource
)
