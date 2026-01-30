package uz.tikoncha_parent.presentation.profile.logout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import uz.tikoncha_parent.presentation.base.CustomText

@Composable
fun BulletTex(
    text: String,
    fonSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ){
        CustomText(
            text = "•  "
        )
        CustomText(
            text = text
        )
    }
}