package uz.tikoncha_parent.presentation.base.multi_phone_input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.mp_cd_select_country
import tikoncha_parents.composeapp.generated.resources.mp_phone_label
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

/* ============================================================
 *  MASK VISUAL TRANSFORMATION
 *  State'da faqat raqamlar saqlanadi ("901234567"),
 *  ekranda mask bo'yicha ko'rsatiladi ("90 123 45 67").
 * ============================================================ */

class PhoneMaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        for (ch in text.text) {
            while (maskIndex < mask.length && specialIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            if (maskIndex >= mask.length) break
            out += ch
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), Mapping(mask))
    }

    private class Mapping(private val mask: String) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 0) return 0
            var seen = 0
            mask.forEachIndexed { i, c ->
                if (c == '#') {
                    seen++
                    if (seen == offset) return i + 1
                }
            }
            return mask.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            val clamped = offset.coerceIn(0, mask.length)
            return mask.take(clamped).count { it == '#' }
        }
    }
}

/* ============================================================
 *  PHONE INPUT FIELD (label + xato + helper bilan)
 * ============================================================ */

/**
 * Multi-country telefon input. Davlat tashqaridan hoist qilinadi (onCountryClick bilan picker ochiladi).
 * Matnlar uz/ru/en — string resource orqali.
 */
@Composable
fun TikonchaPhoneInput(
    value: String,
    onValueChange: (String) -> Unit,
    country: Country,
    onCountryClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = stringResource(Res.string.mp_phone_label),
    isError: Boolean = false,
    errorText: String? = null,
    helperText: String? = null,
    enabled: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()

    val borderColor = when {
        isError -> AppColors.text.accentDanger
        focused -> AppColors.border.accentEmphasis
        else -> AppColors.border.primary
    }
    val borderWidth = if (focused || isError) 2.dp else 1.dp

    Column(modifier = modifier.fillMaxWidth()) {

        if (label != null) {
            Text(
                text = label,
                style = AppTypography.emphasizedSmMedium,
                color = AppColors.text.label,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(borderWidth, borderColor, RoundedCornerShape(12.dp)),
        ) {
            // --- Country selector ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable(enabled = enabled, onClick = onCountryClick)
                    .padding(start = 12.dp, end = 10.dp),
            ) {
                Text(text = country.flag, style = AppTypography.titleLgRegular)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = country.dial,
                    style = AppTypography.titleMdSemiBold,
                    color = AppColors.text.primary,
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(Res.string.mp_cd_select_country),
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
                value = value,
                onValueChange = { raw ->
                    val digits = raw.filter(Char::isDigit).take(country.maxDigits)
                    onValueChange(digits)
                },
                enabled = enabled,
                singleLine = true,
                interactionSource = interaction,
                textStyle = AppTypography.titleMdMedium.copy(color = AppColors.text.primary),
                cursorBrush = SolidColor(AppColors.border.accentEmphasis),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = PhoneMaskVisualTransformation(country.mask),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            text = country.mask.replace('#', '0'),
                            style = AppTypography.titleMdRegular,
                            color = AppColors.text.placeholder,
                        )
                    }
                    inner()
                },
            )
        }

        val bottom = if (isError) errorText else helperText
        if (bottom != null) {
            Text(
                text = bottom,
                style = AppTypography.emphasizedXsMedium,
                color = if (isError) AppColors.text.accentDanger else AppColors.text.tertiary,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

/** Telefonni to'liq holatda qaytaradi: "+998 90 123 45 67". */
fun formatFullPhone(country: Country, digits: String): String {
    val masked = PhoneMaskVisualTransformation(country.mask)
        .filter(AnnotatedString(digits)).text.text
    return "${country.dial} $masked".trim()
}

/** E.164 format: "+998901234567". Backend uchun qulay. */
fun toE164(country: Country, digits: String): String = "${country.dial}$digits"