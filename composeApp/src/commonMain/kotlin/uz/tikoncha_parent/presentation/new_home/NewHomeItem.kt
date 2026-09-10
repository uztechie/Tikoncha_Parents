package uz.tikoncha_parent.presentation.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.ui.HomeIconSize
import uz.tikoncha_parent.ui.HomeItemHeight
import uz.tikoncha_parent.ui.LargeCardCornerRadius
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun NewHomeItem(
    onSettingSelected: (HomeSelectionItem) -> Unit
){
    Column {
        HomeSelectionItem.entries.forEach { selection ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(LargeCardCornerRadius))
                    .clickable { onSettingSelected(selection) }
                    .height(HomeItemHeight)
                    .background(
                        color = AppColors.section.tertiary,
                        shape = RoundedCornerShape(LargeCardCornerRadius)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(selection.title),
                            color = AppColors.text.primary,
                            style = AppTypography.displaySmRegular
                        )
                        Text(
                            text = stringResource(selection.subtitle),
                            color = AppColors.text.secondary,
                            style = AppTypography.titleSmMedium,
                        )
                    }
                    SpaceMedium()
                    Image(
                        painter = painterResource(selection.iconId),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(end = selection.iconEndPadding)
                            .size(HomeIconSize)
                    )
                }
            }
            Space(12.dp)
        }
    }
}

@Composable
@Preview
private fun Preview(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        NewHomeItem(
            onSettingSelected = {}
        )
    }
}