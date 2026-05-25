package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.theme.AppColors

@Composable
fun AppRowWithFeatures(
    modifier: Modifier = Modifier,
    app: AppSelectionUi,
    features: List<AppFeatureUi>,
    isAppSelected: Boolean,
    selectedFeatureKeys: Set<String>,
    coveredByCategory: Boolean = false,
    enabled: Boolean = true,
    expanded: Boolean = false,
    onToggleExpand: () -> Unit = {},
    onAppToggle: () -> Unit,
    onFeatureToggle: (AppFeatureUi) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        // YouTube/Instagram qatori — 16dp content padding ichida
        AppRowItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            app = app,
            isSelected = isAppSelected,
            coveredByCategory = coveredByCategory,
            enabled = enabled,
            expandable = features.isNotEmpty(),
            expanded = expanded,
            onExpandToggle = onToggleExpand,
            onToggle = onAppToggle,
        )

        // Features section — bg full screen width, content indent ichida
        if (expanded && features.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.surface)            // bg avval — full width
                    .padding(start = 64.dp, end = 16.dp),        // padding keyin — content indent
            ) {
                features.forEach { feature ->
                    AppFeatureRowItem(
                        modifier = Modifier.fillMaxWidth(),
                        feature = feature,
                        parentIconUrl = app.iconUrl,
                        isSelected = selectedFeatureKeys.any {
                            it.equals(feature.key, ignoreCase = true)
                        },
                        coveredByCategory = coveredByCategory,
                        enabled = enabled,
                        onToggle = { onFeatureToggle(feature) },
                    )
                }
            }
        }
    }
}