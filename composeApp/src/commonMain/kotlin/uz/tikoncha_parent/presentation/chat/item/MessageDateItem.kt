package uz.tikoncha_parent.presentation.chat.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.chat.ChatUtil.asText
import uz.tikoncha_parent.presentation.chat.model.ChatDateLabel
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ChatTextSize
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun MessageDateItem(
    modifier: Modifier = Modifier,
    date: ChatDateLabel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .background(
                    color = AppColors.section.secondary,
                    shape = RoundedCornerShape(CardCornerRadius)
                )
                .padding(vertical = 4.dp, horizontal = 15.dp),
        ){
            Text(
                text = date.asText(),
                style = AppTypography.bodyLgMedium,
                color = AppColors.text.primary,
            )
        }
    }
}

@Preview
@Composable
private fun Pre() {

    TikonchaParentTheme(
        mode = ThemeMode.LIGHT
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MessageDateItem(
                date = ChatDateLabel.Yesterday
            )
        }
    }


}