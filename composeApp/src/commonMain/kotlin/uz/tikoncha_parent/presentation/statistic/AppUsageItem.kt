package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.domain.model.HourMinute
import uz.tikoncha_parent.platform.AppIconLoader
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.daqiqa
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import tikoncha_parents.composeapp.generated.resources.soat
import tikoncha_parents.composeapp.generated.resources.locked
import tikoncha_parents.composeapp.generated.resources.time_icon
import tikoncha_parents.composeapp.generated.resources.unlocked
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun AppUsageItem(
    appUsageUi: AppUsageUi,
) {

    val loader: AppIconLoader = koinInject()
    val iconKey = remember(appUsageUi.packageName, appUsageUi.icon) {
        appUsageUi.icon.takeIf { it.isNotBlank() } ?: appUsageUi.packageName
    }

    var iconBitmap by remember(iconKey) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(iconKey) {
        iconBitmap = loader.load(iconKey) // ideal: suspend + cache
    }

    Row(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        FilledTonalIconButton(
            onClick = {},
            shape = RoundedCornerShape(ButtonCornerRadius),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.extendedColor.buttonColor),
            modifier = Modifier
                .size(AppIconSize)
        ) {
            iconBitmap?.let { bmp ->
                Image(
                    bitmap = bmp,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(AppIconInnerPadding)
                )
            } ?: Icon(
                painter = painterResource(Res.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = PrimaryColor
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {

            CustomText(
                text = appUsageUi.name,
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(2.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(Res.drawable.time_icon),
                    modifier = Modifier
                        .size(SmallIconSize),
                    contentDescription = "",
                    tint = PrimaryColor
                )

                val soat = stringResource(Res.string.soat)
                val daqiqa = stringResource(Res.string.daqiqa)
                val hour = appUsageUi.usageTime.hour
                val minute = appUsageUi.usageTime.minute

                val time = StringBuilder()
                if (hour > 0){
                    time.append(hour.toString())
                    time.append(" ")
                    time.append(soat)
                    time.append(" ")
                }
                time.append(minute.toString())
                time.append(" ")
                time.append(daqiqa)

                CustomText(
                    text = time.toString(),
                    color = MaterialTheme.extendedColor.hintColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .padding(start = 2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAppUsageItem() {
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        AppUsageItem(
            appUsageUi = AppUsageUi("", "Instagram", "", HourMinute(), false),
        )
    }
}
