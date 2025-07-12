package org.example.project.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.inter_tight_bold
import tikoncha_parents.composeapp.generated.resources.inter_tight_italic
import tikoncha_parents.composeapp.generated.resources.inter_tight_regular
import tikoncha_parents.composeapp.generated.resources.inter_tight_semibold
import kotlin.math.floor
import kotlin.math.max

val ButtonHeight = 50.dp
val DialogButtonHeight = 40.dp

val LinearProgressIndicatorHeight = 10.dp

val LargeIconButtonSize = 45.dp
val NormalIconButtonSize = 35.dp
val SmallIconButtonSize = 30.dp
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

val CoinTextFieldWidth = 90.dp

val TextFieldInnerPadding = 15.dp
val TextFieldIconSize = 20.dp

val ContainerPadding = 15.dp
val HeaderHeight = 60.dp

val TextFieldTextStyle: TextStyle @Composable get() = MaterialTheme.typography.titleMedium

val ChatTextSize:TextUnit @Composable get() =  14.sp //.responsiveSp()
val UltraLargeTextSize:TextUnit @Composable get() =  20.sp //.responsiveSp()
val SmallTextSize:TextUnit @Composable get() =  12.sp //.responsiveSp()
val NormalTextSize:TextUnit @Composable get() =  14.sp //.responsiveSp()
val NormalTextSizeSp = 16.sp
val LargeTextSize:TextUnit @Composable get() =  18.sp //.responsiveSp()
val UltraSmallTextSize:TextUnit @Composable get() =  10.sp //.responsiveSp()
val NormalLargeTextSize:TextUnit @Composable get() =  16.sp //.responsiveSp()

val CloseButtonSize: Dp = 30.dp
val CloseButtonInnerPadding: Dp = 5.dp

val ItemHeight: Dp = 50.dp

val LargeImageHeight: Dp = 170.dp

val AppIconSize: Dp = 45.dp
val AppIconInnerPadding: Dp = 10.dp
val AppItemHeight: Dp = 60.dp
val SmallIconSize: Dp = 18.dp
val NormalIconSize: Dp = 22.dp

val ProfileImageSize: Dp = 90.dp

val ProfileStatsContainerHeight: Dp = 85.dp

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

@Composable
fun MyFontFamily() = FontFamily(
    Font(Res.font.inter_tight_regular, FontWeight.Normal),
    Font(Res.font.inter_tight_semibold, FontWeight.SemiBold),
    Font(Res.font.inter_tight_italic, FontWeight.Normal, FontStyle.Italic),
    Font(Res.font.inter_tight_bold, FontWeight.Bold),

//    Font(R.font.playwritehu_regular, FontWeight.Normal),
//    Font(R.font.playwritehu_regular, FontWeight.SemiBold),
//    Font(R.font.playwritehu_regular, FontWeight.Normal, FontStyle.Italic),
//    Font(R.font.playwritehu_regular, FontWeight.Bold),
)