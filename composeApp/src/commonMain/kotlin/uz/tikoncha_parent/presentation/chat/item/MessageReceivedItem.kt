package uz.tikoncha_parent.presentation.chat.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.chat.model.DeliveryStatus
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.ui.ChatMessageCornerRadius
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun MessageReceivedItem(
    modifier: Modifier = Modifier,
    chatMessageUi: ChatMessageUi,
    showSender: Boolean = false,
) {
    val maxBubbleWidth = LocalWindowInfo.current.containerDpSize.width * 0.78f


    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        SelectionContainer {
            Column(
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
                    .padding(horizontal = 15.dp, vertical = 8.dp)
            ) {
                if (showSender) {
                    CustomText(
                        text = chatMessageUi.senderName,
                        color = MaterialTheme.extendedColor.primaryColor,
                        fontSize = SmallTextSize,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(Modifier.height(2.dp))
                }

                CustomText(
                    text = chatMessageUi.message,
                    color = MaterialTheme.extendedColor.textColor,
                    modifier = Modifier
                )

                CustomText(
                    modifier = Modifier
                        .align(Alignment.End),
                    text = chatMessageUi.time,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = UltraSmallTextSize,
                    lineHeight = UltraSmallTextSize
                )
            }
        }
    }


}


@Preview(
    name = "Sent message – short",
    showBackground = true,
    backgroundColor = 0xFFF2F2F2,
    widthDp = 360
)
@Composable
private fun PreviewSentMessageShortOld() {
    MessageReceivedItem(
        chatMessageUi = ChatMessageUi(
            id = "1",
            isMine = true,
            message = "Salom",
            createdAt = DateTimeUtil.nowMillis(),
            time = "10:25",
            status = DeliveryStatus.READ,
            senderName = "Me",
            senderAvatar = ""
        )
    )
}

@Preview(
    name = "Sent message – 2 lines",
    showBackground = true,
    backgroundColor = 0xFFF2F2F2,
    widthDp = 360
)
@Composable
private fun PreviewSentMessageTwoLinesOld() {
    TikonchaParentTheme(
        mode = ThemeMode.DARK
    ) {
        MessageReceivedItem(
            chatMessageUi = ChatMessageUi(
                id = "2",
                isMine = true,
                message = "Ustoz bugun birinchi darsga bir necha soatdan so‘ng darslar",
                createdAt = DateTimeUtil.nowMillis(),
                time = "10:26",
                status = DeliveryStatus.SENT,
                senderName = "Me",
                senderAvatar = ""
            )
        )
    }
}

@Preview(
    name = "Sent message – long (meta below)",
    showBackground = true,
    backgroundColor = 0xFFF2F2F2,
    widthDp = 360
)
@Composable
private fun PreviewSentMessageLongOld() {
    MessageReceivedItem(
        showSender = true,
        chatMessageUi = ChatMessageUi(
            id = "3",
            isMine = true,
            message = "Bu juda uzun xabar bo‘lib, oxirgi qatorda soat va belgi sig‘maydi va shuning uchun Telegram’dagi kabi pastga alohida qatorda chiqishi kerak",
            createdAt = DateTimeUtil.nowMillis(),
            time = "10:27",
            status = DeliveryStatus.SENDING,
            senderName = "Me",
            senderAvatar = ""
        )
    )
}

