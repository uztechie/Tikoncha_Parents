package org.example.project.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.domain.model.Chat
import org.example.project.domain.model.User
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.SearchField
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.DividerColor
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.TextFieldHeight
import org.example.project.presentation.monitoring.ClientPermissionStateScreen
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.ism_orqali_qidirish
import tikoncha_parents.composeapp.generated.resources.suhbat

class ChatScreen: Screen {
    @Composable
    override fun Content() {
        ChatUi()
    }
}


@Composable
fun ChatUi(modifier: Modifier = Modifier) {
    val navigator = LocalNavigator.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.suhbat),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            }
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp)
        )
        {

            SearchField(
                query = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TextFieldHeight),
                label = stringResource(Res.string.ism_orqali_qidirish),
                onQueryChange = {}
            )

            SpaceMedium()
            LazyColumn(modifier = Modifier
                .fillMaxSize(),
            ) {
                items(chatList){chat->
                    ChatListItem(
                        chat = chat,
                        onClick = {
                            if (chat.isAI){
                                navigator?.push(ChatMessageScreen())
                            }
                        }
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = DividerColor)
                }

            }

        }
    }
}

val chatList = listOf<Chat>(
    Chat(
        title = "9A sinf",
        avatar = "",
        id = 1,
        time = "22:15",
        isRead = false,
        lastSender = User(
            avatar = "",
            name = "Rustamova",
            lastname = "Dilorom"
        ),
        lastMessage = "Ertaga soat 6 da ota onalar yig’ilishi",
        unReadCount = 2
    ),
    Chat(
        title = "Davriyev Akram/Sinf rahbar",
        avatar = "",
        id = 1,
        time = "20:15",
        isRead = false,
        lastSender = null,
        lastMessage = "Ertaga soat 6 da ota onalar yig’ilishi",
        unReadCount = 1
    ),
    Chat(
        isAI = true,
        title = "Yordamchi tipratikon",
        avatar = "",
        id = 1,
        time = "22:15",
        isRead = false,
        lastMessage = "Yangi vazifalar tayyor",
        unReadCount = 1
    )
)

