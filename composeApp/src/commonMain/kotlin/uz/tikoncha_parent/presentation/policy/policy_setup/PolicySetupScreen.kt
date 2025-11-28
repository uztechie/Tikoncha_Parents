@file:OptIn(ExperimentalComposeUiApi::class, InternalVoyagerApi::class)

package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import coil3.compose.AsyncImage
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
import uz.tikoncha_parent.domain.util.capitalizeFirst
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomDialogTextField
import uz.tikoncha_parent.presentation.base.CustomMultiLineTextField
import uz.tikoncha_parent.presentation.base.CustomOutlinedButton
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.tripleShadow
import uz.tikoncha_parent.presentation.policy.PolicyEvent
import uz.tikoncha_parent.presentation.policy.PolicyItemUi
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebEvent
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebState
import uz.tikoncha_parent.presentation.policy.app_selection.AppWebViewModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.*
import kotlin.compareTo
import kotlin.toString

class PolicySetupScreen(
    val child: UserInfo?,
    val policyItemUi: PolicyItemUi? = null
) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current?:return

        val viewModel = koinViewModel<PolicySetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()



        val sharedAppViewModel = navigator.koinNavigatorScreenModel<AppWebViewModel>()
        val sharedAppState by sharedAppViewModel.state.collectAsStateWithLifecycle()
        val sharedAppEvent = sharedAppViewModel::onEvent


        LaunchedEffect(Unit) {
            sharedAppEvent(AppWebEvent.SetChildId(child?.userId?:""))
            sharedAppEvent(AppWebEvent.GetAppsFromServer)
            sharedAppEvent(AppWebEvent.SetServerPackages(policyItemUi?.packages?:emptyList()))
        }

        LaunchedEffect(Unit){
            event(PolicySetupEvent.SetSelectedChild(child))
        }



        Logger.d("PolicySetupScreen", "Content: ${sharedState.timeList}")
        LaunchedEffect(sharedState.timeList, sharedState.limitList, sharedAppState.selectedPkgs, sharedState.policyTitle){
            event(PolicySetupEvent.SetLimitRule(sharedState.limitList))
            event(PolicySetupEvent.SetTimeRule(sharedState.timeList))
            event(PolicySetupEvent.SetSelectedApps(sharedAppState.selectedPkgs.toList()))
            event(PolicySetupEvent.SetTitle(sharedState.policyTitle))
            sharedState.selectedPolicy?.let {
                event(PolicySetupEvent.SetPolicy(it))
            }
        }




        PolicySetupUi(
            navigator = navigator,
            state = state,
            event = event,
            sharedAppState = sharedAppState,
            sharedState = sharedState,
            sharedEvent = sharedEvent
        )
    }

    private fun rememberSaveable(function: () -> MutableState<Boolean>) {}
}

