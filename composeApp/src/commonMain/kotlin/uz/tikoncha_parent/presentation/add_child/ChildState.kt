package uz.tikoncha_parent.presentation.add_child

data class ChildState(
    val confirmCode: String = "",
    val number: String = "",
    val fullNumber: String = "",
    var accept: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = "",
    val success: Boolean = false
)
