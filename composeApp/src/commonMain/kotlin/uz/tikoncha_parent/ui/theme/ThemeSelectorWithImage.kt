package uz.tikoncha_parent.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import uz.tikoncha_parent.ui.NormalTextSize
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun ThemeSelectorWithImage(
    modifier: Modifier = Modifier,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    selected: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    )
    {
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
        ){
            Image(
                painter = if (selectedTheme == ThemeMode.LIGHT) painterResource(Res.drawable.theme_light) else painterResource(
                    Res.drawable.theme_dark
                ),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
            )
        }


        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = {
                    onThemeSelected(selectedTheme)
                },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.onBackground)
            )

            CustomText(
                text = stringResource(selectedTheme.resId),
                fontSize = NormalTextSize
            )
        }
    }
}