package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebSelectionScreen
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleTypeSelectionScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.*

class PolicySetupScreen : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<PolicySetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent



        PolicySetupUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}

@Composable
fun PolicySetupUi(
    navigator: Navigator?,
    state: PolicySetupState = PolicySetupState(),
    event: (PolicySetupEvent) -> Unit = {}
) {

    val bgColor = MaterialTheme.extendedColor.cardColor
    Logger.d("TAG", "PolicySetupUi: ${state.timeList}")
    Logger.d("TAG", "PolicySetupUi: ${state.limitList}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = stringResource(Res.string.jadval),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                Box(
                    modifier = Modifier.size(NormalIconButtonSize)
                        .clip(RoundedCornerShape(ShapeCornerRadius))
                        .background(MaterialTheme.extendedColor.cardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.edite_pen_ilne),
                        contentDescription = "Search",
                        tint = MaterialTheme.extendedColor.onBackgroundColor
                    )
                }
            }
        )

        SpaceMedium()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ContainerPadding)
        ) {
            CustomText(
                text = stringResource(Res.string.shartlar),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            SpaceLarge()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(bgColor, RoundedCornerShape(CardCornerRadius))
                    .padding(horizontal = ContainerPadding, vertical = 12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .clickable{ }
                ) {
                    CustomText(
                        text = stringResource(Res.string.vaqt),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    SpaceSmall()
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomText(
                            text = stringResource(Res.string.ishlash_vaqti),
                            fontSize = NormalTextSize,
                            color = HintTextColor
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "9:00",
                            fontSize = LargeTextSize,
                            color = HintTextColor
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "-",
                            color = HintTextColor,
                        )
                        SpaceUltraSmall()
                        CustomText(
                            text = "17:00",
                            fontSize = LargeTextSize,
                            color = HintTextColor
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                Image(
                    painter = painterResource(Res.drawable.close_circle),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor),
                )
            }

            SpaceLarge()

            TextButton(
                onClick = {
                    navigator?.push(RuleTypeSelectionScreen())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PrimaryColor, RoundedCornerShape(ButtonCornerRadius))
                    .height(ButtonHeight),
            ) {
                Row {

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
            }

            SpaceMedium()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = stringResource(Res.string.qora_ro_yxat),
                    fontSize = LargeTextSize,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = { }
                ) {
                    Image(
                        painter = painterResource(Res.drawable.arrow_down),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor)
                    )
                }
            }

            CustomText(
                text = stringResource(Res.string.bloklamoqchi_bo_lgan_ilova_yoki_saytlarni_tanlang),
                fontSize = NormalTextSize,
                color = HintTextColor
            )

            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        navigator?.push(AppWebSelectionScreen())
                    }
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(bgColor, RoundedCornerShape(CardCornerRadius)
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

                val statusColor = if (icons.isEmpty()) HintTextColor else PrimaryColor
                val countText = icons.size.toString()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = stringResource(Res.string.ilovalar),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.apps_icon),
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = countText,
                        fontSize = NormalTextSize,
                        color = statusColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize),
                        tint = MaterialTheme.extendedColor.textColor
                    )
                }

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
            SpaceLarge()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                        offset = 0.dp
                    )
                    .background(bgColor, RoundedCornerShape(CardCornerRadius)
                    )
            ) {
                val sayt = listOf(
                    "Instagram_com",
                    "Whatsapp_com",
                    "Discord_com",
                    "Linkedin_com",
                    "Social_x_com",
                    "Google_com"
                )

                val statusColor = if (sayt.isEmpty()) HintTextColor else PrimaryColor
                val countText = sayt.size.toString()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = ContainerPadding,
                            end = ContainerPadding,
                            top = ContainerPadding
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    CustomText(
                        text = stringResource(Res.string.veb_sayt),
                        fontSize = LargeTextSize,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.weight(1f))

                    Icon(
                        painter = painterResource(Res.drawable.globuse),
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(SmallIconSize)
                    )
                    Spacer(Modifier.size(4.dp))
                    CustomText(
                        text = countText,
                        fontSize = NormalTextSize,
                        color = statusColor
                    )

                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right),
                        contentDescription = null,
                        modifier = Modifier.size(NormalIconSize),
                        tint = MaterialTheme.extendedColor.textColor
                    )
                }

                SpaceSmall()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = ContainerPadding, bottom = ContainerPadding)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    sayt.forEach { sayt ->
                        Column(
                            modifier = Modifier
                                .verticalShadow(
                                    shape = RoundedCornerShape(CardCornerRadius),
                                    offset = 0.dp
                                )
                                .background( MaterialTheme.extendedColor.backgroundColor,
                                    RoundedCornerShape(CardCornerRadius))
                                .padding(horizontal = 8.dp)
                        ) {
                            CustomText(
                                text = sayt,
                                fontSize = SmallTextSize,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            CustomButton(
                text = stringResource(Res.string.saqlash),
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ButtonHeight),
                enabled = true
            )
            SpaceSmall()
        }
    }
}

@Preview
@Composable
private fun PreviewTableScreen() {
    PolicySetupUi(
        navigator = null
    )
}