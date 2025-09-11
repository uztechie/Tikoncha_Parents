package uz.tikoncha_parent.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import uz.tikoncha_parent.ui.BackgroundColor
import qrgenerator.qrkitpainter.QrPainter
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun TransparentQrScreen(
    onDismissRequest: () -> Unit,
    painter: QrPainter
)
{
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.extendedColor.onBackgroundColor.copy(alpha = 0.3f))
                .clickable { onDismissRequest() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painter,
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(215.dp)
                        .background(BackgroundColor)
                        .padding(15.dp)
                    ,
                    contentScale = ContentScale.Fit
                )

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cornerLength = 30.dp.toPx()
                    val strokeWidth = 4.dp.toPx()

                    drawLine(Color.White, Offset(0f, 0f), Offset(cornerLength, 0f), strokeWidth, StrokeCap.Square)
                    drawLine(Color.White, Offset(0f, 0f), Offset(0f, cornerLength), strokeWidth, StrokeCap.Square)

                    drawLine(Color.White, Offset(size.width, 0f), Offset(size.width - cornerLength, 0f), strokeWidth, StrokeCap.Square)
                    drawLine(Color.White, Offset(size.width, 0f), Offset(size.width, cornerLength), strokeWidth, StrokeCap.Square)

                    drawLine(Color.White, Offset(0f, size.height), Offset(cornerLength, size.height), strokeWidth, StrokeCap.Square)
                    drawLine(Color.White, Offset(0f, size.height), Offset(0f, size.height - cornerLength), strokeWidth, StrokeCap.Square)

                    drawLine(Color.White, Offset(size.width, size.height), Offset(size.width - cornerLength, size.height), strokeWidth, StrokeCap.Square)
                    drawLine(Color.White, Offset(size.width, size.height), Offset(size.width, size.height - cornerLength), strokeWidth, StrokeCap.Square)
                }
            }
        }
    }
}