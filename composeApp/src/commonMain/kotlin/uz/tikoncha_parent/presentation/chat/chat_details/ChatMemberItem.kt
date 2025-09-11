package uz.saidburxon.newedu.presentation.feature.chat.chat_details

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.saidburxon.newedu.R
import uz.saidburxon.newedu.ui.theme.LargeIconButtonSize
import uz.saidburxon.newedu.ui.theme.NormalTextSize
import uz.saidburxon.newedu.ui.theme.SpaceMedium
import uz.saidburxon.newedu.ui.theme.TonalButtonContainerColor
import uz.saidburxon.newedu.ui.theme.UltraSmallTextSize
import uz.saidburxon.newedu.ui.theme.extendedColor
import uz.tikoncha_parent.presentation.model.ChatMemberUi

@Composable
fun ChatMemberItem(chatMemberUi: ChatMemberUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        AsyncImage(
            model = chatMemberUi.avatar,
            error = painterResource(R.drawable.chat_icon),
            placeholder = painterResource(R.drawable.chat_icon),
            contentDescription = null,
            modifier = Modifier
                .size(LargeIconButtonSize)
                .border(1.dp, TonalButtonContainerColor, CircleShape)
                .clip(CircleShape)

        )


        SpaceMedium()


        Column(Modifier.fillMaxWidth()) {
            CustomText(
                text = chatMemberUi.name,
                fontSize = NormalTextSize,
                maxLines = 1,
                lineHeight = NormalTextSize,
                fontWeight = FontWeight.W600
            )



            val memberBuilder = StringBuilder()
            if (chatMemberUi.isOnline){
                memberBuilder.append(stringResource(R.string.faol))
            }else{
                memberBuilder.append(stringResource(R.string.ohirgi_faollik))
                memberBuilder.append(": ")
                memberBuilder.append(chatMemberUi.lastSeen)
            }
            CustomText(
                text = memberBuilder.toString(),
                fontSize = UltraSmallTextSize,
                maxLines = 1,
                lineHeight = UltraSmallTextSize,
                color = when (chatMemberUi.isOnline) {
                    true -> MaterialTheme.extendedColor.primaryColor
                    false -> MaterialTheme.extendedColor.hintColor
                }
            )


        }
    }
}

@Preview
@Composable
private fun Pre() {
    ChatMemberItem(
        chatMemberUi = ChatMemberUi(
            id = "",
            avatar = "",
            name = "Ibroxim Odilov"
        )
    )
}