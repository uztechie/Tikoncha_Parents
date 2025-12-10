package uz.tikoncha_parent.presentation.new_home.logout

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*


enum class LogoutType(
    val iconId: DrawableResource,
    val title: StringResource,
) {
    LOGOUT(
        Res.drawable.logout,
        Res.string.hisobdan_chiqish
    ),
    DELETE(
        Res.drawable.delete,
        Res.string.ilovani_ochirish
    );

    companion object {
        fun fromString(value: String): LogoutType {
            return when (value) {
                "logout" -> LogoutType.LOGOUT
                "delete" -> LogoutType.DELETE
                else -> LogoutType.LOGOUT
            }
        }

        fun LogoutType.toValue(): String = when (this) {
            LogoutType.LOGOUT -> "logout"
            LogoutType.DELETE -> "delete"
        }
    }
}