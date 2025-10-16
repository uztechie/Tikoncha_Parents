package uz.tikoncha_parent.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.model.ChatMessageItem
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.chat.*
import uz.tikoncha_parent.presentation.base.CustomMultiLineTextField
import uz.tikoncha_parent.presentation.chat_details.ChatDetailsScreen
import uz.tikoncha_parent.presentation.model.ChatType
import uz.tikoncha_parent.ui.theme.extendedColor


class ChatMessageScreen(
    private val chatId: String,
    private val chatAvatar: String,
    private val chatTitle: String,
    private val chatType: ChatType
) : Screen{
    @Composable
    override fun Content() {
        val viewModel: ChatViewModel = koinViewModel ()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val bugun = stringResource(Res.string.bugun)
        val kecha = stringResource(Res.string.kecha)

        LaunchedEffect(Unit){
            event(ChatEvent.SetChatData(
                chatId = chatId,
                chatAvatar = chatAvatar,
                chatTitle = chatTitle,
                chatType = chatType,
                bugun = bugun,
                kecha = kecha
            ))
        }
        DisposableEffect(Unit) {
            event(ChatEvent.Open("ChatMessageScreen"))
            onDispose {
                event(ChatEvent.Close("ChatMessageScreen"))
            }
        }

        ChatMessageUi(
            state = state,
            event = event
        )
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChatMessageUi(
    state: ChatState,
    event: (ChatEvent) -> Unit,
)
{

    val bottomShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = ShapeCornerRadius,
        bottomEnd = ShapeCornerRadius
    )
    val navigator = LocalNavigator.current

    val listState = rememberLazyListState()


    val atBottom by remember {
        derivedStateOf { isAtBottom(listState, state.allMessages.size) }
    }

    LaunchedEffect(listState, state.allMessages.size) {
        snapshotFlow { atBottom }
            .distinctUntilChanged()
            .collect { isBottom ->
                if (isBottom && state.allMessages.isNotEmpty()) {
                    event(ChatEvent.OnScrollLastMessage)
                }
            }
    }


    LaunchedEffect(true) {
        event(ChatEvent.OnScrollLastMessage)
        ChatUnreadEventBus.tryEmit(
            ChatUnreadEventBus.ChatUnreadEvent.MessageReceived(state.lastMessage?.id?:"")
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)


    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderHeight),
            verticalArrangement = Arrangement.Center
        )
        {
            Box(
                Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = bottomShape,
                        ambientColor = MaterialTheme.extendedColor.shadowColor,
                        spotColor = MaterialTheme.extendedColor.shadowColor
                    )
                    .background(
                        color = MaterialTheme.extendedColor.backgroundColor,
                        shape = bottomShape
                    )

            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(HeaderHeight)
                        .padding(horizontal = ContainerPadding),
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    FilledTonalIconButton(
                        modifier = Modifier
                            .size(NormalIconButtonSize),
                        onClick = {
                            navigator?.pop()
                        },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.extendedColor.cardColor,
                            contentColor = MaterialTheme.extendedColor.textColor
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

                    AsyncImage(
                        model = state.chatAvatar,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(NormalIconButtonSize)
                            .border(1.dp, TonalButtonContainerColor, CircleShape)
                            .clip(CircleShape),
                        error = painterResource(Res.drawable.chat_icon),
                        placeholder = painterResource(Res.drawable.chat_icon)

                    )


                    SpaceSmall()

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = null,
                                onClick = {
                                    if (state.chatType == ChatType.CLASS){
                                        navigator?.push(
                                            ChatDetailsScreen(
                                                chatId = state.chatId,
                                                chatAvatar = state.chatAvatar,
                                                chatTitle = state.chatTitle
                                            )
                                        )
                                    }
                                }
                            )
                    ) {
                        val styledText = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.extendedColor.textColor
                                )
                            ) {
                                append("Ustoz /")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.extendedColor.hintColor
                                )
                            ) {
                                append(" Ibroxim Odilov")
                            }
                        }

                        CustomText(
                            text = state.chatTitle,
                            fontSize = SmallTextSize,
                            maxLines = 1,
                            lineHeight = SmallTextSize,
                            fontWeight = FontWeight.W500
                        )

                        val status = when(state.chatType){
                            ChatType.CLASS -> {
                                "${stringResource(Res.string.azolar)}: ${state.chatMembersCount}"
                            }
                            else -> {
                                when (state.isUserOnline) {
                                    true -> stringResource(Res.string.faol)
                                    false -> "${stringResource(Res.string.ohirgi_faollik)}: ${state.lastTimeOnline}"
                                }
                            }
                        }

                        val headerTextColor =  if (state.isUserOnline && state.chatType != ChatType.CLASS) {
                            MaterialTheme.extendedColor.primaryColor
                        } else {MaterialTheme.extendedColor.hintColor }

                        CustomText(
                            text = status,
                            fontSize = UltraSmallTextSize,
                            maxLines = 1,
                            color = headerTextColor,
                            lineHeight = SmallTextSize,
                            fontWeight = FontWeight.W500
                        )
                    }

                }
            }
        }


        Column(
            modifier = Modifier
                .padding(ContainerPadding)
                .imePadding()

        ) {


            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                reverseLayout = true
            )
            {

                items(state.messages) { messageItem ->
                    when (messageItem) {
                        is ChatMessageItem.DateHeader -> {
                            MessageDateItem(
                                date = messageItem.date
                            )
                        }

                        is ChatMessageItem.Message -> {
                            if (messageItem.chatMessageUi.isMine) {
                                MessageSentItem(
                                    chatMessageUi = messageItem.chatMessageUi
                                )
                            } else {
                                MessageReceivedItem(
                                    chatMessageUi = messageItem.chatMessageUi,
                                    showSender = if (state.chatType == ChatType.CLASS) true else false
                                )
                            }
                        }
                    }
                }

            }

            SpaceMedium()

            CustomMultiLineTextField(
                value = state.message,
                onValueChange = {
                    event(ChatEvent.OnMessageChange(it))
                },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = TextFieldHeight, max = TextFieldHeight * 5)
                    .imePadding(),
                label = stringResource(Res.string.xabar_yozish),
                containerColor = MaterialTheme.extendedColor.cardColor,
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(NormalIconButtonSize)
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
                    if (state.message.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                event(ChatEvent.SendMessage)
                            },
                            modifier = Modifier
                                .size(NormalIconButtonSize)

                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.chat_send),
                                contentDescription = "",
                                modifier = Modifier,
                                tint = PrimaryColor
                            )
                        }
                    }
//                    if (state.isReceivingMessage) {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .size(SmallIconButtonSize)
//                        )
//                    }

                },
                readOnly = false,

                )
        }
    }
}

fun isAtBottom(state: LazyListState, itemsCount: Int, thresholdPx: Int = 12): Boolean {
    if (itemsCount == 0) return true
    val layout = state.layoutInfo
    val lastIndex = itemsCount - 1
    val visible = layout.visibleItemsInfo
    val lastVisible = visible.lastOrNull() ?: return false

    if (lastVisible.index != lastIndex) return false

    // Element to‘liq ko‘rinadimi?
    val itemBottom = lastVisible.offset + lastVisible.size
    val viewportBottom = layout.viewportEndOffset
    return itemBottom <= viewportBottom + thresholdPx
}

@Preview
@Composable
private fun PRe() {
    ChatMessageUi(
        state = ChatState(),
        event = {}
    )
}