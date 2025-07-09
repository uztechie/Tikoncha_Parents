package uz.saidburxon.newedu.presentation.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.project.presentation.base.theme.ChatMessageBackgroundColor
import org.example.project.presentation.base.theme.ChatMessageCornerRadius
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SpaceUltraSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.UltraSmallTextSize
import org.example.project.ui.ChatTextSize
import org.jetbrains.compose.ui.tooling.preview.Preview
import utils.getCurrentTimeMillis
import uz.saidburxon.newedu.domain.model.ChatMessage
import uz.saidburxon.newedu.presentation.base.CustomText


@Composable
fun MessageSentItem(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessage,
) {

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopEnd
    ){
        Column(
            modifier = modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(
                    topStart = ChatMessageCornerRadius,
                    topEnd = ChatMessageCornerRadius,
                    bottomStart = ChatMessageCornerRadius)
                )
                .background(ChatMessageBackgroundColor)
                .padding(10.dp)

        ) {
            CustomText(
                text = chatMessage.message,
                color = TextColor,
                fontSize = ChatTextSize,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            )
            SpaceUltraSmall()
            CustomText(
                text = chatMessage.time,
                color = PrimaryColor,
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
    MessageSentItem(
        chatMessage = ChatMessage(
            id = 1L,
            isMine = true,
            message = "Ustoz bugun birinchi darsga",
            createdAt = getCurrentTimeMillis(),
            time = "10:25"
        )
    )
}