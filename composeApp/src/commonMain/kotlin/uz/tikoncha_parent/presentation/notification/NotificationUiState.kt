package uz.tikoncha_parent.presentation.notification

import uz.tikoncha_parent.data.remote.model.NewsDto

data class NotificationUiState(
    val loading: Boolean = false,
    val items: List<NewsDto> = emptyList(),
    val error: String? = null
)