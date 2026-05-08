package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_down_reg
import uz.tikoncha_parent.presentation.policy.shared.CategorySelectionState
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.ui.Space
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

@Composable
fun AppCategoryHeaderItem(
    modifier: Modifier = Modifier,
    group: CategoryGroupUi,
    sharedState: PolicySharedState,
    expanded: Boolean,
    enabled: Boolean,
    searchQuery: String = "",                                    // YANGI
    onToggleExpand: () -> Unit,
    onToggleCategory: () -> Unit,
    onToggleApp: (AppSelectionUi) -> Unit,
    onToggleFeature: (AppFeatureUi) -> Unit = {},                // YANGI
) {
    val packageNames = remember(group.apps) { group.apps.map { it.packageName } }
    val featureKeys = remember(group.apps) {                     // YANGI
        group.apps.flatMap { app ->
            AppFeatures.featuresFor(app.packageName).map { it.key }
        }
    }

    val selectionState = sharedState.categoryState(group.id, packageNames, featureKeys)

    val selectedCount = when (selectionState) {
        CategorySelectionState.ALL -> group.apps.size
        CategorySelectionState.NONE -> 0
        CategorySelectionState.PARTIAL -> group.apps.count { app ->
            val appSelected = sharedState.selectedPkgs.any {
                it.equals(app.packageName, ignoreCase = true)
            }
            val anyFeatureSelected = AppFeatures.featuresFor(app.packageName).any { feat ->
                sharedState.selectedFeatures.any { it.equals(feat.key, ignoreCase = true) }
            }
            appSelected || anyFeatureSelected
        }
    }

    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrow_rotation",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        // ── Header row (o'zgarmagan) ───────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 16.dp)
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = onToggleExpand,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_down_reg),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotationAngle),
                tint = AppColors.icon.secondary,
            )

            Space(16.dp)

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(AppColors.bg.surface, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.emoji,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp,
                )
            }

            Space(16.dp)

            Text(
                text = group.displayName,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = AppColors.text.primary,
                style = AppTypography.titleMdMedium,
            )

            Space(8.dp)

            Text(
                text = "$selectedCount/${group.apps.size}",
                color = AppColors.text.primary,
                style = AppTypography.titleSmMedium,
            )

            Space(8.dp)

            AppTripleCheckbox(
                state = when (selectionState) {
                    CategorySelectionState.NONE -> TripleCheckState.Off
                    CategorySelectionState.PARTIAL -> TripleCheckState.Indeterminate
                    CategorySelectionState.ALL -> TripleCheckState.On
                },
                onStateChange = { if (enabled) onToggleCategory() },
                modifier = Modifier.size(24.dp),
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.surface)
                    .padding(start = 48.dp, end = 16.dp)
            ) {
                group.apps.forEach { app ->
                    val visibleFeatures = AppFeatures.visibleFeaturesFor(app, searchQuery)
                    AppRowWithFeatures(                          // YANGI: AppRowItem o'rniga
                        app = app,
                        features = visibleFeatures,
                        isAppSelected = sharedState.isAppSelected(app.packageName, app.category),
                        selectedFeatureKeys = sharedState.selectedFeatures,
                        enabled = enabled,
                        onAppToggle = { onToggleApp(app) },
                        onFeatureToggle = onToggleFeature,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCategory() {
    TikonchaParentTheme {
        AppCategoryHeaderItem(
            group = CategoryGroupUi(
                id = "Ijtimoiy tarmoqlar",
                emoji = "\uD83D\uDC65",
                displayName = "Ijtimoiy tarmoqlar",
                apps = listOf(
                    AppSelectionUi("Telegram", "org.telegram.messenger", "Social"),
                    AppSelectionUi("Instagram", "com.instagram.android", "Social"),
                    AppSelectionUi("TikTok", "com.zhiliaoapp.musically", "Social"),
                ),
            ),
            sharedState = PolicySharedState(
                selectedPkgs = setOf("org.telegram.messenger"),
            ),
            expanded = true,
            enabled = true,
            onToggleExpand = {},
            onToggleCategory = {},
            onToggleApp = {},
        )
    }
}