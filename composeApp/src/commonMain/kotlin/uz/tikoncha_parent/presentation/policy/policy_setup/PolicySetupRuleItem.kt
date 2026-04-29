package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
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

@Composable
fun EmptyPolicySetupRuleItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String,
    painter: Painter = painterResource(Res.drawable.time_square),
    onItemClick: () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    val borderColor = AppColors.border.primary   // [CHANGE] secondary → primary (kontrastliroq)
    val density = LocalDensity.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            // [REMOVE] simpleShadow — empty item'da shadow bo'lmasligi kerak,
            // chunki u "raised/configured" hissini beradi
            .drawBehind {
                val outline = shape.createOutline(size, layoutDirection, density)
                val path = when (outline) {
                    is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
                    is Outline.Rounded  -> Path().apply { addRoundRect(outline.roundRect) }
                    is Outline.Generic  -> outline.path
                }
                drawPath(
                    path = path,
                    color = borderColor,
                    style = Stroke(
                        width = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 8f), 0f)
                    )
                )
            }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .singleClick { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AppColors.bg.primary)
                        .size(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        // [CHANGE] icon.inverse (oq) → icon.accentPrimary (rangli)
                        tint = AppColors.icon.inverse
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 12.dp).weight(1f)
                ) {
                    Text(
                        text = title,
                        style = AppTypography.titleLgSemiBold,
                        color = AppColors.text.primary
                    )
                    SpaceUltraSmall()
                    Text(
                        text = subTitle,
                        style = AppTypography.emphasizedXsMedium,
                        // [CHANGE] text.primary → text.tertiary (muted, "hint" tuyg'usi)
                        color = AppColors.text.tertiary,
                    )
                }
            }
        }

        // [ADD] O'ng tomonda "+" affordance — bu "qo'shish kerak" signali
        SpaceSmall()

        val plusShape = CircleShape
        val plusBorderColor = AppColors.border.primary  // border rangi

        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .drawBehind {
                    val outline = plusShape.createOutline(size, layoutDirection, density)
                    val path = when (outline) {
                        is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
                        is Outline.Rounded   -> Path().apply { addRoundRect(outline.roundRect) }
                        is Outline.Generic   -> outline.path
                    }
                    drawPath(
                        path = path,
                        color = plusBorderColor,
                        style = Stroke(
                            width = 3f,
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10f, 4f),   // dash kichikroq — circle kichkina
                                phase = 0f
                            )
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = AppColors.icon.secondary   // muted, "qo'shish" hint
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOfPolicySetupRuleItem() {
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColors.bg.page)
        ) {
            PolicySetupRuleItem(
                title = "Vaqt",
                subTitle = "Ish vaqti dam olish kuni",
                canRemove = true,
                onRemoveClick = {},
                onItemClick = {}
            )

            Space(16.dp)
            EmptyPolicySetupRuleItem(
                title = "Vaqt",
                subTitle = "Ish vaqti dam olish kuni",
                onItemClick = {}
            )
        }
    }
}