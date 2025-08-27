package org.example.project.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import tikoncha_parents.composeapp.generated.resources.lock
import tikoncha_parents.composeapp.generated.resources.locked
import tikoncha_parents.composeapp.generated.resources.time_icon
import tikoncha_parents.composeapp.generated.resources.unlocked
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun AppUsageItem(
    appUsageUi: AppUsageUi,
    onClickLock: (appUsageUi: AppUsageUi) -> Unit
) {

//    var bitMapIcon: Bitmap? = null
//
//    appUsage.icon?.let { drawable ->
//        bitMapIcon = remember(drawable) {
//            drawable.toBitmap()
//        }
//    }

    var allowed by remember {
        mutableStateOf(appUsageUi.allowed)
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
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.scrim),
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

            Icon(
                painter = painterResource(Res.drawable.ic_launcher_foreground),
                contentDescription = "",
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

                CustomText(
                    text = appUsageUi.usageTime,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = SmallTextSize,
                    modifier = Modifier
                        .padding(start = 2.dp)
                )
            }
        }

        FilledTonalIconButton(
            onClick = {
                allowed = !allowed
                onClickLock(appUsageUi.copy(allowed = allowed))
            },
            shape = RoundedCornerShape(ButtonCornerRadius),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.background),
            modifier = Modifier
                .padding(start = 10.dp)
                .size(AppIconSize)
                .border(
                    shape = RoundedCornerShape(ButtonCornerRadius),
                    width = 1.dp,
                    color = if (allowed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.error
                )
        ) {

            Icon(
                painter = if (allowed) painterResource(Res.drawable.unlocked) else painterResource(
                    Res.drawable.locked
                ),
                contentDescription = "",
                tint = if (allowed) PrimaryColor else MaterialTheme.colorScheme.error
            )

        }
    }

}

@Preview
@Composable
private fun PreviewAppUsageItem() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(ContainerPadding)
    ) {
        AppUsageItem(
            appUsageUi = AppUsageUi(
                "",
                "Instagram",
                "",
                "2 soat 45 minut",
                allowed = true,
            ),
            onClickLock = {}
        )
    }
}
