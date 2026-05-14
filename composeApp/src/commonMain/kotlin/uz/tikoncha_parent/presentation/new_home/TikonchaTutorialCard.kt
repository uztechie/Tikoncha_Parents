package uz.tikoncha_parent.presentation.new_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.hedgehog_heart
import tikoncha_parents.composeapp.generated.resources.play
import tikoncha_parents.composeapp.generated.resources.tikoncha_title_bottom
import tikoncha_parents.composeapp.generated.resources.tikoncha_title_top
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.FredokaSemiBold
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun TikonchaTutorialCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(RoundedCornerShape(32.dp))
            .background(AppColors.modal.primary, RoundedCornerShape(32.dp))
            .clickable(
                indication = null,
                interactionSource = null,
                onClick = onClick
            )
            .padding(start = 25.dp, end = 25.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            Text(
                text = stringResource(Res.string.tikoncha_title_top),
                color = AppColors.text.primary,
                fontFamily = FredokaSemiBold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = stringResource(Res.string.tikoncha_title_bottom),
                color = AppColors.text.accentEmphasis,
                fontFamily = FredokaSemiBold,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
        Space(16.dp)
        Icon(
            painter = painterResource(Res.drawable.play),
            contentDescription = "",
            modifier = Modifier
                .size(52.dp),
            tint = AppColors.icon.accentPrimarySurface
        )
        Space(16.dp)
        Image(
            painter = painterResource(Res.drawable.hedgehog_heart),
            contentDescription = "",
            modifier = Modifier
                .height(120.dp)
        )
    }
}

@Preview
@Composable
private fun Pre(){
    TikonchaParentTheme {
        TikonchaTutorialCard(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {}
        )
    }
}