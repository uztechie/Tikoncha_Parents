package uz.tikoncha_parent.presentation.map

// 2. MapMarker composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.UltraSmallTextSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.person

@Composable
fun MapMarker(
    title: String,
    lastUpdated: String? = null, // qo'shildi
    icon: Painter = painterResource(Res.drawable.person),
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF4CAF50),
    contentColor: Color = Color.White,
    cornerRadius: Dp = 8.dp,
    pointerWidth: Dp = 16.dp,
    pointerHeight: Dp = 10.dp,
    padding: Dp = 8.dp,
    iconSize: Dp = 20.dp,
    textSizeSp: TextUnit = SmallTextSize
) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = MarkerShape(cornerRadius, pointerWidth, pointerHeight)
            )
            .padding(bottom = pointerHeight)
            .padding(horizontal = padding, vertical = padding),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = title,
                    color = contentColor,
                    fontSize = textSizeSp,
                    maxLines = 1,
                    lineHeight = textSizeSp
                )
                // Agar sana null bo'lmasa, chiqaramiz
                if (lastUpdated != null) {
                    Text(
                        lineHeight = UltraSmallTextSize,
                        text = lastUpdated,
                        color = contentColor.copy(alpha = 0.7f),
                        fontSize = UltraSmallTextSize,
                        maxLines = 1
                    )
                }
            }
        }
    }
}



@Preview
@Composable
fun Pre(){
    MapMarker(
        title = "Mexanizatsiyalashtirilmaganimizdandir",
        icon = painterResource(Res.drawable.person),
    )
}