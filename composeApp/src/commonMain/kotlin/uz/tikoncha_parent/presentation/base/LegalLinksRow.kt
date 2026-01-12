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
import uz.tikoncha_parent.platform.isIos

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
        CustomText(
            text = "Privacy Policy",
            fontSize = SmallTextSize,
            fontWeight = FontWeight.W600,
            color = MaterialTheme.extendedColor.primaryColor,
            textDecoration = TextDecoration.Underline, // ✅ underline
            modifier = Modifier.clickable { uriHandler.openUri(privacyUrl) }
        )

        if (isIos()){
            CustomText(
                text = "  |  ",
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W500,
                color = MaterialTheme.extendedColor.hintColor
            )

            // Terms link
            CustomText(
                text = "Terms of Use",
                fontSize = SmallTextSize,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.extendedColor.primaryColor,
                textDecoration = TextDecoration.Underline, // ✅ underline
                modifier = Modifier.clickable { uriHandler.openUri(termsUrl) }
            )
        }


    }
}