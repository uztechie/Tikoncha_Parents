package org.example.project.domain.model

import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class PermissionItem(
    val title: StringResource,
    val isEnabled: Boolean,
    val icon: DrawableResource
)
