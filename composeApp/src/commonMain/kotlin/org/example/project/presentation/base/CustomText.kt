package uz.saidburxon.newedu.presentation.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import org.example.project.presentation.base.theme.TextColor

import org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
fun CustomText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = TextColor,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize:TextUnit = TextUnit.Unspecified,
    softWrap: Boolean = true,
    lineHeight:TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle = FontStyle.Normal

) {
    Text(
       text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        minLines = minLines,
        fontWeight = fontWeight,
        fontSize = fontSize,
        softWrap = softWrap,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        fontFamily = FontFamily(),
        fontStyle = fontStyle
    )
}


@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = TextColor,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize:TextUnit = TextUnit.Unspecified,
    softWrap: Boolean = true,
    lineHeight:TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle = FontStyle.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        textAlign = textAlign,
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        maxLines = maxLines,
        minLines = minLines,
        fontWeight = fontWeight,
        fontSize = fontSize,
        softWrap = softWrap,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        fontFamily = FontFamily(),
        fontStyle = fontStyle
    )
}



@Preview
@Composable
private fun Pre() {

}