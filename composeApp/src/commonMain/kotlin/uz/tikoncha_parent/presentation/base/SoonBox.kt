package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.UltraSmallTextSize

@Composable
fun SoonBox(
    modifier: Modifier
){
    Box(
        modifier = modifier
            .padding(end = 10.dp, bottom = 10.dp)
            .background(PrimaryColor, CircleShape)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ){
        CustomText(
            text = "Tez kunda",
            fontSize = UltraSmallTextSize,
            color = Color.White,
            modifier = Modifier,
            lineHeight = UltraSmallTextSize

        )
    }
}