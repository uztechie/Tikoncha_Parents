package uz.tikoncha_parent.presentation.profile.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_right
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun ProfileSettingsItem(
    onSettingSelected: (SettingType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.bg.secondary)
    ) {
        SettingType.entries.forEachIndexed { index, setting ->
            val isLast = index == SettingType.entries.lastIndex

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .singleClick { onSettingSelected(setting) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            AppColors.bg.surfaceTertiary,
                            shape = RoundedCornerShape(NormalIconButtonPadding)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(setting.iconId),
                        contentDescription = null,
                        tint = if (setting.isDestructive)
                            AppColors.icon.accentDanger
                        else
                            AppColors.icon.accentPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))

                Text(
                    text = stringResource(setting.settingName),
                    style = AppTypography.titleSmMedium,
                    color = if (setting.isDestructive)
                        AppColors.text.accentDanger
                    else
                        AppColors.text.primary,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(Res.drawable.arrow_right),
                    contentDescription = null,
                    tint = AppColors.icon.tertiary,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (!isLast) {
                DividerHorizontal(
                    modifier = Modifier.padding(start = 64.dp)
                )
            }
        }
    }
}

enum class SettingType(
    val iconId: DrawableResource,
    val settingName: StringResource,
    val isDestructive: Boolean = false
) {
    THEME(
        iconId = Res.drawable.star_setting,
        settingName = Res.string.tema
    ),
    LOGOUT(
        iconId = Res.drawable.logout,
        settingName = Res.string.akkauntdan_chiqish,
        isDestructive = true
    )
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        ProfileSettingsItem(onSettingSelected = {})
    }
}