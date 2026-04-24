package uz.tikoncha_parent.presentation.profile.settings

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.*
import tikoncha_parents.composeapp.generated.resources.star_setting
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun ProfileSettingsItem(
    onSettingSelected: (SettingType) -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        SettingType.entries.forEach { setting ->

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
                            MaterialTheme.extendedColor.tonalButtonColor,
                            RoundedCornerShape(NormalIconButtonPadding)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(setting.iconId),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.primaryAlphaColor),
                        modifier = Modifier.size(24.dp)
                    )
                }

                SpaceLarge()

                Column {

                    CustomText(
                        text = stringResource(setting.settingName),
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.W500
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = {
                        onSettingSelected(setting)
                    }
                ) {
                    Image(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.onBackgroundColor)
                    )
                }
            }
            DividerHorizontal()
        }
    }
}

enum class SettingType(
    val iconId: DrawableResource,
    val settingName: StringResource,
    val subtitle: StringResource,

    ) {
    THEME(
        iconId = Res.drawable.star_setting,
        settingName = Res.string.tema,
        subtitle = Res.string.yorug
    ),
    LOGOUT(
        iconId = Res.drawable.logout,
        settingName = Res.string.akkauntdan_chiqish,
        subtitle = Res.string.akkauntdan_chiqish
    ),
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ProfileSettingsItem(
            onSettingSelected = {}
        )
    }


}