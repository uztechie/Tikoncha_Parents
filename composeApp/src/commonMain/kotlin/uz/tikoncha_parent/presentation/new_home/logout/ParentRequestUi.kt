package uz.tikoncha_parent.presentation.new_home.logout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.jarayonda
import tikoncha_parents.composeapp.generated.resources.ruxsat_yoq
import tikoncha_parents.composeapp.generated.resources.tasdiqlandi
import uz.tikoncha_parent.ui.ImportantButtonColor
import uz.tikoncha_parent.ui.MediumButtonColor
import uz.tikoncha_parent.ui.MostImportantButtonColor
import uz.tikoncha_parent.ui.theme.AppColors

data class ParentRequestUi(
    val userId: String = "",
    val requestId: String = "",
    val type: ParentRequestType,
    val status: ParentRequestStatus = ParentRequestStatus.ALLOWED,
    val createdAt: String = "",
    val childName: String = ""
)

enum class ParentRequestStatus(
    val title: StringResource,
    val statusId: String
) {
    DENIED(Res.string.ruxsat_yoq, "deny"),
    PROCESS(Res.string.jarayonda, "process"),
    ALLOWED(Res.string.tasdiqlandi, "access");

    companion object {
        fun fromString(value: String): ParentRequestStatus = when (value) {
            "process" -> PROCESS
            "access" -> ALLOWED
            "deny" -> DENIED
            else -> DENIED
        }
    }
}

val ParentRequestStatus.color: Color
    @Composable
    get() = when (this) {
        ParentRequestStatus.DENIED  -> AppColors.bg.accentDanger
        ParentRequestStatus.PROCESS -> AppColors.bg.accentWarning
        ParentRequestStatus.ALLOWED -> AppColors.text.accentSuccess
    }