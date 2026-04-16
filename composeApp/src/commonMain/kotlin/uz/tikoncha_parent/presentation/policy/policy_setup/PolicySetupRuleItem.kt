package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.close_remove
import tikoncha_parents.composeapp.generated.resources.time_square
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.base.CloseButton
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun PolicySetupRuleItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    canRemove: Boolean = false,
    painter: Painter = painterResource(Res.drawable.time_square),
    onRemoveClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .simpleShadow(
                shape = RoundedCornerShape(24.dp),
            )
            .background(
                AppColors.bg.section,
                RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .singleClick { onItemClick() },
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AppColors.section.primary)
                        .size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp),
                        tint = AppColors.icon.inverse
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = title,
                        style = AppTypography.titleMdSemiBold,
                        color = AppColors.text.primary
                    )
                    SpaceUltraSmall()

                    Text(
                        text = subTitle,
                        style = AppTypography.emphasizedXsMedium,
                        color = AppColors.text.primary,
                    )
                }
            }
        }

        if (canRemove) {
            SpaceSmall()
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                shape = CircleShape,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppColors.modal.secondary,
                    contentColor = AppColors.icon.secondary
                )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_remove),
                    contentDescription = "",
                    modifier = Modifier
                        .size(14.dp),
                )
            }

        }
    }
}

@Preview
@Composable
private fun PreviewOfPolicySetupRuleItem() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        PolicySetupRuleItem(
            title = "Vaqt",
            subTitle = "Ish vaqti dam olish kuni",
            canRemove = true,
            onRemoveClick = {},
            onItemClick = {}
        )
    }
}