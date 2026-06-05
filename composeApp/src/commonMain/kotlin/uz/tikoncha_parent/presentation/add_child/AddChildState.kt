package uz.tikoncha_parent.presentation.add_child

import org.jetbrains.compose.resources.StringResource
import uz.tikoncha_parent.presentation.base.multi_phone_input.Country
import uz.tikoncha_parent.presentation.base.multi_phone_input.DefaultCountry

data class AddChildState(
    val phoneNumber: String = "",            // raw digits, max 9
    val selectedCountry: Country = DefaultCountry,
    val requestedPhone: String = "",         // phone used in last successful request
    val code: String? = null,                // OTP code from server
    val isLoading: Boolean = false,          // requesting / refreshing
    val showBindChildTutorial: Boolean = false,  // tutorial card -> header icon
    val errorRes: StringResource? = null,    // error from Resource.Error
    val errorMessage: String? = null,        // error from Resource.Error
    val showCopiedSnackbar: Boolean = false  // "Kod nusxalandi" toast
) {

    val fullPhoneNumber : String get() =
        "${selectedCountry.dial}${phoneNumber}"
    val isPhoneValid: Boolean
        get() = phoneNumber.length == selectedCountry.maxDigits && phoneNumber.all { it.isDigit() }

    /** Code is shown only when phone matches the phone we requested for. */
    val showCodeCard: Boolean
        get() = !code.isNullOrEmpty() && phoneNumber == requestedPhone

    val canRequestCode: Boolean
        get() = isPhoneValid && !isLoading && !showCodeCard
}