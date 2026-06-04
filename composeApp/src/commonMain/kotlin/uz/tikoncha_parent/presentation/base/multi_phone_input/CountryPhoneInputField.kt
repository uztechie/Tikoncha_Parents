@file:OptIn(ExperimentalMaterial3Api::class)

package uz.tikoncha_parent.presentation.base.multi_phone_input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.mp_cd_select_country
import tikoncha_parents.composeapp.generated.resources.telefon_nomer
import uz.tikoncha_parent.ui.TextFieldCornerRadius
import uz.tikoncha_parent.ui.TextFieldHeight
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

/**
 * Multi-country telefon raqami maydoni (flag + dial code + mask + tanlash bottom sheet).
 *
 * API eski PhoneNumberInputField bilan mos: [phoneNumber] faqat raqamlardan iborat,
 * [onPhoneNumberChange] ham faqat tozalangan raqamlarni qaytaradi.
 *
 * Tanlangan davlat ichkarida boshqariladi (default — O'zbekiston). Dial code'ni
 * tashqarida ishlatish kerak bo'lsa, [onCountryChange] orqali oling.
 * Matnlar uz/ru/en — string resource orqali.
 */
@Composable
fun CountryPhoneInputField(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    enabled: Boolean = true,
    initialCountry: Country = DefaultCountry,
    onCountryChange: (Country) -> Unit = {},
) {
    var country by remember { mutableStateOf(initialCountry) }
    var sheetOpen by remember { mutableStateOf(false) }

    val placeholder = stringResource(Res.string.telefon_nomer)
    val selectCountryCd = stringResource(Res.string.mp_cd_select_country)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = TextFieldHeight)
            .clip(RoundedCornerShape(TextFieldCornerRadius)),
    ) {
        // --- Country selector (flag + dial + chevron) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(TextFieldCornerRadius))
                .clickable(enabled = enabled) { sheetOpen = true }
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        ) {
            Text(text = country.flag, fontSize = 22.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = country.dial,
                style = AppTypography.titleSmMedium,
                color = AppColors.text.tertiary,
            )
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = selectCountryCd,
                tint = AppColors.icon.secondary,
                modifier = Modifier.size(18.dp),
            )
        }

        // --- Ajratuvchi chiziq ---
        Box(
            Modifier
                .padding(vertical = 12.dp)
                .width(1.dp)
                .height(24.dp)
                .background(AppColors.border.secondary)
        )

        // --- Raqam maydoni ---
        BasicTextField(
            value = phoneNumber,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(country.maxDigits)
                onPhoneNumberChange(digits)
            },
            enabled = enabled,
            singleLine = true,
            interactionSource = remember { MutableInteractionSource() },
            textStyle = AppTypography.titleSmMedium.copy(color = AppColors.text.primary),
            cursorBrush = SolidColor(AppColors.border.accentEmphasis),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            visualTransformation = PhoneMaskVisualTransformation(country.mask),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
            decorationBox = { inner ->
                if (phoneNumber.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.placeholder,
                    )
                }
                inner()
            },
        )
    }

    if (sheetOpen) {
        CountryPickerBottomSheet(
            selected = country,
            onSelect = { picked ->
                if (picked.iso != country.iso) {
                    country = picked
                    onCountryChange(picked)
                    // Mask uzunligi farq qilishi mumkin — raqamni tozalaymiz.
                    // Tozalashni xohlamasangiz, shu qatorni olib tashlang.
                    onPhoneNumberChange("")
                }
                sheetOpen = false
            },
            onDismiss = { sheetOpen = false },
        )
    }
}

@Preview
@Composable
private fun CountryPhoneInputFieldPreview() {
    TikonchaParentTheme(ThemeMode.DARK) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            CountryPhoneInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        AppColors.field.page,
                        RoundedCornerShape(TextFieldCornerRadius),
                    ),
                phoneNumber = "901234567",
                onPhoneNumberChange = {},
            )
        }
    }
}