package uz.tikoncha_parent.presentation.policy.app_site_selection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppRowWithFeatures(
    modifier: Modifier = Modifier,
    app: AppSelectionUi,
    features: List<AppFeatureUi>,
    isAppSelected: Boolean,
    selectedFeatureKeys: Set<String>,
    enabled: Boolean = true,
    onAppToggle: () -> Unit,
    onFeatureToggle: (AppFeatureUi) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AppRowItem(
            modifier = Modifier.fillMaxWidth(),
            app = app,
            isSelected = isAppSelected,
            enabled = enabled,
            onToggle = onAppToggle,
        )
        features.forEach { feature ->
            AppFeatureRowItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp),
                feature = feature,
                parentIconUrl = app.iconUrl,
                isSelected = selectedFeatureKeys.any {
                    it.equals(feature.key, ignoreCase = true)
                },
                enabled = enabled,
                onToggle = { onFeatureToggle(feature) },
            )
        }
    }
}