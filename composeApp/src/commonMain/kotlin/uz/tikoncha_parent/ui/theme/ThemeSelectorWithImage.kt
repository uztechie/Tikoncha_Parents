package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.ui.NormalTextSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.Space

@Composable
fun ThemeSelectorWithImage(
    modifier: Modifier = Modifier,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    selected: Boolean = false,
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onThemeSelected(selectedTheme) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.extendedColor.buttonColor
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Space(12.dp)
            // Maket (preview) — chetga tegmasligi uchun padding ichida
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                when (selectedTheme) {
                    ThemeMode.LIGHT -> Image(
                        painter = painterResource(Res.drawable.theme_light),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    ThemeMode.DARK -> Image(
                        painter = painterResource(Res.drawable.theme_dark),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    ThemeMode.SYSTEM -> SystemPreview(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Radio + yozuv — karta ICHIDA
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected,
                    onClick = { onThemeSelected(selectedTheme) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.extendedColor.textColor
                    )
                )
                CustomText(
                    text = stringResource(selectedTheme.resId),
                    fontSize = NormalTextSize
                )
            }
        }
    }
}

@Composable
private fun SystemPreview(modifier: Modifier = Modifier) {
    val lightShape = remember {
        GenericShape { size, _ ->
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(0f, size.height)
            close()
        }
    }
    val darkShape = remember {
        GenericShape { size, _ ->
            moveTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
    }

    Box(modifier = modifier) {
        Image(
            painter = painterResource(Res.drawable.theme_light),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .clip(lightShape)
        )
        Image(
            painter = painterResource(Res.drawable.theme_dark),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .clip(darkShape)
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawLine(
                color = Color(0xFF9E9E9E),
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 2.dp.toPx()
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
        ThemeMode.entries.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { mode ->
                    ThemeSelectorWithImage(
                        modifier = Modifier.weight(1f),
                        selectedTheme = mode,
                        onThemeSelected = onSelect,
                        selected = selected == mode
                    )
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview
@Composable
private fun Pre() {
    TikonchaParentTheme {
        ThemeSelectionGrid(
            selected = ThemeMode.SYSTEM,
            onSelect = {}
        )
    }
}