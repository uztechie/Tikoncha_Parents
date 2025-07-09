package org.example.project.presentation.monitoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.CustomMultiLineTextField
import org.example.project.presentation.base.CustomSelectionButton
import org.example.project.presentation.base.DividedButton
import org.example.project.presentation.base.theme.BackgroundColor
import org.example.project.presentation.base.theme.ContainerPadding
import org.example.project.presentation.base.theme.SmallTextSize
import org.example.project.presentation.base.theme.SpaceLarge
import org.example.project.presentation.base.theme.SpaceMedium
import org.example.project.presentation.base.theme.SpaceSmall
import org.example.project.presentation.base.theme.SpaceUltraSmall
import org.example.project.presentation.base.theme.TextColor
import org.example.project.presentation.chat.ChatScreen
import org.example.project.presentation.common.CustomListDialog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.dialogg
import tikoncha_parents.composeapp.generated.resources.farzandingiz
import tikoncha_parents.composeapp.generated.resources.kuzatuv
import tikoncha_parents.composeapp.generated.resources.microphonee
import tikoncha_parents.composeapp.generated.resources.permission_adminstration
import tikoncha_parents.composeapp.generated.resources.permission_camera
import tikoncha_parents.composeapp.generated.resources.person
import tikoncha_parents.composeapp.generated.resources.tanlang
import tikoncha_parents.composeapp.generated.resources.yuborish
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText


class MonitorScreen: Screen {
    @Composable
    override fun Content() {
        MonitorUi()
    }
}


val items = listOf<String>("Alijonov Karimjon", "Alijonova Gulmira")

@Preview
@Composable
private fun MonitorUi(){

    val mainNavigator = LocalNavigator.current
    val rootNavigator = mainNavigator?.parent

    var message by remember {
        mutableStateOf("")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }
    var selectedChild by remember {
        mutableStateOf("")
    }

    CustomListDialog(
        title = "Farzandlaringiz",
        items = items,
        show = showDialog,
        onItemSelected = {
            selectedChild = it
        },
        onDismiss = {
            showDialog = false
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.kuzatuv)
        ) {

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ContainerPadding)
                .verticalScroll(rememberScrollState()),
        )
        {
            CustomText(
                text = stringResource(Res.string.farzandingiz),
                fontSize = SmallTextSize,
                color = TextColor
            )

            SpaceUltraSmall()

            CustomSelectionButton(
                onClick = {
                    showDialog = true
                },
                label = stringResource(Res.string.tanlang),
                text = selectedChild,
                painter = painterResource(Res.drawable.person)
            )
            SpaceLarge()
            SpaceLarge()
            DividedButton(
                title = "Yon atrofini kuzatish",
                icon = painterResource(Res.drawable.permission_camera),
                onItemClick = {},
                isPermission = false
            )
            SpaceSmall()
            DividedButton(
                title = "Yon atrofini eshitish",
                icon = painterResource(Res.drawable.microphonee),
                onItemClick = {},
                isPermission = false
            )
            SpaceSmall()
            DividedButton(
                title = "Bolaning ilovasi sozligini ko’rish",
                icon = painterResource(Res.drawable.permission_adminstration),
                onItemClick = {
                    rootNavigator?.push(ClientPermissionStateScreen())
                },
                isPermission = false
            )
            SpaceSmall()
            DividedButton(
                title = "Farzandingiz bilan suhbat",
                icon = painterResource(Res.drawable.dialogg),
                onItemClick = {
                    rootNavigator?.push(ChatScreen())
                },
                isPermission = false
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            SpaceLarge()


            CustomMultiLineTextField(
                value = message,
                onValueChange = {
                    message = it
                },
                hasBorder = true,
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = false,
                label = "Xabar yuborish",
            )
            SpaceMedium()
            CustomButton(
                text = stringResource(Res.string.yuborish),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            )



        }





    }
}
