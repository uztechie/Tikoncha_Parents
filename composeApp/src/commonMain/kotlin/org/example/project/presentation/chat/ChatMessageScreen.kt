package org.example.project.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.example.project.presentation.base.ChatTypingTextField
import org.example.project.presentation.base.CustomMultiLineTextField
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ChatMessageBackgroundColor
import org.example.project.presentation.base.theme.ContainerPadding
import org.example.project.presentation.base.theme.HeaderHeight
import org.example.project.presentation.base.theme.HintTextColor
import org.example.project.presentation.base.theme.NormalIconButtonPadding
import org.example.project.presentation.base.theme.NormalIconButtonSize
import org.example.project.presentation.base.theme.PrimaryColor
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.base.theme.TextFieldHeight
import org.example.project.presentation.base.theme.TonalButtonContainerColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left
import tikoncha_parents.composeapp.generated.resources.chat_add
import tikoncha_parents.composeapp.generated.resources.chat_icon
import tikoncha_parents.composeapp.generated.resources.chat_send
import tikoncha_parents.composeapp.generated.resources.xabar_yozish
import uz.saidburxon.newedu.domain.model.ChatMessageItem
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.chat.ChatEvent
import uz.saidburxon.newedu.presentation.feature.chat.ChatState
import uz.saidburxon.newedu.presentation.feature.chat.ChatViewModel
import uz.saidburxon.newedu.presentation.feature.chat.MessageDateItem
import uz.saidburxon.newedu.presentation.feature.chat.MessageReceivedItem
import uz.saidburxon.newedu.presentation.feature.chat.MessageSentItem


class ChatMessageScreen : Screen{
    @Composable
    override fun Content() {
        val viewModel: ChatViewModel = koinViewModel ()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent
        ChatMessageUi(
            state = state,
            event = event
        )
    }

}

@Composable
fun ChatMessageUi(
    state: ChatState,
    event: (ChatEvent) -> Unit
) {

    val navigator = LocalNavigator.current

    val listState = rememberLazyListState()
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)


    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(HeaderHeight),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = ContainerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilledTonalIconButton(
                    modifier = Modifier.size(NormalIconButtonSize),
                    onClick = {
                        navigator?.pop()
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = TonalButtonContainerColor,
                        contentColor = TextColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_left),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(NormalIconButtonPadding)
                    )
                }

                SpaceSmall()
                Image(
                    painter = painterResource(Res.drawable.chat_icon),
                    contentDescription = "",
                    modifier = Modifier.size(NormalIconButtonSize)
                        .border(1.dp, TonalButtonContainerColor, CircleShape)
                        .clip(CircleShape)

                )
                SpaceSmall()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val styledText = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = TextColor
                            )
                        ) {
                            append("Ustoz /")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = HintTextColor
                            )
                        ) {
                            append(" Ibroxim Odilov")
                        }
                    }

                    CustomText(
                        text = styledText,
                        fontSize = SmallTextSize,
                        maxLines = 1,
                        lineHeight = SmallTextSize,
                        fontWeight = FontWeight.W500
                    )
                    CustomText(
                        text = "online",
                        fontSize = SmallTextSize,
                        maxLines = 1,
                        color = PrimaryColor,
                        lineHeight = SmallTextSize,
                        fontWeight = FontWeight.W500
                    )
                }

            }
            HorizontalDivider(
                modifier = Modifier
                    .shadow(elevation = 2.dp)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(ContainerPadding)
        ) {


            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(state.messages) { messageItem ->
                    when (messageItem) {
                        is ChatMessageItem.DateHeader -> {
                            MessageDateItem(
                                date = messageItem.date
                            )
                        }

                        is ChatMessageItem.Message -> {
                            if (messageItem.chatMessage.isMine) {
                                MessageSentItem(chatMessage = messageItem.chatMessage)
                            } else {
                                MessageReceivedItem(chatMessage = messageItem.chatMessage)
                            }
                        }
                    }
                }

            }

            SpaceMedium()

            var textFieldHeight by remember {
                mutableStateOf(0.dp)
            }


            ChatTypingTextField(
                value = state.message,
                onValueChange = {
                    event(ChatEvent.OnMessageChange(it))
                },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = TextFieldHeight, max = TextFieldHeight*5),
                label = stringResource(Res.string.xabar_yozish),
                containerColor = ChatMessageBackgroundColor,
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(NormalIconButtonSize)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.chat_add),
                            contentDescription = "",
                            modifier = Modifier,
                            tint = PrimaryColor
                        )
                    }


                },
                trailingIcon = {
                    if (state.message.isNotEmpty()){
                        IconButton(
                            onClick = {
                                event(ChatEvent.SendMessage)
                            },
                            modifier = Modifier.size(NormalIconButtonSize)

                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.chat_send),
                                contentDescription = "",
                                modifier = Modifier,
                                tint = PrimaryColor
                            )
                        }
                    }

                }
            )
        }


    }

}

@Preview
@Composable
private fun PRe() {
    ChatMessageUi(
        state = ChatState(),
        event = {}
    )
}