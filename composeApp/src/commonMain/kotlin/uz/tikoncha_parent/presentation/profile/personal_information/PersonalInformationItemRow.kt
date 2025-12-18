package uz.tikoncha_parent.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.ShapeCornerRadius
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.TextFieldIconSize
import uz.tikoncha_parent.ui.UltraSmallTextSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun PersonalInformationItemRow(
    icon: DrawableResource,
    title: String,
    value: String?
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ShapeCornerRadius))
            .background(MaterialTheme.extendedColor.backgroundColor)
            .padding(horizontal = ContainerPadding, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = "",
            tint = MaterialTheme.extendedColor.primaryColor,
            modifier = Modifier
                .size(TextFieldIconSize)
        )

        SpaceSmall()

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {

            Text(
                text = title,
                fontSize = UltraSmallTextSize,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(),
            )

            Text(
                text = value?:"",
                fontSize = SmallTextSize,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                style = TextStyle(),
                color = MaterialTheme.extendedColor.textColor
            )
        }
    }
}

@Preview
@Composable
private fun Pre(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        PersonalInformationItemRow(
            icon = Res.drawable.profile,
            title = "Ism",
            value = "Shuxratov Saidburxon Dilmurod o'g'li"
        )
    }
}