package uz.tikoncha_parent.presentation.notification

import uz.tikoncha_parent.domain.model.app_error.Outcome
import uz.tikoncha_parent.domain.model.news.News

data class NotificationUiState(
    val loading: Boolean = false,
    val items: List<News> = emptyList(),
    val error: Outcome.Failure? = null,
)