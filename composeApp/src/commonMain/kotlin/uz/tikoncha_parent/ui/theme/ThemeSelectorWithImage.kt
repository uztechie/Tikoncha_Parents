package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.NormalTextSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.ContainerPadding

@Composable
fun ThemeSelectorWithImage(
    modifier: Modifier = Modifier,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    selected: Boolean = false,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f)
        ) {
            Card(
                modifier = Modifier
                    .clickable {
                        onThemeSelected(selectedTheme)
                    }
                    .clip(RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.extendedColor.buttonColor)
            ) {

                Column(
                    modifier = Modifier.padding(horizontal = ContainerPadding)
                ) {

                    Spacer(Modifier.size(16.dp))
                    Image(
                        painter = if (selectedTheme == ThemeMode.LIGHT) painterResource(Res.drawable.theme_light)
                        else painterResource(
                            Res.drawable.theme_dark
                        ),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }


        Row(
            modifier = Modifier.clickable { onThemeSelected(selectedTheme) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = {
                    onThemeSelected(selectedTheme)
                },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.extendedColor.textColor)
            )

            CustomText(
                text = stringResource(selectedTheme.resId),
                fontSize = NormalTextSize
            )
        }
    }
}


@Composable
fun ThemeSelectionGrid(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ThemeSelectorWithImage(
                modifier = Modifier.weight(1f),
                selectedTheme = ThemeMode.LIGHT,
                onThemeSelected = onSelect,
                selected = selected == ThemeMode.LIGHT
            )
            ThemeSelectorWithImage(
                modifier = Modifier.weight(1f),
                selectedTheme = ThemeMode.DARK,
                onThemeSelected = onSelect,
                selected = selected == ThemeMode.DARK
            )
        }
    }
}


@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme {
        ThemeSelectionGrid(
            selected = ThemeMode.DARK,
            onSelect = {}
        )
    }
}