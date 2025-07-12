package org.example.project.presentation.profile.personal_information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.common.CustomText
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ContainerPadding
import org.example.project.ui.HintTextColor
import org.example.project.ui.MainCornerRadius
import org.example.project.ui.NormalIconSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceUltraSmall
import org.example.project.ui.TextColor
import org.example.project.ui.UltraSmallTextSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.profile

@Composable
fun PersonalInformationItem(
    icon: DrawableResource,
    title: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MainCornerRadius))
            .background(BackgroundColor)
            .padding(horizontal = ContainerPadding, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(icon),
            contentDescription = "",
            tint = PrimaryColor,
            modifier = Modifier
                .size(NormalIconSize)
        )

        SpaceUltraSmall()

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            CustomText(
                text = title,
                fontSize = UltraSmallTextSize,
                color = HintTextColor,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle()
            )

            SpaceUltraSmall()

            CustomText(
                text = value,
                fontSize = SmallTextSize,
                color = TextColor,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                style = TextStyle()
            )

        }

    }
}

@Preview
@Composable
private fun Pre(){
    PersonalInformationItem(
        icon = Res.drawable.profile,
        title = "Ism",
        value = "Shuxratov Saidburxon Dilmurod o'g'li"
    )
}