package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.BackgroundColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.tikoncha_logo

@Composable
fun LogoText(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 5.dp, bottom = 24.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.tikoncha_logo),
            contentDescription = null,
            modifier = Modifier.weight(1f),
            colorFilter = ColorFilter.tint(BackgroundColor)
        )

        Spacer(Modifier.weight(1f))
    }
}



fun getStyledText(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append("New ")
        }
        withStyle(style = SpanStyle(color = Color.White)) {
            append("edu")
        }
    }
}

@Preview
@Composable
private fun Pre() {
    LogoText()
}