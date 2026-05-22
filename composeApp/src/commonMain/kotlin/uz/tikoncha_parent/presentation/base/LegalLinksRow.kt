package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.foydalanish_shartlari
import tikoncha_parents.composeapp.generated.resources.maxfiylik_siyosati
import tikoncha_parents.composeapp.generated.resources.ok
import uz.tikoncha_parent.platform.isIos
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography

@Composable
fun LegalLinksRow(
    privacyUrl: String = "https://tikoncha.uz/parent-privacy-policy/",
    termsUrl: String = "https://www.apple.com/legal/internet-services/itunes/dev/stdeula/",
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Privacy link
        Text(
            text = stringResource(Res.string.maxfiylik_siyosati),
            textDecoration = TextDecoration.Underline, // ✅ underline
            modifier = Modifier.clickable { uriHandler.openUri(privacyUrl) },
            style = AppTypography.titleSmMedium,
            color = AppColors.text.accentEmphasis
        )

        if (isIos()){
            Text(
                text = "  |  ",
                style = AppTypography.titleSmMedium,
                color = AppColors.text.accentEmphasis
            )

            // Terms link
            Text(
                text = stringResource(Res.string.foydalanish_shartlari),
                textDecoration = TextDecoration.Underline, // ✅ underline
                modifier = Modifier.clickable { uriHandler.openUri(termsUrl) },
                style = AppTypography.titleSmMedium,
                color = AppColors.text.accentEmphasis
            )

        }


    }
}