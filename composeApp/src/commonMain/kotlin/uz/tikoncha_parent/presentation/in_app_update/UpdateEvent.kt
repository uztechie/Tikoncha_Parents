package uz.tikoncha_parent.presentation.in_app_update

import uz.tikoncha_parent.domain.model.in_app_update.UpdateType

sealed interface UpdateEvent {
    /* ==== LIFECYCLE ==== */
    data object ScreenStarted : UpdateEvent

    /* ==== UPDATE DIALOG ==== */
    data object DismissUpdateDialog : UpdateEvent
    data class StartUpdateClicked(
        val type: UpdateType
    ) : UpdateEvent



    /* ==== FLEXIBLE UPDATE ==== */
    data object RestartClicked : UpdateEvent
    data object InstallLater : UpdateEvent

    /* ==== MESSAGE ==== */
    data object ClearMessage : UpdateEvent
}