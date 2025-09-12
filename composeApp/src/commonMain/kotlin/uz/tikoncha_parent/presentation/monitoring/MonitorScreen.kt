package uz.tikoncha_parent.presentation.monitoring

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomMultiLineTextField
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.base.DividedButton
import uz.tikoncha_parent.presentation.chat.ChatScreen
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.ui.theme.extendedColor


class MonitorScreen: Screen {
    @Composable
    override fun Content() {
        MonitorUi()
    }
}


val items = listOf<String>("Alijonov Karimjon", "Alijonova Gulmira")

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
        title = stringResource(Res.string.farzandlaringiz),
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
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.kuzatuv)
        )

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
                title = stringResource(Res.string.ekranni_kuzatish),
                icon = painterResource(Res.drawable.monitor_screen),
                onItemClick = {},
                isPermission = false
            )

            SpaceSmall()

            DividedButton(
                title = stringResource(Res.string.yon_atrofni_kuzatish),
                icon = painterResource(Res.drawable.permission_camera),
                onItemClick = {},
                isPermission = false
            )

            SpaceSmall()

            DividedButton(
                title = stringResource(Res.string.yon_atrofni_eshitish),
                icon = painterResource(Res.drawable.microphonee),
                onItemClick = {},
                isPermission = false
            )

            SpaceSmall()

            DividedButton(
                title = stringResource(Res.string.bolaning_ilovasini_sozligini_korish),
                icon = painterResource(Res.drawable.permission_adminstration),
                onItemClick = {
                    rootNavigator?.push(ClientPermissionStateScreen())
                },
                isPermission = false
            )

            SpaceSmall()

            DividedButton(
                title = stringResource(Res.string.farzandingiz_bilan_suhbat),
                icon = painterResource(Res.drawable.dialogg),
                onItemClick = {
                    rootNavigator?.push(ChatScreen())
                },
                isPermission = false
            )

            SpaceSmall()

            DividedButton(
                title = stringResource(Res.string.internetdagi_tarix),
                icon = painterResource(Res.drawable.clock),
                onItemClick = {

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
                label = stringResource(Res.string.xabar_yuborish),
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
