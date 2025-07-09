package org.example.project.presentation.base.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ButtonHeight = 50.dp
val DialogButtonHeight = 40.dp

val LinearProgressIndicatorHeight = 10.dp

val LargeIconButtonSize = 45.dp
val NormalIconButtonSize = 35.dp
val SmallIconButtonSize = 25.dp
val LargeIconButtonPadding = 12.dp
val NormalIconButtonPadding = 8.dp
val SmallIconButtonPadding = 5.dp

val ButtonCornerRadius = 15.dp
val ButtonDialogCornerRadius = 12.dp

val ChatMessageCornerRadius = 20.dp
val CardCornerRadius = 25.dp
val LargeCardCornerRadius = 32.dp
val CardCornerPadding = 20.dp
val ContainerCornerRadius = 15.dp
val PaddingCornerRadius = 15.dp
val MainCornerRadius = 16.dp
val ShapeCornerRadius = 10.dp
val ItemElevation = 2.dp

val SliderCornerRadius = 20.dp

val TextFieldCornerRadius = 12.dp
val TextFieldHeight = 45.dp
val TextFieldHeightDialogOrButtonSheet = 40.dp
val TextFieldInnerPadding = 15.dp
val TextFieldIconSize = 20.dp

val NormalTextSize = 16.sp
val NormalTextLineHeight = 18.sp
val LargeTextSize = 26.sp
val SmallTextSize = 14.sp
val UltraSmallTextSize = 12.sp

val ContainerPadding = 15.dp
val HeaderHeight = 60.dp

val TextFieldTextStyle: TextStyle @Composable get() = MaterialTheme.typography.titleMedium


val CloseButtonSize: Dp = 30.dp
val CloseButtonInnerPadding: Dp = 5.dp

val ItemHeight: Dp = 50.dp

val LargeImageHeight: Dp = 170.dp

val AppIconSize: Dp = 45.dp
val AppIconInnerPadding: Dp = 10.dp
val AppItemHeight: Dp = 60.dp
val SmallIconSize: Dp = 18.dp
val NormalIconSize: Dp = 22.dp

val ProfileImageSize: Dp = 100.dp

val ProfileStatsContainerHeight: Dp = 95.dp

val ParentsInformationContainerHeight: Dp = 260.dp

@Composable
fun SpaceLarge() {
    Spacer(Modifier.size(20.dp))
}
@Composable
fun SpaceMedium() {
    Spacer(Modifier.size(15.dp))
}
@Composable
fun SpaceSmall() {
    Spacer(Modifier.size(10.dp))
}


@Composable
fun SpaceUltraSmall() {
    Spacer(Modifier.size(5.dp))
}

@Composable
fun DividerHorizontal() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
        color = TextColor.copy(alpha = 0.1f)
    )
}



