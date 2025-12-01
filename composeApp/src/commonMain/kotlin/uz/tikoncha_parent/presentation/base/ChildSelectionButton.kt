package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile_hedgehog_img
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.DialogButtonHeight
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.TextFieldInnerPadding
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun ChildSelectionButton(
    modifier: Modifier = Modifier,
    text: String,
    label: String = "",
    imageUrl: String = "",
    onClick: () -> Unit,
    shape: Shape = CircleShape,
    fonSize: TextUnit = NormalTextSize,
    fontWeight: FontWeight = FontWeight.Normal,
    background: Color = MaterialTheme.extendedColor.cardColor,
) {

    val color = if (text.isEmpty()) MaterialTheme.extendedColor.hintColor else MaterialTheme.extendedColor.primaryColor
    val newText = text.ifEmpty { label }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(background, shape)
            .padding(horizontal = 8.dp)
            .height(DialogButtonHeight)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onClick()
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Box(
            modifier = Modifier
                .size(30.dp)
                .background(MaterialTheme.extendedColor.backgroundColor,  CircleShape)
                .padding(2.dp)
        ){
            AsyncImage(
                model = imageUrl,
                contentDescription = "",
                error = painterResource(Res.drawable.profile_hedgehog_img),
                placeholder = painterResource(Res.drawable.profile_hedgehog_img),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }
        SpaceSmall()



        CustomText(
            text = newText,
            fontSize = fonSize,
            fontWeight = fontWeight,
            color = color,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorWhite)
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChildSelectionButton(
                text = "",
                onClick = {},
                label = "Viloyat"
            )
        }
    }
}