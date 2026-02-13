package uz.tikoncha_parent.presentation.chat.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.message_failed
import tikoncha_parents.composeapp.generated.resources.message_read
import tikoncha_parents.composeapp.generated.resources.message_sending
import tikoncha_parents.composeapp.generated.resources.message_sent
import uz.tikoncha_parent.common.DateTimeUtil
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.chat.model.DeliveryStatus
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.ui.ChatMessageCornerRadius
import uz.tikoncha_parent.ui.Failed
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

/**
 * ✅ Sent (right) bubble: Telegram-like time+status behavior (cached)
 */
@Composable
fun MessageSentItem(
    modifier: Modifier = Modifier,
    chatMessageUi: ChatMessageUi,
) {
    val maxBubbleWidth = LocalWindowInfo.current.containerDpSize.width * 0.78f


    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ){
        Column(
            modifier = modifier
                .widthIn(max = maxBubbleWidth)
                .clip(
                    RoundedCornerShape(
                        topStart = ChatMessageCornerRadius,
                        topEnd = ChatMessageCornerRadius,
                        bottomStart = ChatMessageCornerRadius
                    )
                )
                .background(MaterialTheme.extendedColor.cardColor)
                .padding(horizontal = 15.dp, vertical = 8.dp)
        ) {
            SelectionContainer {
                CustomText(
                    text = chatMessageUi.message,
                    color = MaterialTheme.extendedColor.textColor,
                    modifier = Modifier
                )
            }

            SentMeta(
                modifier = Modifier
                    .align(Alignment.End),
                time = chatMessageUi.time,
                status = chatMessageUi.status
            )
        }
    }


}


@Composable
private fun SentMeta(
    modifier: Modifier = Modifier,
    time: String,
    status: DeliveryStatus
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {

        val textColor = when (status) {
            DeliveryStatus.SENDING -> MaterialTheme.extendedColor.hintColor
            DeliveryStatus.SENT -> MaterialTheme.extendedColor.hintColor
            DeliveryStatus.READ -> MaterialTheme.extendedColor.hintColor
            DeliveryStatus.FAILED -> MaterialTheme.extendedColor.hintColor
        }

        val iconColor = when (status) {
            DeliveryStatus.SENDING -> MaterialTheme.extendedColor.hintColor
            DeliveryStatus.SENT -> MaterialTheme.extendedColor.primaryColor
            DeliveryStatus.READ -> MaterialTheme.extendedColor.primaryColor
            DeliveryStatus.FAILED -> Failed
        }

        val icon = when (status) {
            DeliveryStatus.SENDING -> painterResource(Res.drawable.message_sending)
            DeliveryStatus.SENT -> painterResource(Res.drawable.message_sent)
            DeliveryStatus.READ -> painterResource(Res.drawable.message_read)
            DeliveryStatus.FAILED -> painterResource(Res.drawable.message_failed)
        }

        CustomText(
            text = time,
            color = textColor,
            fontSize = UltraSmallTextSize,
            lineHeight = UltraSmallTextSize,
        )



        SpaceUltraSmall()

        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(SmallIconSize),
            tint = iconColor
        )
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
    MessageSentItem(
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
    MessageSentItem(
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

@Preview(
    name = "Sent message – long (meta below)",
    showBackground = true,
    backgroundColor = 0xFFF2F2F2,
    widthDp = 360
)
@Composable
private fun PreviewSentMessageLongOld() {
    MessageSentItem(
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

