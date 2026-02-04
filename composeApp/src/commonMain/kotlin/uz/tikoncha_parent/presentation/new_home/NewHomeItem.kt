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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.HomeIconSize
import uz.tikoncha_parent.ui.HomeItemHeight
import uz.tikoncha_parent.ui.LargeIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.UltraLargeIconButtonSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun NewHomeItem(
    onSettingSelected: (HomeSelectionItem) -> Unit
){
    Column {
        HomeSelectionItem.entries.forEach { selection ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CardCornerRadius))
                    .clickable { onSettingSelected(selection) }
                    .height(HomeItemHeight)
                    .background(
                        color = MaterialTheme.extendedColor.cardColor,
                        shape = RoundedCornerShape(CardCornerRadius)
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
                        CustomText(
                            text = stringResource(selection.title),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.extendedColor.titleColor,
                        )
                        CustomText(
                            text = stringResource(selection.subtitle),
                            fontSize = SmallTextSize,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.extendedColor.titleColor,
                        )
                    }
                    SpaceMedium()
                    Image(
                        painter = painterResource(selection.iconId),
                        contentDescription = "",
                        modifier = Modifier.size(HomeIconSize)
                    )
                }
            }
            SpaceMedium()
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