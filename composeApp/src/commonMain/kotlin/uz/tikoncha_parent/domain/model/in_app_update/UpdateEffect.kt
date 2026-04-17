package uz.tikoncha_parent.domain.model.in_app_update

sealed interface UpdateEffect {
    data class StartUpdateFlow(val type: UpdateType) : UpdateEffect
    data object OpenAppStore : UpdateEffect
}