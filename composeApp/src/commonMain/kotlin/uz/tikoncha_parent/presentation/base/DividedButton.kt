package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun DividedButton(
    modifier: Modifier = Modifier,
    success: Boolean = true,
    title: String,
    icon: Painter,
    isPermission: Boolean = false,
    onItemClick:() -> Unit
) {


    var contentColor = PrimaryColor
    var borderColor = PermissionBorderColor
    var checkIcon = painterResource(Res.drawable.permission_granted)

    if (success){
        contentColor = MaterialTheme.extendedColor.primaryColor
        borderColor = MaterialTheme.extendedColor.borderColor
        checkIcon = painterResource(Res.drawable.permission_granted)
    }
    else{
//        contentColor = Failed
//        borderColor = Failed
        contentColor = HintTextColor
        borderColor = BorderColor
        checkIcon = painterResource(Res.drawable.permission_denied)
    }

    if (!isPermission){
        checkIcon = painterResource(Res.drawable.arrow_right_rounded)
    }
    Row(
        modifier = modifier
            .height(ButtonHeight)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ){
                onItemClick()
            },
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .border(1.dp, borderColor, shape = RoundedCornerShape(TextFieldCornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "",
                    tint = contentColor,
                    modifier = Modifier
                        .size(SmallIconButtonSize)
                )

                Spacer(Modifier.padding(horizontal = 10.dp))

                CustomText(
                    text = title,
                    fontWeight = FontWeight.Normal,
                    lineHeight = NormalTextSize,
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .weight(1f)
                )
                Spacer(Modifier.padding(horizontal = 10.dp))
            }
        }

        SpaceSmall()

        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .border(1.dp, borderColor, shape = RoundedCornerShape(TextFieldCornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = checkIcon ,
                contentDescription = "",
                modifier = Modifier
                    .size(SmallIconButtonSize),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(contentColor)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DividedButton(
        modifier = Modifier
            .background(Color.White),
        onItemClick = {},
        title = "Title\n titl",
        icon = painterResource(Res.drawable.arrow_right),
        success = true,
        isPermission = false

    )






}