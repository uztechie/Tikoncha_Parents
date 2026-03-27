package uz.tikoncha_parent.presentation.chat.model

import androidx.compose.runtime.Immutable

@Immutable
sealed class ChatDateLabel {
    data class Time(val hhmm: String): ChatDateLabel()
    data object Unknown: ChatDateLabel()
    data object Today: ChatDateLabel()
    data object Yesterday: ChatDateLabel()
    data class Date(val ddMonth: String): ChatDateLabel()
}