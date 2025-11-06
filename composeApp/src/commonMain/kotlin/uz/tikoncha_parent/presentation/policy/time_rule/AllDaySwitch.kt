package uz.tikoncha_parent.presentation.policy.time_rule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.clock
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CustomSwitch
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.OnPrimaryColor
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallIconButtonSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun AllDaySwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(TextFieldHeight)
            .background(MaterialTheme.extendedColor.backgroundColor, RoundedCornerShape(ShapeCornerRadius))
            .border(1.dp, MaterialTheme.extendedColor.borderColor, RoundedCornerShape(ShapeCornerRadius))
            .padding(horizontal = ContainerPadding),
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(SmallIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.extendedColor.tonalButtonColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.clock),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(HintTextColor),
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                )
            }

            SpaceSmall()

            CustomText(
                text = "Kun davomida",
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.weight(1f))
            CustomSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                width = 38.dp,
                height = 24.dp,
                padding = 3.dp,
                trackOnColor = PrimaryColor,
                trackOffColor = HintTextColor,
                thumbOnColor = OnPrimaryColor,
                thumbOffColor = OnPrimaryColor
            )
        }
    }
}