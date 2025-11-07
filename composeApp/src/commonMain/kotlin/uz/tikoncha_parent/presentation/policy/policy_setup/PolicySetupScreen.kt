package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.verticalShadow
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebSelectionScreen
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleListScreen
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleTypeSelectionScreen
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleListScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomMultiLineTextField
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.policy.PolicyEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.*

class PolicySetupScreen(
    val child: UserInfo?
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val viewModel = koinViewModel<PolicySetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        Logger.d("PolicySetupScreen", "Content: ${sharedState.timeList}")
        LaunchedEffect(sharedState.timeList, sharedState.limitList){
            Logger.d("PolicySetupScreen", "Content effect: ${sharedState.timeList}")
            event(PolicySetupEvent.SetLimitRule(sharedState.limitList))
            event(PolicySetupEvent.SetTimeRule(sharedState.timeList))
            event(PolicySetupEvent.SetSelectedChild(child))
        }



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



    val loading = state.responseState is ResponseState.Loading
    val errorText = state.responseState.errorText()
    val success = state.responseState is ResponseState.Success

    LoadingDialog(loading)

    var showErrorDialog by remember() {
        mutableStateOf(false)
    }
    var showSuccessDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(errorText){
        if (errorText.isNotEmpty()){
            showErrorDialog = true
        }
    }
    LaunchedEffect(success){
        if (success){
            showSuccessDialog = true
        }
    }

    CustomDialog(
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = errorText,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showErrorDialog = false
        },
        onButtonClick = {
            showErrorDialog = false
        }
    )

    CustomDialog(
        show = showSuccessDialog,
        title = stringResource(Res.string.muvaffaqiyatli),
        message = stringResource(Res.string.jadval_muvaffaqiyatli_yaratildi),
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showSuccessDialog = false
        },
        onButtonClick = {
            showSuccessDialog = false
            navigator?.pop()
        }
    )



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
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = ContainerPadding)
                .verticalScroll(rememberScrollState())
        )
        {
            CustomText(
                text = stringResource(Res.string.shartlar),
                fontSize = LargeTextSize,
                fontWeight = FontWeight.SemiBold
            )

            SpaceLarge()

            if (state.timeList.isNotEmpty()){
                val subTitle = if (state.timeList.size == 1){
                    val timeRule = state.timeList.first()
                    val weekdays = if (timeRule.weekDays.size == 7){
                        stringResource(Res.string.har_kuni)
                    }
                    else{
                        timeRule.weekDays.map { it.weekdayLabel() }.joinToString(", ")
                    }
                    val time = timeRule.time

                    if (timeRule.outside){
                        "$weekdays  $time (${stringResource(Res.string.tashqarida)})"
                    }
                    else{
                        "$weekdays  $time"
                    }
                }

                else{
                    "${state.timeList.size} ${stringResource(Res.string.ta_jadval)}"
                }

                PolicySetupRuleItem(
                    title = stringResource(Res.string.vaqt),
                    subTitle = subTitle,
                    onRemoveClick = {
                        event(PolicySetupEvent.SetTimeRule(emptyList()))
                    },
                    onItemClick = {
                        navigator?.push(TimeRuleListScreen())
                    }

                )
                SpaceLarge()
            }

            if (state.limitList.isNotEmpty()){
                val subTitle = if (state.limitList.size == 1){
                    val limitRule = state.limitList.first()
                    val weekdays = if (limitRule.weekDays.size == 7){
                        stringResource(Res.string.har_kuni)
                    }
                    else{
                        limitRule.weekDays.map { it.weekdayLabel() }.joinToString(", ")
                    }
                    val hourMinute = limitRule.time

                    val time = StringBuilder()
                    if (hourMinute.hour > 0) {
                        time.append(hourMinute.hour)
                        time.append(" ")
                        time.append(stringResource(Res.string.soat))
                        time.append(", ")
                    }
                    if (hourMinute.minute > 0){
                        time.append(hourMinute.minute)
                        time.append(" ")
                        time.append(stringResource(Res.string.daqiqa))
                    }

                    val type = if (limitRule.limitType == DayHour.DAY) stringResource(Res.string.kunlik) else stringResource(Res.string.soatlik)

                    "$weekdays  ${time} \n${type}"
                }

                else{
                    "${state.limitList.size} ${stringResource(Res.string.ta_jadval)}"
                }

                PolicySetupRuleItem(
                    title = stringResource(Res.string.vaqt),
                    subTitle = subTitle,
                    onRemoveClick = {
                        event(PolicySetupEvent.SetLimitRule(emptyList()))
                    },
                    onItemClick = {
                        navigator?.push(LimitRuleListScreen())
                    }

                )
                SpaceLarge()
            }

            SpaceMedium()

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
            SpaceLarge()

            CustomMultiLineTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.packagesString,
                onValueChange = {
                    event(PolicySetupEvent.UpdatePackagesLint(it))
                },
                containerColor = MaterialTheme.extendedColor.cardColor,
                label = "Packages",
                singleLine = false
            )



//            Column {
//
//
//
//

