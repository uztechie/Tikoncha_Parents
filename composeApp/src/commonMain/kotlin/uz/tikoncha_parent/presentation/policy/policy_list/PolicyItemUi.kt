package uz.tikoncha_parent.presentation.policy.policy_list

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.serialization.Serializable
import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.PolicyType
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.domain.model.policy.PolicyTemplate
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleUi
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleUi

@Serializable
data class PolicyItemUi(
    val ruleId: String,
    val policyName: String,
    val action: PolicyAction,
    val isMine: Boolean,
    val hasTimeRule: Boolean,
    val hasLimitRule: Boolean,
    val hasLocationRule: Boolean,
    val policyType: PolicyType,
    val isActive: Boolean,
    val packages: List<String>,
    val categories: List<String>,
    val sites: List<String>,
    val features: List<String>,
    val timeRule: List<TimeRuleUi>,
    val limitRule: List<LimitRuleUi>,
    val locationRule: LocationRule?,
    val policyTemplate: PolicyTemplate? = null,

    ):JavaSerializable{

    val appCount: Int get() = packages.count() + features.count()
    val webCount: Int get() = sites.count()

    val categoryCount: Int get() = categories.count()

    val featureCount: Int get() = features.count()

}
