package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.tikoncha_parent.domain.model.Chat
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomText


@Composable
fun ChatListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    chat: Chat
) {

    val icon = if (chat.isAI){
        painterResource(Res.drawable.chat_ai_icon)
    }
    else{
        painterResource(Res.drawable.chat_icon)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(NormalIconSize)
                .border(1.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
        )
        SpaceSmall()
        Column(
            modifier = Modifier
                .weight(1f)
        )
        {
            CustomText(
                text = chat.title,
                fontSize = NormalTextSize,
            )
            if (chat.lastSender != null) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.chat_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(SmallIconSize)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    )
                    SpaceUltraSmall()
                    CustomText(
                        text = "${chat.lastSender.lastname} ${chat.lastSender.name}",
                        fontSize = UltraSmallTextSize,
                        maxLines = 1,
                    )
                }
            }
            CustomText(
                text = chat.lastMessage,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = UltraSmallTextSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        SpaceSmall()

        Column(
            horizontalAlignment = Alignment.End
        ) {
            CustomText(
                text = chat.time,
                fontSize = UltraSmallTextSize
            )

            SpaceSmall()
            if (chat.unReadCount == 0) {
                Spacer(Modifier.height(4.dp))

            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ChatMessageColor)
                    .size(SmallIconButtonSize),
                contentAlignment = Alignment.Center
            ){
                val textSize = if (chat.unReadCount == 9){
                    UltraSmallTextSize
                }
                else{
                    SmallTextSize
                }
                CustomText(
                    text = "${chat.unReadCount}",
                    color = MaterialTheme.colorScheme.background,
                    fontSize = textSize
                )
            }
        }
    }
}


