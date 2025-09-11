package uz.saidburxon.newedu.presentation.feature.chat.chat_details

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import uz.saidburxon.newedu.R
import uz.saidburxon.newedu.presentation.base.components.CustomDialog
import uz.saidburxon.newedu.presentation.base.components.LoadingDialog
import uz.saidburxon.newedu.ui.theme.ContainerPadding
import uz.saidburxon.newedu.ui.theme.DividerHorizontal
import uz.saidburxon.newedu.ui.theme.NewEduTheme
import uz.saidburxon.newedu.ui.theme.SmallTextSize
import uz.saidburxon.newedu.ui.theme.SpaceLarge
import uz.saidburxon.newedu.ui.theme.extendedColor
import uz.tikoncha_parent.presentation.chat.chat_details.ChatDetailState
import uz.tikoncha_parent.presentation.chat.chat_details.ChatDetailsHeader

@Composable
fun ChatDetailsScreen(
    navController: NavController,
    state: ChatDetailState
) {


    LoadingDialog(show = state.loading)

    var showDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.error) {
        showDialog = state.error.isNotEmpty()
    }

    CustomDialog(
        show = showDialog,
        title = stringResource(R.string.xatolik),
        message = state.error,
        buttonText = stringResource(R.string.ok),
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
            navController = navController,
            state = state
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(ContainerPadding)
        ) {
            item {
                CustomText(
                    text = stringResource(R.string.azolar),
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
    NewEduTheme {
        ChatDetailsScreen(
            navController = rememberNavController(),
            state = ChatDetailState()
        )
    }
}