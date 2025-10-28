package uz.tikoncha_parent.presentation.home.schedule.type

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ScheduleTypeItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    title: String,
    subTitle: String,
    enabled: Boolean = false
) {

    val bgColor = if (enabled) DisableButtonContentColor else MaterialTheme.extendedColor.cardColor


    Row(
        modifier = modifier
            .fillMaxWidth()
            .verticalShadow(
                shape = RoundedCornerShape(TextFieldCornerRadius),
                offset = 0.dp
            )
            .background(bgColor, RoundedCornerShape(TextFieldCornerRadius)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(NormalIconButtonSize)
                .clip(RoundedCornerShape(ShapeCornerRadius))
                .background(MaterialTheme.extendedColor.tonalButtonColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )
        }

        SpaceMedium()

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomText(
                text = title,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

            CustomText(
                text = subTitle,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.W500,
                color = HintTextColor
            )
        }
    }
}