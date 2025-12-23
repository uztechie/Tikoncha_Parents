package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.message_read
import tikoncha_parents.composeapp.generated.resources.message_sent
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.model.ChatMessageUi
import uz.tikoncha_parent.ui.ChatMessageCornerRadius
import uz.tikoncha_parent.ui.ChatTextSize
import uz.tikoncha_parent.ui.SmallIconSize
import uz.tikoncha_parent.ui.SpaceUltraSmall
import uz.tikoncha_parent.ui.UltraSmallTextSize
import uz.tikoncha_parent.ui.theme.extendedColor

@Composable
fun MessageSentItem(
    modifier: Modifier = Modifier,
    chatMessageUi: ChatMessageUi,
) {

    val density = LocalDensity.current
    var timeWidthPx by remember { mutableIntStateOf(0) }
    var parentWidthPx by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { parentWidthPx = it.width },
        contentAlignment = Alignment.TopEnd
    ){
        val maxBubbleWidth: Dp = with(density) { (parentWidthPx * 0.78f).toDp() }
        Box(
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
                .padding(horizontal = 15.dp, vertical = 8.dp),
        )
        {
            val timePadding = with(density) { (timeWidthPx.toDp() + 15.dp) }

            CustomText(
                text = chatMessageUi.message,
                fontSize = ChatTextSize,
                modifier = Modifier
                    .padding(end = timePadding),
                lineHeight = ChatTextSize
            )


            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .onGloballyPositioned { timeWidthPx = it.size.width },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            )
            {
                CustomText(
                    text = chatMessageUi.time,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontSize = UltraSmallTextSize,
                    lineHeight = UltraSmallTextSize,
                    modifier = Modifier,
                )
                val icon = if (chatMessageUi.isRead){
                    painterResource(Res.drawable.message_read)
                }
                else{
                    painterResource(Res.drawable.message_sent)
                }
                SpaceUltraSmall()
                Icon(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.size(SmallIconSize),
                    tint = MaterialTheme.extendedColor.primaryColor
                )
            }
        }
    }






}

@Preview
@Composable
private fun Pre() {
    MessageSentItem(
        chatMessageUi = ChatMessageUi(
            id = "",
            isMine = true,
            message = "Ustoz bugun birinchi darsga",
            createdAt = 0,
            time = "10:25",
            isRead = true,
            senderName = "Abror",
            senderAvatar = ""
        )
    )
}