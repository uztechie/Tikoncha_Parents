package uz.tikoncha_parent.presentation.profile.coins

data class MyCoinsState(
    val isLoading: Boolean = false,
    val coins: Int? = null,
    val error: String? = null
)
