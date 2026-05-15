package uz.tikoncha_parent.presentation.add_child

sealed interface AddChildEffect {
    data class OpenUrl(val url: String) : AddChildEffect
    data class ShareText(val text: String) : AddChildEffect
    data object PlayTutorialVideo : AddChildEffect
    data object NavigateBack : AddChildEffect
}