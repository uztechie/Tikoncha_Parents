package uz.tikoncha_parent.presentation.new_home.logout

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.StringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.jarayonda
import tikoncha_parents.composeapp.generated.resources.ruxsat_yoq
import tikoncha_parents.composeapp.generated.resources.tasdiqlandi
import uz.tikoncha_parent.ui.ImportantButtonColor
import uz.tikoncha_parent.ui.MediumButtonColor
import uz.tikoncha_parent.ui.MostImportantButtonColor

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
    val color: Color,
    val statusId: String
){
    DENIED(
        Res.string.ruxsat_yoq,
        color = MostImportantButtonColor,
        statusId = "deny"
    ),
    PROCESS(
        title = Res.string.jarayonda,
        color = ImportantButtonColor,
        statusId = "process"
    ),
    ALLOWED(
        title = Res.string.tasdiqlandi,
        color = MediumButtonColor,
        statusId = "access"
    );

    companion object{
        fun fromString(value: String): ParentRequestStatus {
            return when (value) {
                "process" -> ParentRequestStatus.PROCESS
                "access" -> ParentRequestStatus.ALLOWED
                "deny" -> ParentRequestStatus.DENIED
                else -> ParentRequestStatus.DENIED
            }
        }
    }
}