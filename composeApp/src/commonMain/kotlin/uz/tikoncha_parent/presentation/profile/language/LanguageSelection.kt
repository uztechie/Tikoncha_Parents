package uz.tikoncha_parent.presentation.profile.language

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.presentation.domain.model.LanguageType
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceSmall
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun LanguageSelection(
    selectedLanguage: LanguageType,
    onLanguageSelected: (LanguageType) -> Unit
) {

    val languageExpanded = listOf(
        "Узбекский","Ruscha"
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LanguageType.values().forEach { language ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) { onLanguageSelected(language) }
            ) {


                // Flag icon with rounded rectangle background
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.extendedColor.tonalButtonColor, RoundedCornerShape(NormalIconButtonPadding)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(language.iconId),
                        contentDescription = language.languageName,
                        modifier = Modifier.size(24.dp)
                    )
                }

                SpaceLarge()

                Column{

                    CustomText(
                        text = language.languageName,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.W500,
                        style = TextStyle()
                    )

                    SpaceSmall()

                    CustomText(
                        text = languageExpanded[language.ordinal],
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500,
                        color = MaterialTheme.colorScheme.secondary,
                        style = TextStyle()
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Radio Circle (border + inner circle if selected)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(
                            width = 2.dp,
                            color = if (selectedLanguage == language) MaterialTheme.extendedColor.onBackgroundColor else MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedLanguage == language) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(MaterialTheme.extendedColor.onBackgroundColor, CircleShape)
                        )
                    }
                }
            }
            DividerHorizontal()
        }
    }
}

@Composable
@Preview
fun Preview(){
    TikonchaParentTheme(
        ThemeMode.LIGHT
    ) {
        LanguageSelection(
            selectedLanguage = LanguageType.UZ,
            onLanguageSelected = {}
        )
    }
}