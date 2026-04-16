package uz.tikoncha_parent.presentation.policy.policy_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.discord_icon
import tikoncha_parents.composeapp.generated.resources.google_icon
import tikoncha_parents.composeapp.generated.resources.instagram_icon
import tikoncha_parents.composeapp.generated.resources.jadval_yaratish
import tikoncha_parents.composeapp.generated.resources.jadvallar
import tikoncha_parents.composeapp.generated.resources.kunlik_bloklashlarni_rejalashtiring
import tikoncha_parents.composeapp.generated.resources.linkedin_icon
import tikoncha_parents.composeapp.generated.resources.social_x_icon
import tikoncha_parents.composeapp.generated.resources.telegram
import tikoncha_parents.composeapp.generated.resources.whatsapp_icon
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.ContainerCornerRadius
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme


@Composable
fun CreatePolicyCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ContainerCornerRadius))
            .singleClick {
                onClick()
            }
            .background(
                AppColors.bg.surface,
                RoundedCornerShape(ContainerCornerRadius)
            )
            .padding(12.dp),
    ) {
        Text(
            text = stringResource(Res.string.jadvallar),
            style = AppTypography.titleLgSemiBold,
            color = AppColors.text.primary,
        )
        Space(12.dp)

        Text(
            text = stringResource(Res.string.kunlik_bloklashlarni_rejalashtiring),
            style = AppTypography.emphasizedMdMedium,
            color = AppColors.text.secondary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.instagram_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.google_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.whatsapp_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.linkedin_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.discord_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.social_x_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Image(
                painter = painterResource(Res.drawable.telegram),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        CustomButtonNew(
            text = stringResource(Res.string.jadval_yaratish),
            onClick = onClick,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme (
        ThemeMode.LIGHT
    ) {
        CreatePolicyCard { }
    }
}