@Composable
fun PolicySetupUi(
    navigator: Navigator?,
    state: PolicySetupState = PolicySetupState(),
    event: (PolicySetupEvent) -> Unit = {},
    sharedAppState: AppWebState = AppWebState(),
    sharedState: PolicySharedState = PolicySharedState(),
    sharedEvent: (PolicySharedEvent) -> Unit = {}
) {



    var showDialogEdit by remember { mutableStateOf(false) }
    var showCloseConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val loading =
        state.responseState is ResponseState.Loading || state.updateState is ResponseState.Loading || state.deleteState is ResponseState.Loading
    val createErrorText = state.responseState.errorText()
    val createSuccess = state.responseState is ResponseState.Success

    val updateErrorText = state.updateState.errorText()
    val updateSuccess = state.updateState is ResponseState.Success

    val deleteErrorText = state.deleteState.errorText()
    val deleteSuccess = state.deleteState is ResponseState.Success

    val selectedApps = sharedAppState.apps.filter { it.checked }

    LaunchedEffect(deleteSuccess){
        if (deleteSuccess){
            sharedEvent(PolicySharedEvent.LoadSubscriptionLimit)
        }
    }



    LoadingDialog(loading)

    var showErrorDialog by remember() {
        mutableStateOf(false)
    }
    var showSuccessDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(createErrorText, updateErrorText, deleteErrorText) {
        if (createErrorText.isNotEmpty() || updateErrorText.isNotEmpty() || deleteErrorText.isNotEmpty()) {
            showErrorDialog = true
        }
    }
    LaunchedEffect(createSuccess, updateSuccess, deleteSuccess) {
        if (createSuccess || updateSuccess || deleteSuccess) {
            showSuccessDialog = true
        }
    }

    Logger.d("PolicySetupScreen", "createError=$createErrorText  show=$showErrorDialog")

    cafe.adriel.voyager.navigator.internal.BackHandler(enabled = true) {
        if (
            (state.timeList.isNotEmpty() || state.limitList.isNotEmpty() || state.selectedPackages.isNotEmpty())
            && sharedState.canUpdate
        ) {
            showCloseConfirmDialog = true
        } else {
            navigator?.pop()
        }
    }

    CustomDialogTextField(
        enabled = sharedState.policyTitle.isNotEmpty(),
        show = showDialogEdit,
        title = stringResource(Res.string.jadval_nomini_kiriting),
        value = sharedState.policyTitle,
        onValueChange = { sharedEvent(PolicySharedEvent.SetPolicyTitle(it)) },
        buttonText = stringResource(Res.string.saqlash),
        label = stringResource(Res.string.misol_o_quv_markaz),
        onDismiss = { showDialogEdit = false },
        onButtonClick = {
            showDialogEdit = false
        }
    )

    CustomDialog(
//        lottieAsset = DialogLottie.ERROR,
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = createErrorText.ifEmpty { updateErrorText }.ifEmpty { deleteErrorText },
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showErrorDialog = false
            event(PolicySetupEvent.ResetResponseState)
        },
        onButtonClick = {
            showErrorDialog = false
            event(PolicySetupEvent.ResetResponseState)
        }
    )

    CustomDialog(
//        lottieAsset = DialogLottie.SUCCESS,
        show = showSuccessDialog,
        title = stringResource(Res.string.muvaffaqiyatli),
        message = if (createSuccess) stringResource(Res.string.jadval_muvaffaqiyatli_yaratildi)
        else if (updateSuccess) stringResource(Res.string.jadval_muvaffaqiyatli_tahrirlandi)
        else stringResource(Res.string.jadval_muvaffaqiyatli_o_chirildi),
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showSuccessDialog = false
            event(PolicySetupEvent.ResetResponseState)
        },
        onButtonClick = {
            showSuccessDialog = false
            navigator?.pop()
            event(PolicySetupEvent.ResetResponseState)
        }
    )

    CustomDialog(
        show = showDeleteConfirmDialog,
//        lottieAsset = DialogLottie.WARNING,
        title = stringResource(Res.string.diqqat),
        message = stringResource(Res.string.siz_rostdan_ham_ushbu_jadvalni_o_chirmoqchimisiz),
        buttonText = stringResource(Res.string.ochirish),
        showCloseButton = true,
        onDismiss = {
            showDeleteConfirmDialog = false
        },
        onButtonClick = {
            showDeleteConfirmDialog = false
            event(PolicySetupEvent.DeletePolicy)
        }
    )

    CustomDialog(
        show = showCloseConfirmDialog,
//        lottieAsset = DialogLottie.WARNING,
        title = stringResource(Res.string.diqqat),
        message = stringResource(Res.string.jadvalni_saqlamasdan_chiqishga_rozimisiz),
        buttonText = stringResource(Res.string.roziman).capitalizeFirst(),
        showCloseButton = true,
        onDismiss = {
            showCloseConfirmDialog = false
        },
        onButtonClick = {
            showCloseConfirmDialog = false
            navigator?.pop()
        }
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {
        CustomHeader(
            title = sharedState.policyTitle,
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                if (sharedState.canUpdate){
                    Box(
                        modifier = Modifier
                            .size(NormalIconButtonSize)
                            .clip(RoundedCornerShape(ShapeCornerRadius))
                            .background(MaterialTheme.extendedColor.cardColor)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                showDialogEdit = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.edite_pen_ilne),
                            contentDescription = "Search",
                            tint = MaterialTheme.extendedColor.onBackgroundColor
                        )
                    }
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

            if (state.timeList.isNotEmpty()) {
                val subTitle = if (state.timeList.size == 1) {
                    val timeRule = state.timeList.first()
                    val weekdays = if (timeRule.weekDays.size == 7) {
                        stringResource(Res.string.har_kuni)
                    } else {
                        timeRule.weekDays.map { it.weekdayLabel() }.joinToString(", ")
                    }
                    val time = if (timeRule.allDay) {
                        stringResource(Res.string.kun_davomida)
                    } else {
                        timeRule.time
                    }

                    if (timeRule.outside) {
                        "$weekdays  $time (${stringResource(Res.string.tashqarida)})"
                    } else {
                        "$weekdays  $time"
                    }
                } else {
                    "${state.timeList.size} ${stringResource(Res.string.ta_jadval)}"
                }

                PolicySetupRuleItem(
                    title = stringResource(Res.string.vaqt),
                    subTitle = subTitle,
                    onRemoveClick = {
                        sharedEvent(PolicySharedEvent.SetTimeRule(emptyList()))
                    },
                    onItemClick = {
                        navigator?.push(TimeRuleListScreen())
                    },
                    canRemove = sharedState.canUpdate

                )
                SpaceLarge()
            }

            if (state.limitList.isNotEmpty()) {
                val subTitle = if (state.limitList.size == 1) {
                    val limitRule = state.limitList.first()
                    val weekdays = if (limitRule.weekDays.size == 7) {
                        stringResource(Res.string.har_kuni)
                    } else {
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
                    if (hourMinute.minute > 0) {
                        time.append(hourMinute.minute)
                        time.append(" ")
                        time.append(stringResource(Res.string.daqiqa))
                    }

                    val type =
                        if (limitRule.limitType == DayHour.DAY) stringResource(Res.string.kunlik) else stringResource(
                            Res.string.soatlik
                        )

                    "$weekdays  ${time} \n${type}"
                } else {
                    "${state.limitList.size} ${stringResource(Res.string.ta_jadval)}"
                }

                PolicySetupRuleItem(
                    title = stringResource(Res.string.foydalanish_chegarasi),
                    subTitle = subTitle,
                    onRemoveClick = {
                        sharedEvent(PolicySharedEvent.SetLimitRule(emptyList()))
                    },
                    onItemClick = {
                        navigator?.push(LimitRuleListScreen())
                    },
                    canRemove = sharedState.canUpdate

                )
                SpaceLarge()
            }

            if (sharedState.canUpdate) {
                SpaceMedium()
                CustomOutlinedButton(
                    onClick = {
                        navigator?.push(RuleTypeSelectionScreen())
                    },
                    text = stringResource(Res.string.shartlar_kiritish),
                    endingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.add_square),
                            contentDescription = "",
                            tint = PrimaryColor
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SpaceLarge()



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
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
                    .clickable {
                        navigator?.push(AppWebSelectionScreen())
                    }
                    .tripleShadow(
                        shape = RoundedCornerShape(CardCornerRadius),
                    )
                    .clip(RoundedCornerShape(CardCornerRadius))
                    .background(
                        MaterialTheme.extendedColor.cardColor
                    )
            )
            {

                val statusColor = if (selectedApps.isEmpty()) HintTextColor else PrimaryColor
                val countText = selectedApps.size.toString()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = ContainerPadding,
                            start = ContainerPadding,
                            end = ContainerPadding
                        ),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
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
                if (selectedApps.isNotEmpty()) {
                    SpaceSmall()
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = ContainerPadding,
                        bottom = ContainerPadding,
                        end = ContainerPadding
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                )
                {
                    items(selectedApps) { app ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.extendedColor.backgroundColor,
                                    RoundedCornerShape(CardCornerRadius)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            if (!app.iconUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    modifier = Modifier.size(SmallIconSize)
                                        .clip(RoundedCornerShape(6.dp)),
                                    model = app.iconUrl,
                                    contentDescription = null,
                                    placeholder = painterResource(Res.drawable.ic_launcher_foreground),
                                    error = painterResource(Res.drawable.ic_launcher_foreground),
                                    contentScale = ContentScale.Crop
                                )
                            } else {

                                Image(
                                    painter = painterResource(Res.drawable.ic_launcher_foreground),
                                    modifier = Modifier.size(SmallIconSize)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )


                            }
                            SpaceSmall()
                            CustomText(
                                text = app.name,
                                color = MaterialTheme.extendedColor.textColor,
                                fontSize = SmallTextSize,
                                lineHeight = SmallTextSize
                            )
                        }
                    }

                }
                if (sharedAppState.serverMissingCount > 0) {
                    CustomText(
                        text = stringResource(
                            Res.string.ta_ilova_sizda_o_rnatilmagan,
                            sharedAppState.serverMissingCount
                        ),
                        modifier = Modifier
                            .padding(
                                start = ContainerPadding,
                                end = ContainerPadding,
                                bottom = ContainerPadding
                            ),
                        color = MaterialTheme.extendedColor.hintColor
                    )
                }


            }
            SpaceLarge()
            SpaceLarge()

            if (sharedState.selectedPolicy != null && sharedState.canUpdate) {
                CustomOutlinedButton(
                    text = stringResource(Res.string.ochirish),
                    borderColor = MostImportantButtonColor,
                    textColor = MostImportantButtonColor,
                    modifier = Modifier
                        .fillMaxWidth(),
                    endingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.delete),
                            contentDescription = "",
                            tint = MostImportantButtonColor,
                            modifier = Modifier
                                .size(SmallIconSize)
                        )
                    },
                    onClick = {
                        showDeleteConfirmDialog = true
                    }
                )
                SpaceLarge()
            }


        }

        if (sharedState.canUpdate) {
            CustomButton(
                enabled = (state.limitList.isNotEmpty() || state.timeList.isNotEmpty()) && selectedApps.isNotEmpty(),
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


}

@Preview
@Composable
private fun PreviewTableScreen() {
    TikonchaParentTheme(ThemeMode.LIGHT) {
        PolicySetupUi(
            navigator = null
        )
    }

}