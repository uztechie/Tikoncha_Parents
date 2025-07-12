package org.example.project.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.presentation.common.CustomButton
import org.example.project.presentation.common.CoinGeneratorTextField
import org.example.project.ui.BackgroundColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceMedium
import org.example.project.ui.TextFieldHeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrgenerator.qrkitpainter.rememberQrKitPainter

class QrCodeScreen: Screen {
    @Composable
    override fun Content() {
        QrCodeUI()
    }
}

@Composable
fun QrCodeUI(

){

    var textForQrCode by remember {
        mutableStateOf("")
    }

    val painter = rememberQrKitPainter(data = textForQrCode)

    var isShowQrCode by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(ContainerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CoinGeneratorTextField(
                modifier = Modifier
                    .weight(1f)
                    .height(TextFieldHeight),
                hasBorder = true,
                label = "Enter text",
                value = TextFieldValue(textForQrCode),
                onValueChange = {
                    textForQrCode = it.text
                },
                fonSize = NormalTextSize,
                contentColor = PrimaryColor,
                isCenteredText = true
            )

            SpaceMedium()

            CustomButton(
                modifier = Modifier
                    .height(ButtonHeight),
                onClick = {
                    if (textForQrCode.isNotEmpty() && textForQrCode.length > 6){
                        isShowQrCode = true
                    }else{
                        isShowQrCode = false
                    }
                },
                text = "Generate QR Code",
                fontSize = NormalTextSize
            )

        }

        SpaceLarge()

        if (isShowQrCode){
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .size(150.dp)
            )
        }

    }
}

@Preview
@Composable
fun PreviewQrCodeScreen(){
    QrCodeUI()
}