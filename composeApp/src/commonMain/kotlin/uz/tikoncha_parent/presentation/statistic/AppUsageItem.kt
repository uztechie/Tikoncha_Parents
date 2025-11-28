package uz.tikoncha_parent.presentation.statistic

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun AppUsageItem(
    appUsageUi: AppUsageUi,
    onLockClick: (appUsageUi: AppUsageUi) -> Unit
) {


    val loader: AppIconLoader = koinInject()
    val icon = remember(appUsageUi.packageName) { loader.load(appUsageUi.packageName) }


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

//            if (bitMapIcon == null) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_launcher_foreground),
//                    contentDescription = "",
//                    tint = PrimaryColor
//                )
//            } else {
//                Image(
//                    painter = BitmapPainter(bitMapIcon.asImageBitmap()),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(AppIconInnerPadding),
//                    contentScale = ContentScale.Crop
//                )
//            }

            if (icon == null){
                Icon(
                    painter = painterResource(Res.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    tint = PrimaryColor
                )
            }
            else{
                Image(
                    bitmap = icon,
                    contentDescription = "",
                )
            }


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

        FilledTonalIconButton(
            onClick = {
                onLockClick(appUsageUi)
            },
            shape = RoundedCornerShape(ButtonCornerRadius),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.extendedColor.backgroundColor),
            modifier = Modifier
                .padding(start = 10.dp)
                .size(AppIconSize)
                .border(
                    shape = RoundedCornerShape(ButtonCornerRadius),
                    width = 1.dp,
                    color = if (appUsageUi.allowed) MaterialTheme.extendedColor.primaryColor else MaterialTheme.colorScheme.error
                )
        ) {

            Icon(
                painter = if (appUsageUi.allowed) painterResource(Res.drawable.unlocked) else painterResource(
                    Res.drawable.locked
                ),
                contentDescription = "",
                tint = if (appUsageUi.allowed) PrimaryColor else MaterialTheme.colorScheme.error
            )

        }
    }

}

@Preview
@Composable
private fun PreviewAppUsageItem() {
    AppUsageItem(
        appUsageUi = AppUsageUi("", "Instagram", "", HourMinute(), false),
        onLockClick = {}
    )
}
