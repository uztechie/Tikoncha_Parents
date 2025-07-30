package org.example.project.presentation.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ic_launcher_foreground
import tikoncha_parents.composeapp.generated.resources.time_icon
import uz.saidburxon.newedu.presentation.base.CustomText

@Composable
fun AppUsageItem(
    appUsage: AppUsage
) {

//    var bitMapIcon: Bitmap? = null
//
//    appUsage.icon?.let { drawable ->
//        bitMapIcon = remember(drawable) {
//            drawable.toBitmap()
//        }
//    }


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
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {

            CustomText(
                text = appUsage.name,
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
                    text = appUsage.usageTime,
                    color = MaterialTheme.colorScheme.secondary,
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
    AppUsageItem(
        appUsage = AppUsage("", "Instagram", "", "2 soat 45 minut")
    )
}
