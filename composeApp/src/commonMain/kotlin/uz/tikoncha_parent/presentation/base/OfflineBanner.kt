// commonMain/presentation/base/OfflineBanner.kt
package uz.tikoncha_parent.presentation.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.iltimos_internetga_ulang
import uz.tikoncha_parent.presentation.chat.chat_list.ChatState
import uz.tikoncha_parent.presentation.chat.chat_list.ChatUi
import uz.tikoncha_parent.ui.OtpErrorColor
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun OfflineBanner(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = !isOnline,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OtpErrorColor.copy(0.2f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon o'rniga oddiy belgi (sizda WiFi off icon bor bo'lsa, qo'ying)
            Text(
                text = "📡",
                color = Color.White,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.iltimos_internetga_ulang),
                color = AppColors.text.accentWarning
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ) {
        OfflineBanner(
            isOnline = false,
        )
    }
}