package uz.tikoncha_parent.presentation.profile.coins

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down_reg
import tikoncha_parents.composeapp.generated.resources.question
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun CoinQuestionItem(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(shape = RoundedCornerShape(16.dp))
            .background(AppColors.bg.surface, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(AppColors.bg.section, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(Res.drawable.question),
                contentDescription = "",
                modifier = Modifier
                    .size(22.dp),
                tint = AppColors.icon.primary
            )
        }
        Spacer(Modifier.height(12.dp))

        Text(
            text = title,
            color = AppColors.text.primary,
            style = AppTypography.titleSmMedium,
            modifier = Modifier
                .weight(1f)
        )

        Icon(
            painter = painterResource(Res.drawable.arrow_down_reg),
            contentDescription = "",
            tint = AppColors.icon.secondary,
            modifier = Modifier
                .size(22.dp)
        )
        Spacer(Modifier.height(5.dp))
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme (
        ThemeMode.LIGHT
    ) {
        CoinQuestionItem(
            title = "Salom dunyo"
        )
    }
}