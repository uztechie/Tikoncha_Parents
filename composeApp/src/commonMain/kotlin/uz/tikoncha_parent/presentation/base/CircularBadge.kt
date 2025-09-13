package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.tikoncha_parent.ui.ImportantButtonColor
import uz.tikoncha_parent.ui.MostImportantButtonColor
import uz.tikoncha_parent.ui.PrimaryColor

@Composable
fun Dp.dpToPx(): Float {
    val density = LocalDensity.current
    val dp = this
    return with(density) { dp.toPx() }
}

@Composable
fun Float.pxToDp(): Dp {
    val density = LocalDensity.current
    val px = this
    return with(density) { px.toDp() }
}

@Composable
fun CircularBadge(
    count: Int,
    diameter: Dp = 20.dp,               // 1–2 raqam uchun 20dp yaxshi
    bg: Color = PrimaryColor,
    fg: Color = Color.White
) {

    val widthInPixel = diameter.dpToPx()

    val text = when {
        count <= 0 -> return
        count > 999 -> "999"              // doira saqlash uchun "99+" o‘rniga "99"
        else -> count.toString()
    }

    val newWidthInPixel: Float = if (count>99){
        widthInPixel*2
    }
    else{
        widthInPixel
    }

    Box(
        modifier = Modifier
            .height(diameter)
            .width(newWidthInPixel.pxToDp())
            .background(bg, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            lineHeight = 10.sp
        )
    }
}