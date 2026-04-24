package uz.tikoncha_parent.presentation.child_confirm_cod

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.Util.format6DigitCode
import uz.tikoncha_parent.platform.copyPlainText
import uz.tikoncha_parent.presentation.base.LogoHeader
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomText
import uz.tikoncha_parent.presentation.new_home.NewHomeScreen
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor
import uz.tikoncha_parent.ui.theme.rememberScreenSystemBars

class ChildConfirmCodeRegisterScreen(
    private val confirmCode: String
): Screen {
    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<ChildConfirmViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val navigator = LocalNavigator.current

        LaunchedEffect(confirmCode){
            event(ChildConfirmEvent.SetConfirmCode(confirmCode))
        }

        ChildConfirmCodeRegisterUi(
            navigator = navigator,
            state = state.value,
            event = event
        )
    }
}

@Composable
fun ChildConfirmCodeRegisterUi(
    navigator: Navigator?,
    state: ChildConfirmState,
    event: (ChildConfirmEvent)-> Unit
)
{

    val scope = rememberCoroutineScope ()
    val clipboard = LocalClipboard.current



    val formatted = remember(state.codeNumber) { format6DigitCode(state.codeNumber) }


    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.secondary
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .imePadding()
            .background(AppColors.bg.secondary)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.extendedColor.backgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            LogoHeader()

            CustomText(
                text = stringResource(Res.string.farzandingizni_tasdiqlang),
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
            )

            SpaceLarge()
            SpaceLarge()
            SpaceLarge()


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.extendedColor.cardColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(TextFieldHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.password_check),
                    contentDescription = null,
                    tint = MaterialTheme.extendedColor.hintColor,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable {
                            scope.launch {
                                copyPlainText(clipboard, formatted)
                            }
                        }
                )
                SpaceSmall()

                CustomText(
                    text = formatted,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )

            }


            SpaceSmall()

            CustomText(
                text = stringResource(Res.string.ushbu_kodni_farzandingiz_telefonidan_kiriting),
                fontSize = NormalTextSize,
                color = MaterialTheme.extendedColor.hintColor,
                fontWeight = FontWeight.W500
            )


            Spacer(modifier = Modifier.weight(1f))


            CustomButton(
                onClick = {
                    navigator?.replaceAll(NewHomeScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.davom_etish)
            )
            SpaceLarge()
        }
    }
}


@Preview
@Composable
private fun Pre(){
    TikonchaParentTheme(
        ThemeMode.DARK
    ){
        ChildConfirmCodeRegisterUi(
            navigator = null,
            state = ChildConfirmState(),
            event = {}
        )
    }
}

