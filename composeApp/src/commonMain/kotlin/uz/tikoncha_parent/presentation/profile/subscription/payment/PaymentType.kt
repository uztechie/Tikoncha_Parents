package uz.tikoncha_parent.presentation.profile.subscription.payment

import org.jetbrains.compose.resources.DrawableResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.click_pay

enum class PaymentType(
    val icon: DrawableResource?,
    val title: String?
) {
    Click(icon = Res.drawable.click_pay, null),
    AppStore(icon = null, "App Store"),

}