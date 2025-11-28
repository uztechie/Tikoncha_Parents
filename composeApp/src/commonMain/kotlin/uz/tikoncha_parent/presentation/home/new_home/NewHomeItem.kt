package uz.tikoncha_parent.presentation.home.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.baloo_2_medium
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.LargeIconSize
import uz.tikoncha_parent.ui.NewTextColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun NewHomeItem(
    onSettingSelected: (HomeSelectionItem) -> Unit
){

    val baloo2 = FontFamily(
        Font(Res.font.baloo_2_medium)
    )
    Column {
        HomeSelectionItem.values().forEach { selection ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CardCornerRadius))
                    .clickable { onSettingSelected(selection) }
                    .background(
                        color = ColorWhite,
                        shape = RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(selection.iconId),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(PrimaryColor),
                        modifier = Modifier.size(LargeIconSize)
                    )
                    SpaceMedium()
                    Column {
                        Text(
                            text = stringResource(selection.title),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W500,
                            color = NewTextColor,
                            fontFamily = baloo2,
                        )
                        CustomText(
                            text = stringResource(selection.subtitle),
                            fontSize = NormalTextSize,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryColor
                        )
                    }
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