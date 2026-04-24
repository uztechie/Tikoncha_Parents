package uz.tikoncha_parent.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import uz.tikoncha_parent.ui.AppIconInnerPadding
import uz.tikoncha_parent.ui.ProfileStatsContainerHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.coin
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.AppItemHeight
import uz.tikoncha_parent.ui.SmallCardCornerRadius
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun UserStatsItem(
    title: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .zIndex(1f)
            .clip(RoundedCornerShape(SmallCardCornerRadius))
    ){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(SmallCardCornerRadius))
                .singleClick(onClick = onClick)
                .background(AppColors.bg.surface, RoundedCornerShape(SmallCardCornerRadius))
                .padding(AppIconInnerPadding)
        ) {
            Text(
                text = title,
                style = AppTypography.emphasizedSmSemiBold,
                color = AppColors.text.primary,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            Text(
                text = value,
                style = AppTypography.emphasizedSmSemiBold,
                color = AppColors.text.accentEmphasis,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Image(
            painter = icon,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(AppItemHeight)
                .align(Alignment.BottomEnd)
                .offset(10.dp, 10.dp)
        )
    }
}

@Preview
@Composable
private fun Pre(){
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ){
        UserStatsItem(
            title = "Tangalaringiz",
            value = "44 ta",
            icon = painterResource(Res.drawable.coin),
            modifier = Modifier.fillMaxWidth().height(ProfileStatsContainerHeight)
        )
    }
}