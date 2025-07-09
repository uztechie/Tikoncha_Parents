package org.example.project.domain.model

import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.DrawableResource

data class PermissionItem(
    val title: String,
    val isEnabled: Boolean,
    val icon: DrawableResource
)
