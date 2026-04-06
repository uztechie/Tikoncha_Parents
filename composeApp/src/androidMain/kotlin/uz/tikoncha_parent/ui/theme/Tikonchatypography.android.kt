package uz.tikoncha_parent.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import uz.tikoncha_parent.R

actual val TikonchaFontFamily: FontFamily = FontFamily(
    Font(R.font.golostext_regular, FontWeight.Normal),
    Font(R.font.golostext_medium, FontWeight.Medium),
    Font(R.font.golostext_semibold, FontWeight.SemiBold),
    Font(R.font.golostext_bold, FontWeight.Bold),
    Font(R.font.golostext_extrabold, FontWeight.ExtraBold),
    Font(R.font.golostext_black, FontWeight.Black),
)
