package org.example.project.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.NormalIconButtonSize
import org.example.project.ui.NormalTextSize
import org.example.project.ui.ShapeCornerRadius
import org.example.project.ui.SpaceSmall
import org.example.project.ui.TextColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.obuna
import tikoncha_parents.composeapp.generated.resources.profile
import tikoncha_parents.composeapp.generated.resources.shaxsiy_malumotlar
import tikoncha_parents.composeapp.generated.resources.sozlamalar
import tikoncha_parents.composeapp.generated.resources.tangachalar
import tikoncha_parents.composeapp.generated.resources.til
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun ProfileSectionItem(
    icon: Painter,
    section: ProfileSection,
    onItemClick: (section: ProfileSection) -> Unit
) {

    val title = when(section){
        ProfileSection.PERSONAL_INFORMATION -> stringResource(Res.string.shaxsiy_malumotlar)
        ProfileSection.LANGUAGE -> stringResource(Res.string.til)
        ProfileSection.SETTINGS -> stringResource(Res.string.sozlamalar)
        ProfileSection.SUBSCRIPTIONS -> stringResource(Res.string.obuna)
        ProfileSection.COINS -> stringResource(Res.string.tangachalar)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable(
                enabled = true,
                onClick = { onItemClick(section) }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(NormalIconButtonSize)
                    .clip(RoundedCornerShape(ShapeCornerRadius))
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                )
            }

            SpaceSmall()

            CustomText(
                text = title,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )
        }

        SpaceSmall()

        DividerHorizontal()
    }
}

@Preview
@Composable
private fun Pre(){
    ProfileSectionItem(
        icon = painterResource(Res.drawable.profile),
        section = ProfileSection.PERSONAL_INFORMATION,
        onItemClick = {

        }
    )
}