package org.example.project.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.example.project.domain.model.Chat
import org.example.project.presentation.base.theme.ChatIconSize
import org.example.project.presentation.base.theme.ChatMessageColor
import org.example.project.presentation.base.theme.ChatSmallIconSize
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.NormalTextSize
import org.example.project.presentation.base.theme.SmallIconButtonSize
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.SpaceUltraSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.UltraSmallTextSize
import org.example.project.ui.OnPrimaryColor
import org.example.project.ui.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.chat_ai_icon
import tikoncha_parents.composeapp.generated.resources.chat_icon
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
                .size(ChatIconSize)
                .border(1.dp, TonalButtonContainerColor, CircleShape),
        )
        SpaceSmall()
        Column(
            modifier = Modifier
                .weight(1f)
        )
        {
            CustomText(
                text = chat.title,
                color = TextColor,
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
                            .size(ChatSmallIconSize)
                            .background(TonalButtonContainerColor)
                            .border(1.dp, TonalButtonContainerColor, CircleShape),
                    )
                    SpaceUltraSmall()
                    CustomText(
                        text = "${chat.lastSender.lastname} ${chat.lastSender.name}",
                        color = TextColor,
                        fontSize = UltraSmallTextSize,
                        maxLines = 1,
                    )

                }
            }
            CustomText(
                text = chat.lastMessage,
                color = HintTextColor,
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
                color = TextColor,
                fontSize = UltraSmallTextSize
            )

            SpaceSmall()
            if (chat.unReadCount > 0) {
                Spacer(Modifier.height(4.dp))

            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ChatMessageColor)
                    .size(SmallIconButtonSize),
                contentAlignment = Alignment.Center
            ){
                val textSize = if (chat.unReadCount>9){
                    UltraSmallTextSize
                }
                else{
                    SmallTextSize
                }
                CustomText(
                    text = "${chat.unReadCount}",
                    color = OnPrimaryColor,
                    fontSize = textSize
                )
            }
        }

    }

}


