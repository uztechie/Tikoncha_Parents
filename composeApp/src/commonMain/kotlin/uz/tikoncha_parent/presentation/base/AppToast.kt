package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.checked
import tikoncha_parents.composeapp.generated.resources.info_profile_us
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

enum class ToastType {
    Success, Error
}

data class ToastData(
    val message: String,
    val type: ToastType,
)

// presentation/base/components/AppToast.kt

@Composable
fun AppToast(
    toastData: ToastData?,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(toastData) {
        if (toastData != null) {
            delay(3000L)
            onDismiss()
        }
    }

    if (toastData != null) {
        Popup(
            alignment = Alignment.BottomCenter,
            offset = IntOffset(0, -220),  // buttondan yuqoriroq
            onDismissRequest = onDismiss,
            properties = PopupProperties(

            )
        ) {
            val backgroundColor = when (toastData.type) {
                ToastType.Success -> AppColors.bg.primary
                ToastType.Error -> AppColors.bg.accentWarning
            }
            val iconRes = when (toastData.type) {
                ToastType.Success -> Res.drawable.checked
                ToastType.Error -> Res.drawable.info_profile_us
            }

            Row (
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = vectorResource(iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = toastData.message,
                    color = AppColors.text.inverse,
                    style = AppTypography.titleSmMedium,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        AppToast(
            toastData = ToastData(
                message = "Xatolik",
                type = ToastType.Error,
            ),
            onDismiss = {},
        )
    }
}