package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import org.example.project.common.Util.format6DigitCode
import org.example.project.platform.copyPlainText
import org.example.project.presentation.add_child.ChildState
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.LogoHeader
import org.example.project.presentation.child_confirm_cod.ChildConfirmEvent
import org.example.project.presentation.child_confirm_cod.ChildConfirmState

import org.example.project.ui.BorderColor
import org.example.project.ui.ButtonHeight
import org.example.project.ui.ContainerPadding
import org.example.project.ui.NormalTextSize
import org.example.project.ui.PrimaryColor
import org.example.project.ui.SpaceLarge
import org.example.project.ui.SpaceSmall
import org.example.project.ui.TextFieldCornerRadius
import org.example.project.ui.TextFieldHeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.davom_etish
import tikoncha_parents.composeapp.generated.resources.farzand_qoshish
import tikoncha_parents.composeapp.generated.resources.farzandingizni_tasdiqlang
import tikoncha_parents.composeapp.generated.resources.hozir_emas
import tikoncha_parents.composeapp.generated.resources.password_check
import tikoncha_parents.composeapp.generated.resources.qr_screen
import tikoncha_parents.composeapp.generated.resources.ushbu_kodni_farzandingiz_telefonidan_kiriting
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.saidburxon.newedu.presentation.feature.main.MainScreen


@Composable
fun ChildConfirmCodeUi(
    navigator: Navigator?,
    state: ChildConfirmState,
    event: (ChildConfirmEvent)-> Unit
)
{

    val scope = rememberCoroutineScope ()
    val clipboard = LocalClipboard.current



    val formatted = remember(state.codeNumber) { format6DigitCode(state.codeNumber) }




    Column (
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background),

    ) {

        CustomHeader(
            title = stringResource(Res.string.farzand_qoshish),
            showBackButton = true
        ) {
            navigator?.pop()
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {


            CustomText(
                text = stringResource(Res.string.farzandingizni_tasdiqlang),
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
            )

            SpaceLarge()
            SpaceLarge()

            Image(
                painter = painterResource(Res.drawable.qr_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
            )

            SpaceLarge()
            SpaceLarge()


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(TextFieldCornerRadius))
                    .height(TextFieldHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.password_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.W500
            )


            Spacer(modifier = Modifier.weight(1f))


            CustomButton(
                onClick = {
                    navigator?.replaceAll(MainScreen())
                },
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(ButtonHeight),
                text = stringResource(Res.string.davom_etish),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.W600,
            )
            SpaceLarge()
        }
    }
}


@Preview
@Composable
fun Pre() {
    ChildConfirmCodeUi(
        navigator = null,
        state = ChildConfirmState(
            codeNumber = "132133"
        ),
        event = {}
    )
}
