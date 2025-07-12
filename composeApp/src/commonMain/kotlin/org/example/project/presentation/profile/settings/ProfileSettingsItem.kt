package org.example.project.presentation.profile.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.BackgroundColor
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.HintTextColor
import org.example.project.ui.LanguageTonalIconColor
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalTextSize
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.SpaceSmall
import org.example.project.ui.SpaceUltraSmall
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.bell_notification
import tikoncha_parents.composeapp.generated.resources.star_setting
import tikoncha_parents.composeapp.generated.resources.warning_1

@Composable
fun ProfileSettingsItem(
    selectedSetting: SettingType,
    onSettingSelected: (SettingType) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor),
    ) {

        SettingType.values().forEach { setting ->

            SpaceMedium()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { onSettingSelected(setting) }
            ) {


                // Flag icon with rounded rectangle background
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            LanguageTonalIconColor,
                            RoundedCornerShape(NormalIconButtonPadding)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(setting.iconId),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }

                SpaceLarge()

                Column {

                    Text(
                        text = setting.settingName,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.W500
                    )

                    SpaceUltraSmall()

                    Text(
                        text = setting.subtitle,
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500,
                        color = HintTextColor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Radio Circle (border + inner circle if selected)
                IconButton(
                    onClick = {
                        onSettingSelected(setting)
                    }
                ) {
                    Image(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = "",
                    )
                }
            }

            SpaceSmall()

            DividerHorizontal()
        }

        SpaceLarge()
    }
}

enum class SettingType(
    val iconId: DrawableResource,
    val settingName: String,
    val subtitle: String,

    ) {
    NOTIFICATION(
        iconId = Res.drawable.bell_notification,
        settingName = "Bildirishnomalar",
        subtitle = "Reklamalar"
    ),
    THEME(iconId = Res.drawable.star_setting, settingName = "Tema", subtitle = "Yorug`"),
    DANGEROUS_ZONE(
        iconId = Res.drawable.warning_1,
        settingName = "Xavfli zona",
        subtitle = "Akkauntni o’chirish"
    ),
}

@Preview
@Composable
private fun Preview() {
    ProfileSettingsItem(
        selectedSetting = SettingType.NOTIFICATION,
        onSettingSelected = {}
    )


}