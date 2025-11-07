package uz.tikoncha_parent.presentation.policy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.add_square
import tikoncha_parents.composeapp.generated.resources.barcha_farzandlar
import tikoncha_parents.composeapp.generated.resources.discord_icon
import tikoncha_parents.composeapp.generated.resources.google_icon
import tikoncha_parents.composeapp.generated.resources.har_kuni_bloklashni_rejalashtiring
import tikoncha_parents.composeapp.generated.resources.ilovalar
import tikoncha_parents.composeapp.generated.resources.instagram_icon
import tikoncha_parents.composeapp.generated.resources.jadval
import tikoncha_parents.composeapp.generated.resources.linkedin_icon
import tikoncha_parents.composeapp.generated.resources.shartlar_kiritish
import tikoncha_parents.composeapp.generated.resources.social_x_icon
import tikoncha_parents.composeapp.generated.resources.whatsapp_icon
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebSelectionScreen
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupEvent
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.ui.ButtonCornerRadius
import uz.tikoncha_parent.ui.ButtonHeight
import uz.tikoncha_parent.ui.CardCornerRadius
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.HintTextColor
import uz.tikoncha_parent.ui.LargeTextSize
import uz.tikoncha_parent.ui.NormalIconSize
import uz.tikoncha_parent.ui.NormalTextSize
import uz.tikoncha_parent.ui.PrimaryColor
import uz.tikoncha_parent.ui.SpaceMedium
import uz.tikoncha_parent.ui.SpaceSmall
import uz.tikoncha_parent.ui.theme.extendedColor


class PolicyListScreen(
    val child: UserInfo?
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current


        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()
        val sharedEvent = sharedViewModel::onEvent

        val viewModel = koinViewModel<PolicyViewModel>()
        val event = viewModel::onEvent
        val state by viewModel.state.collectAsStateWithLifecycle()

        LaunchedEffect(Unit){
            event(PolicyEvent.SetSelectedChild(child))
        }


        PolicyListUi(
            navigator = navigator,
            sharedEvent = sharedEvent,
            event = event,
            state = state
        )
    }
}

@Composable
fun PolicyListUi(
    navigator: Navigator?,
    state: PolicyState,
    event: (PolicyEvent) -> Unit = {},
    sharedEvent: (PolicySharedEvent) -> Unit = {},
){

    val bgColor = MaterialTheme.extendedColor.cardColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        val name = state.selectedChild?.name ?: ""
        val title = StringBuilder()
        title.append(stringResource(Res.string.jadval))
        if (name.isEmpty()){
            title.append(" - ")
            title.append(stringResource(Res.string.barcha_farzandlar))
        }
        else{
            title.append(" - ")
            title.append(name)
        }

        CustomHeader(
            title = title.toString(),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            modifier = Modifier.fillMaxWidth(),
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigator?.push(AppWebSelectionScreen())
                    }
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(
                        bgColor, RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(ContainerPadding)
            ) {
                val icons = listOf(
                    Res.drawable.instagram_icon,
                    Res.drawable.whatsapp_icon,
                    Res.drawable.discord_icon,
                    Res.drawable.linkedin_icon,
                    Res.drawable.social_x_icon,
                    Res.drawable.google_icon
                )

                CustomText(
                    text = stringResource(Res.string.ilovalar),
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                CustomText(
                    text = stringResource(Res.string.har_kuni_bloklashni_rejalashtiring),
                    fontSize = NormalTextSize,
                    color = HintTextColor,
                    style = TextStyle(lineHeight = 14.sp)
                )

                SpaceSmall()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    icons.forEach { icon ->
                        Image(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(NormalIconSize)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    sharedEvent(PolicySharedEvent.ClearData)
                    navigator?.push(PolicySetupScreen(child = state.selectedChild))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            )
            {
                Text(
                    text = stringResource(Res.string.shartlar_kiritish),
                    color = PrimaryColor
                )

                SpaceMedium()

                Icon(
                    painter = painterResource(Res.drawable.add_square),
                    contentDescription = "",
                    tint = PrimaryColor
                )

            }
            SpaceSmall()
        }
    }
}