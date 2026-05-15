package uz.tikoncha_parent.presentation.add_child


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * "119952666" -> "11 995 26 66"
 *  XX XXX XX XX  (O'zbekiston formati)
 */
object PhoneVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > 9) text.text.substring(0, 9) else text.text
        val out = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if ((i == 1 || i == 4 || i == 6) && i != trimmed.lastIndex) append(' ')
            }
        }
        return TransformedText(AnnotatedString(out), PhoneOffsetMapping)
    }

    private object PhoneOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = when {
            offset <= 1 -> offset
            offset <= 4 -> offset + 1
            offset <= 6 -> offset + 2
            else        -> offset + 3
        }

        override fun transformedToOriginal(offset: Int): Int = when {
            offset <= 2  -> offset
            offset <= 6  -> offset - 1
            offset <= 9  -> offset - 2
            else         -> offset - 3
        }
    }
}