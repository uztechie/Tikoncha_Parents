package uz.tikoncha_parent.presentation.new_home.logout

data class LogoutUi(
    val requestId: String,
    val type: LogoutType,
    val createdAt: String = ""
)