package uz.tikoncha_parent.presentation.home.schedule.type

import androidx.compose.ui.graphics.painter.Painter

data class ScheduleItem(
    val type: ScheduleType,
    val icon: Painter,
    val title: String,
    val subtitle: String,
)