//
//
//
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                CustomText(
//                    text = stringResource(Res.string.qora_ro_yxat),
//                    fontSize = LargeTextSize,
//                    fontWeight = FontWeight.SemiBold
//                )
//
//                IconButton(
//                    onClick = { }
//                ) {
//                    Image(
//                        painter = painterResource(Res.drawable.arrow_down),
//                        contentDescription = null,
//                        colorFilter = ColorFilter.tint(MaterialTheme.extendedColor.textColor)
//                    )
//                }
//            }
//
//            CustomText(
//                text = stringResource(Res.string.bloklamoqchi_bo_lgan_ilova_yoki_saytlarni_tanlang),
//                fontSize = NormalTextSize,
//                color = HintTextColor
//            )
//
//            SpaceLarge()
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable{
//                        navigator?.push(AppWebSelectionScreen())
//                    }
//                    .verticalShadow(
//                        shape = RoundedCornerShape(CardCornerRadius),
//                        offset = 0.dp
//                    )
//                    .background(bgColor, RoundedCornerShape(CardCornerRadius)
//                    )
//                    .padding(ContainerPadding)
//            )
//            {
//                val icons = listOf(
//                    Res.drawable.instagram_icon,
//                    Res.drawable.whatsapp_icon,
//                    Res.drawable.discord_icon,
//                    Res.drawable.linkedin_icon,
//                    Res.drawable.social_x_icon,
//                    Res.drawable.google_icon
//                )
//
//                val statusColor = if (icons.isEmpty()) HintTextColor else PrimaryColor
//                val countText = icons.size.toString()
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    CustomText(
//                        text = stringResource(Res.string.ilovalar),
//                        fontSize = LargeTextSize,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                    Spacer(Modifier.weight(1f))
//
//                    Icon(
//                        painter = painterResource(Res.drawable.apps_icon),
//                        contentDescription = null,
//                        tint = statusColor,
//                        modifier = Modifier.size(SmallIconSize)
//                    )
//                    Spacer(Modifier.size(4.dp))
//                    CustomText(
//                        text = countText,
//                        fontSize = NormalTextSize,
//                        color = statusColor
//                    )
//
//                    SpaceUltraSmall()
//                    Icon(
//                        painter = painterResource(Res.drawable.arrow_right),
//                        contentDescription = null,
//                        modifier = Modifier.size(NormalIconSize),
//                        tint = MaterialTheme.extendedColor.textColor
//                    )
//                }
//
//                SpaceSmall()
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .horizontalScroll(rememberScrollState()),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//
//                    icons.forEach { icon ->
//                        Image(
//                            painter = painterResource(icon),
//                            contentDescription = null,
//                            modifier = Modifier.size(NormalIconSize)
//                        )
//                    }
//                }
//            }
//            SpaceLarge()
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .verticalShadow(
//                        shape = RoundedCornerShape(CardCornerRadius),
//                        offset = 0.dp
//                    )
//                    .background(bgColor, RoundedCornerShape(CardCornerRadius)
//                    )
//            )
//            {
//                val sayt = listOf(
//                    "Instagram_com",
//                    "Whatsapp_com",
//                    "Discord_com",
//                    "Linkedin_com",
//                    "Social_x_com",
//                    "Google_com"
//                )
//
//                val statusColor = if (sayt.isEmpty()) HintTextColor else PrimaryColor
//                val countText = sayt.size.toString()
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(
//                            start = ContainerPadding,
//                            end = ContainerPadding,
//                            top = ContainerPadding
//                        ),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//                    CustomText(
//                        text = stringResource(Res.string.veb_sayt),
//                        fontSize = LargeTextSize,
//                        fontWeight = FontWeight.SemiBold
//                    )
//
//                    Spacer(Modifier.weight(1f))
//
//                    Icon(
//                        painter = painterResource(Res.drawable.globuse),
//                        contentDescription = null,
//                        tint = statusColor,
//                        modifier = Modifier.size(SmallIconSize)
//                    )
//                    Spacer(Modifier.size(4.dp))
//                    CustomText(
//                        text = countText,
//                        fontSize = NormalTextSize,
//                        color = statusColor
//                    )
//
//                    SpaceUltraSmall()
//                    Icon(
//                        painter = painterResource(Res.drawable.arrow_right),
//                        contentDescription = null,
//                        modifier = Modifier.size(NormalIconSize),
//                        tint = MaterialTheme.extendedColor.textColor
//                    )
//                }
//
//                SpaceSmall()
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = ContainerPadding, bottom = ContainerPadding)
//                        .horizontalScroll(rememberScrollState()),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    sayt.forEach { sayt ->
//                        Column(
//                            modifier = Modifier
//                                .verticalShadow(
//                                    shape = RoundedCornerShape(CardCornerRadius),
//                                    offset = 0.dp
//                                )
//                                .background( MaterialTheme.extendedColor.backgroundColor,
//                                    RoundedCornerShape(CardCornerRadius))
//                                .padding(horizontal = 8.dp)
//                        ) {
//                            CustomText(
//                                text = sayt,
//                                fontSize = SmallTextSize,
//                            )
//                        }
//                    }
//                }
//            }
//
//
//                }
            Spacer(Modifier.weight(1f))

        }

        CustomButton(
            enabled = state.limitList.isNotEmpty() || state.timeList.isNotEmpty(),
            text = stringResource(Res.string.saqlash),
            onClick = { 
                event(PolicySetupEvent.SavePolicy)
            },
            modifier = Modifier
                .padding(ContainerPadding)
                .fillMaxWidth()
                .height(ButtonHeight),
        )
    }
}

@Preview
@Composable
private fun PreviewTableScreen() {
    PolicySetupUi(
        navigator = null
    )
}