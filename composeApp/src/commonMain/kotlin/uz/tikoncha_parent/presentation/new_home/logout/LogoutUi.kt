package uz.tikoncha_parent.presentation.new_home.logout

import kotlinx.serialization.Serializable

data class LogoutUi(
    val requestId: String,
    val type: LogoutType,
    val status: LogoutStatus = LogoutStatus.ALLOWED,
    val createdAt: String = "",
    val childName: String = ""
)

enum class LogoutStatus(
    val statusId: String
){
    DENIED( "deny"),
    PROCESS("process"),
    ALLOWED("access"),
    UNKNOWN("");

    companion object{
        fun fromString(value: String): LogoutStatus {
            return when (value) {
                "process" -> LogoutStatus.PROCESS
                "access" -> LogoutStatus.ALLOWED
                "deny" -> LogoutStatus.DENIED
                else -> LogoutStatus.UNKNOWN
            }
        }
    }
}