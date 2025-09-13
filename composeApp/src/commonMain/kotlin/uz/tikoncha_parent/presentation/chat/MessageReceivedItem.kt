package uz.saidburxon.newedu.presentation.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.ui.ChatMessageCornerRadius
import uz.tikoncha_parent.ui.ChatTextSize
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun MessageReceivedItem(
    modifier: Modifier = Modifier,
    chatMessageUi: ChatMessageUi,
    showSender: Boolean = false
) {

    val density = LocalDensity.current
    var timeWidthPx by remember { mutableIntStateOf(0) }
    var parentWidthPx by remember { mutableIntStateOf(0) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { parentWidthPx = it.width } // px
    ) {
        val maxBubbleWidth: Dp = with(density) { (parentWidthPx * 0.78f).toDp() }
        Box(
            modifier = modifier
                .widthIn(max = maxBubbleWidth)
                .clip(
                    RoundedCornerShape(
                        topStart = ChatMessageCornerRadius,
                        topEnd = ChatMessageCornerRadius,
                        bottomEnd = ChatMessageCornerRadius
                    )
                )
                .background(MaterialTheme.extendedColor.cardColor)
                .padding(horizontal = 15.dp, vertical = 8.dp),
        )
        {

            Column {
                if (showSender){
                    CustomText(
                        text = chatMessageUi.senderName,
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500
                    )
                }

                Box{
                    val timePadding = with(density) { (timeWidthPx.toDp() + 15.dp) }


                    CustomText(
                        text = chatMessageUi.message,
                        fontSize = ChatTextSize,
                        // Matn soat bo‘shlig‘ini “ehtiyotlab” yoziladi
                        modifier = Modifier
                            .padding(end = timePadding),
                        lineHeight = ChatTextSize
                    )

                    // Soat past-o‘ngda overlay bo‘lib turadi
                    CustomText(
                        text = chatMessageUi.time,
                        color = MaterialTheme.extendedColor.hintColor,
                        maxLines = 1,
                        fontSize = UltraSmallTextSize,
                        lineHeight = UltraSmallTextSize,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding()
                            .onGloballyPositioned { timeWidthPx = it.size.width } // soat kengligini olamiz
                    )
                }
            }




        }

    }



}

@Preview
@Composable
private fun Pre() {
    MessageReceivedItem(
        chatMessageUi = ChatMessageUi(
            id = "",
            isMine = true,
            message = "Ustoz",
            createdAt = 0,
            time = "10:25",
            isRead = true,
            senderName = "Ustoz",
            senderAvatar = ""

        )
    )
}