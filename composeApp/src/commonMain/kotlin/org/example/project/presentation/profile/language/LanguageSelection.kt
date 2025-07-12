package org.example.project.presentation.profile.language

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.presentation.domain.model.LanguageType
import org.example.project.ui.BackgroundColor
import org.example.project.ui.DividerHorizontal
import org.example.project.ui.HintTextColor
import org.example.project.ui.LanguageTonalIconColor
import org.example.project.ui.NormalIconButtonPadding
import org.example.project.ui.NormalTextSize
import org.example.project.ui.SmallTextSize
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceSmall
import org.example.project.ui.TextColor
import org.jetbrains.compose.resources.painterResource

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
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundColor)
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
                        .background(LanguageTonalIconColor, RoundedCornerShape(NormalIconButtonPadding)),
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

                    Text(
                        text = language.languageName,
                        fontSize = NormalTextSize,
                        fontWeight = FontWeight.W500,
                        style = TextStyle()
                    )

                    SpaceSmall()

                    Text(
                        text = languageExpanded[language.ordinal],
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500,
                        color = HintTextColor,
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
                            color = if (selectedLanguage == language) TextColor else TextColor,
                            shape = CircleShape
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedLanguage == language) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(TextColor, CircleShape)
                        )
                    }
                }
            }
            DividerHorizontal()
        }
    }
}