package uz.tikoncha_parent.presentation.new_home.logout

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*


enum class ParentRequestType(
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
        fun fromString(value: String): ParentRequestType {
            return when (value) {
                "logout" -> ParentRequestType.LOGOUT
                "delete" -> ParentRequestType.DELETE
                else -> ParentRequestType.LOGOUT
            }
        }

        fun ParentRequestType.toValue(): String = when (this) {
            ParentRequestType.LOGOUT -> "logout"
            ParentRequestType.DELETE -> "delete"
        }
    }
}