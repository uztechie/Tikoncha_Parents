package uz.saidburxon.newedu.presentation.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.common.Util.currentMillis
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.domain.model.ChatMessage
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor


@Composable
fun MessageReceivedItem(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
) {

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ){
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(
                    topStart = ChatMessageCornerRadius,
                    topEnd = ChatMessageCornerRadius,
                    bottomEnd = ChatMessageCornerRadius)
                )
                .background(ChatMessageBackgroundColor)
                .padding(10.dp)

        ) {
            CustomText(
                text = chatMessage.message,
                fontSize = ChatTextSize,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            )
            SpaceUltraSmall()
            CustomText(
                text = chatMessage.time,
                color = MaterialTheme.extendedColor.primaryColor,
                fontSize = UltraSmallTextSize,
                lineHeight = UltraSmallTextSize,
                modifier = Modifier
                    .align(Alignment.End),
            )
        }
    }

}

@Preview
@Composable
private fun Pre() {
    MessageReceivedItem(
        chatMessage = ChatMessage(
            id = 1L,
            isMine = true,
            message = "Ustoz bugun birinchi darsga bora olmayman",
            createdAt = currentMillis,
            time = "10:25"
        )
    )
}