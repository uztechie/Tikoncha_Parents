package uz.tikoncha_parent.presentation.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.contenue
import tikoncha_parents.composeapp.generated.resources.crown
import tikoncha_parents.composeapp.generated.resources.endi_siz_ushbu_obunaga_egasiz
import tikoncha_parents.composeapp.generated.resources.maxsus_imkoniyatlar_va_qollab_quvvatlashdan_foydalaning
import tikoncha_parents.composeapp.generated.resources.tabriklaymiz
import tikoncha_parents.composeapp.generated.resources.tikoncha_plus_dialog
import uz.saidburxon.newedu.presentation.base.CustomText

import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ColorWhite
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.UltraLargeTextSize
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun CustomPaymentDialog(
    modifier: Modifier = Modifier,
    show: Boolean = true,
    painter: Painter = painterResource(Res.drawable.crown),
    painterPayment: Painter = painterResource(Res.drawable.tikoncha_plus_dialog),
    painterContinue: Painter = painterResource(Res.drawable.contenue),
    brushStartColor: Color = Color(0xFFD0EBC3),
    brushEndColor: Color = Color(0xFF5AAC5B),
    titleColor: Color = Color(0xFF286435),
    title: String = stringResource(Res.string.tabriklaymiz),
    subtitle: String = stringResource(Res.string.endi_siz_ushbu_obunaga_egasiz),
    message: String = stringResource(Res.string.maxsus_imkoniyatlar_va_qollab_quvvatlashdan_foydalaning),
    onDismiss: () -> Unit,
    onButtonClick: () -> Unit
) {

    if (!show) {
        return
    }

    val density = LocalDensity.current

    Dialog(
        onDismissRequest = onDismiss
    ) {

        var height by remember {
            mutableStateOf(0.dp)
        }

        Box{
            Card(
                modifier = Modifier
                    .coverShadow(
                        shape = RoundedCornerShape(CardCornerRadius)
                    )
                    .onGloballyPositioned{
                        val px = it.size.height
                        height = with(density) { px.toDp() }
                    },
                shape = RoundedCornerShape(CardCornerRadius)
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    brushStartColor,
                                    brushEndColor,
                                )
                            )
                        )
                        .border(
                            1.dp,
                            MaterialTheme.extendedColor.primaryColor,
                            RoundedCornerShape(CardCornerRadius)
                        )
                        .padding(ContainerPadding)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(0.25f), contentScale = ContentScale.FillWidth
                    )
                    SpaceSmall()
                    CustomText(
                        text = title,
                        fontSize = UltraLargeTextSize,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = titleColor
                    )
                    CustomText(
                        text = subtitle,
                        fontSize = NormalTextSize,
                        textAlign = TextAlign.Center,
                        color = ColorWhite
                    )
                    SpaceMedium()
                    Image(
                        painter = painterPayment,
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(0.7f), contentScale = ContentScale.FillWidth
                    )
                    SpaceMedium()
                    CustomText(
                        text = message,
                        fontSize = UltraSmallTextSize,
                        textAlign = TextAlign.Center,
                        color = ColorWhite
                    )
                    SpaceLarge()
                    Image(
                        painter = painterContinue,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = null,
                            ) {
                                onButtonClick()
                            }
                            .fillMaxWidth(0.8f),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }

//            LottiePlayer(
//                filePath = "json/confetti.json",
//                modifier = Modifier
//                    .background(Color.Red)
//                    .fillMaxWidth()
//                    .height(height)
//            )
        }


    }
}

@Preview
@Composable
private fun CustomConfirmDialogPre() {
    CustomPaymentDialog(
        onDismiss = { },
        onButtonClick = {},
        show = true
    )
}