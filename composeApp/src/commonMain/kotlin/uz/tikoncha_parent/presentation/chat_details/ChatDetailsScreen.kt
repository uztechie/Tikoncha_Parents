package uz.tikoncha_parent.presentation.chat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.azolar
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.ok
import tikoncha_parents.composeapp.generated.resources.xatolik
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.chat.chat_details.ChatMemberItem
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.chat.ChatViewModel
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.DividerHorizontal
import uz.tikoncha_parent.ui.SmallTextSize
import uz.tikoncha_parent.ui.SpaceLarge
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor



class ChatDetailsScreen(
    val chatId: String,
    val chatAvatar: String,
    val chatTitle: String
): Screen{
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<ChatDetailsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(Unit){
            event(ChatDetailEvent.SetChatData(chatId, chatAvatar, chatTitle))
        }

        ChatDetailsUi(
            state = state,
        )
    }

}
@Composable
fun ChatDetailsUi(
    state: ChatDetailState
) {

    val navigator = LocalNavigator.current
    val loading = state.responseState is ResponseState.Loading
    val errorText = state.responseState.errorText()


    LoadingDialog(show = loading)

    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(errorText) {
        showDialog = errorText.isNotEmpty()
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showDialog,
        title = stringResource(Res.string.xatolik),
        message = errorText,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showDialog = false
        },
        onButtonClick = {
            showDialog = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)


    ) {

        ChatDetailsHeader(
            state = state,
            onBackPressed = {
                navigator?.pop()
            }
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            item {
                CustomText(
                    text = stringResource(Res.string.azolar),
                    fontSize = SmallTextSize,
                    maxLines = 1,
                    color = MaterialTheme.extendedColor.hintColor,
                    fontWeight = FontWeight.W500
                )
                SpaceLarge()
            }
            items(state.members){member->
                ChatMemberItem(member)
                DividerHorizontal()
            }
        }



    }
}

@Preview
@Composable
private fun Preview() {
    TikonchaParentTheme(mode = ThemeMode.LIGHT) {
        ChatDetailsUi(
            state = ChatDetailState()
        )
    }
}