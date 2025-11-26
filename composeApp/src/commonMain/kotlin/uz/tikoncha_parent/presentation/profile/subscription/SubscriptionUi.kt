package uz.tikoncha_parent.presentation.profile.subscription

import uz.tikoncha_parent.domain.model.SubscriptionType

data class SubscriptionUi(
    val planId: String,
    val type: SubscriptionType,
    val monthly: PlanUi,
    val annual: PlanUi
)

data class PlanUi(
    val price: Int,
    val coin: Int,
    val feature: List<String>,
    val bonus: List<String>
)