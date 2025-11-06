package uz.tikoncha_parent.presentation.policy.rule_type_selection

import androidx.compose.ui.graphics.painter.Painter

data class RuleTypeUi(
    val type: RuleType,
    val icon: Painter,
    val title: String,
    val subtitle: String,
    val enabled: Boolean,
    val hasItems: Boolean,
)