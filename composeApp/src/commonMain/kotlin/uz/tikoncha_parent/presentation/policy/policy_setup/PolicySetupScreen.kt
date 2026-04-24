@file:OptIn(ExperimentalComposeUiApi::class, InternalVoyagerApi::class)

package uz.tikoncha_parent.presentation.policy.policy_setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.internal.BackHandler
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.model.DayHour
import uz.tikoncha_parent.domain.model.GeoType
import uz.tikoncha_parent.domain.model.LocationRule
import uz.tikoncha_parent.domain.model.policy.PolicyAction
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.policy.app_site_selection.AppWebSelectionScreen
import uz.tikoncha_parent.presentation.policy.limit_rule.LimitRuleListScreen
import uz.tikoncha_parent.presentation.policy.rule_type_selection.RuleTypeSelectionScreen
import uz.tikoncha_parent.presentation.policy.time_rule.TimeRuleListScreen
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.domain.model.weekdayLabel
import uz.tikoncha_parent.presentation.base.CustomBottomDialog
import uz.tikoncha_parent.presentation.base.CustomButtonNew
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomTextField
import uz.tikoncha_parent.presentation.base.DashedBorderButton
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.simpleShadow
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.policy.location_rule.LocationRuleScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedState
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.*

class PolicySetupScreen : Screen {



    @Composable
    override fun Content() {

        val viewModel = koinScreenModel<PolicySetupViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        PolicySetupUi(
            state = state,
            event = event,
            sharedState = sharedState,
            sharedEvent = sharedEvent
        )
    }

}

@Composable
fun PolicySetupUi(
    state: PolicySetupState = PolicySetupState(),
    event: (PolicySetupEvent) -> Unit = {},
    sharedState: PolicySharedState,
    sharedEvent: (PolicySharedEvent) -> Unit,
) {

    val navigator: Navigator? = LocalNavigator.current
    val cardColor = AppColors.bg.surface

    var showCloseConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val createErrorText = state.createState.errorText()
    val updateErrorText = state.updateState.errorText()
    val deleteErrorText = state.deleteState.errorText()

    val createSuccess = state.createState is ResponseState.Success
    val updateSuccess = state.updateState is ResponseState.Success
    val deleteSuccess = state.deleteState is ResponseState.Success

    var policyName by rememberSaveable(sharedState.policyTitle) {
        mutableStateOf(sharedState.policyTitle)
    }

    var showPopupMenu by remember { mutableStateOf(false) }
    var showPolicyNameUpdateDialog by remember { mutableStateOf(false) }

    // ── Initial snapshot lock ────────────────
    LaunchedEffect(Unit) {
        sharedEvent(PolicySharedEvent.LockInitialSnapshot)
    }

    // ── Error dialog trigger ─────────────────
    LaunchedEffect(createErrorText, updateErrorText, deleteErrorText) {
        if (createErrorText.isNotEmpty() || updateErrorText.isNotEmpty() || deleteErrorText.isNotEmpty()) {
            showErrorDialog = true
        }
    }

    // ── Success dialog trigger ───────────────
    LaunchedEffect(createSuccess, updateSuccess, deleteSuccess) {
        if (createSuccess || updateSuccess || deleteSuccess) {
            showSuccessDialog = true
        }
    }

    LaunchedEffect(showSuccessDialog) {
        if (showSuccessDialog){
            delay(3000)
            showSuccessDialog = false
            event(PolicySetupEvent.ResetResponseState)
            navigator?.pop()
        }
    }

    // ── Back handler ─────────────────────────
    BackHandler(true) {
        if (sharedState.hasChanges && sharedState.canUpdate) {
            showCloseConfirmDialog = true
        } else {
            navigator?.pop()
        }
    }

    // ── Dialoglar ────────────────────────────
    LoadingDialog(state.isLoading)

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = createErrorText.ifEmpty { updateErrorText }.ifEmpty { deleteErrorText },
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = { showErrorDialog = false },
        onButtonClick = { showErrorDialog = false },
    )

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_success),
        show = showSuccessDialog,
        title = stringResource(Res.string.muvaffaqiyatli),
        message = when {
            createSuccess -> stringResource(Res.string.jadval_muvaffaqiyatli_yaratildi)
            updateSuccess -> stringResource(Res.string.jadval_muvaffaqiyatli_tahrirlandi)
            else -> stringResource(Res.string.jadval_muvaffaqiyatli_o_chirildi)
        },
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
        },
    )

    CustomBottomDialog(
        show = showDeleteConfirmDialog,
        title = stringResource(Res.string.jadval_o_chirilsinmi),
        message = stringResource(Res.string.siz_rostdan_ham_ushbu_jadvalni_o_chirmoqchimisiz),
        confirmButtonText = stringResource(Res.string.ochirish),
        confirmButtonColor = AppColors.action.accentDanger,
        dismissButtonText = stringResource(Res.string.bekor_qilish),
        showCancelButton = true,
        onDismiss = { showDeleteConfirmDialog = false },
        onConfirm = {
            showDeleteConfirmDialog = false
            sharedState.selectedPolicy?.ruleId?.let {
                event(PolicySetupEvent.DeletePolicy(it))
            }
        },
    )

    CustomBottomDialog(
        show = showCloseConfirmDialog,
        title = stringResource(Res.string.chiqmoqchimisiz),
        message = stringResource(Res.string.kiritilgan_ma_lumotlar_saqlanmaydi),
        confirmButtonText = stringResource(Res.string.chiqish),
        confirmButtonColor = AppColors.button.primary,
        dismissButtonText = stringResource(Res.string.qolish),
        showCancelButton = true,
        onDismiss = { showCloseConfirmDialog = false },
        onConfirm = {
            showCloseConfirmDialog = false
            navigator?.pop()
        },
    )


    PolicyNameDialog(
        policyName = sharedState.policyTitle,
        show = showPolicyNameUpdateDialog,
        onDismiss = {showPolicyNameUpdateDialog = false},
        onButtonClick = {
            sharedEvent(PolicySharedEvent.SetPolicyTitle(it))
            showPolicyNameUpdateDialog = false
        },
    )

    val systemBars = rememberScreenSystemBars(
        statusBarColor = AppColors.bg.secondary,
        navigationBarColor = AppColors.bg.elevated
    )

    // ── Asosiy content ───────────────────────
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(systemBars.modifier)
            .background(AppColors.bg.secondary),
    ) {
        CustomHeader(
            title = if (sharedState.isEditMode) sharedState.policyTitle
            else stringResource(Res.string.jadval_yaratish),
            modifier = Modifier
                .fillMaxWidth(),
            showBackButton = true,
            onBackClick = { navigator?.pop() },
            trailingIcon = {
                if (sharedState.isEditMode && sharedState.canUpdate){
                    Box(){
                        IconButton(
                            onClick = {
                                showPopupMenu = !showPopupMenu
                            },
                            modifier = Modifier
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                        DropdownMenu(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp)),
                            expanded = showPopupMenu,
                            onDismissRequest = {showPopupMenu = false},
                            shape = RoundedCornerShape(24.dp),
                            containerColor = AppColors.modal.primary,

                            ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(Res.string.tahrirlash),
                                        style = AppTypography.titleSmMedium,
                                        color = AppColors.text.primary
                                    )
                                },
                                onClick = {
                                    showPolicyNameUpdateDialog = true
                                    showPopupMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.edite_pen_ilne),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp),
                                        tint = AppColors.icon.primary
                                    )
                                }

                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(Res.string.ochirish),
                                        style = AppTypography.titleSmMedium,
                                        color = AppColors.text.primary
                                    )
                                },
                                onClick = {
                                    showDeleteConfirmDialog = true
                                    showPopupMenu = false
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(Res.drawable.message_delete),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(20.dp),
                                        tint = AppColors.icon.primary
                                    )
                                }

                            )
                        }
                    }
                }

            }
        )
        Space(20.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── Jadval nomi ──────────────────

            if (!sharedState.isEditMode && sharedState.canUpdate) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .simpleShadow(shape = RoundedCornerShape(20.dp))
                        .background(cardColor, RoundedCornerShape(20.dp))
                        .padding(ContainerPadding),
                ) {
                    Text(
                        text = stringResource(Res.string.jadval_nomi),
                        style = AppTypography.titleLgSemiBold,
                        color = AppColors.text.primary,
                    )
                    SpaceSmall()
                    CustomTextField(
                        label = stringResource(Res.string.jadval_nomini_kiriting),
                        enabled = sharedState.canUpdate,
                        value = policyName,
                        onValueChange = { newValue ->
                            policyName = newValue
                            sharedEvent(PolicySharedEvent.SetPolicyTitle(newValue))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(TextFieldHeight),
                        containerColor = AppColors.field.page,
                        shape = RoundedCornerShape(20.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }




            // ── Shartlar ─────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .simpleShadow(shape = RoundedCornerShape(20.dp))
                    .background(cardColor, RoundedCornerShape(20.dp))
                    .padding(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.shartlar),
                    style = AppTypography.titleLgSemiBold,
                    color = AppColors.text.primary,
                )

                if (sharedState.limitList.isNotEmpty() || sharedState.timeList.isNotEmpty() || sharedState.locationRule != null){
                    Space(12.dp)
                }

                // Time rule
                if (sharedState.timeList.isNotEmpty()) {
                    val subTitle = buildTimeRuleSubtitle(sharedState)
                    PolicySetupRuleItem(
                        title = stringResource(Res.string.vaqt),
                        painter = painterResource(Res.drawable.time_square),
                        subTitle = subTitle,
                        canRemove = sharedState.canUpdate,
                        onRemoveClick = {
                            sharedEvent(PolicySharedEvent.SetTimeRule(emptyList()))
                        },
                        onItemClick = {
                            navigator?.push(TimeRuleListScreen())
                        },
                    )
                    Space(10.dp)
                }

                // Limit rule
                if (sharedState.limitList.isNotEmpty()) {
                    val subTitle = buildLimitRuleSubtitle(sharedState)
                    PolicySetupRuleItem(
                        title = stringResource(Res.string.foydalanish_chegarasi),
                        painter = painterResource(Res.drawable.per_time_enabled),
                        subTitle = subTitle,
                        canRemove = sharedState.canUpdate,
                        onRemoveClick = {
                            sharedEvent(PolicySharedEvent.SetLimitRule(emptyList()))
                        },
                        onItemClick = {
                            navigator?.push(LimitRuleListScreen())
                        },
                    )
                    Space(10.dp)
                }

                // Location rule
                if (sharedState.locationRule != null) {
                    val subTitle = if (sharedState.locationRule.reverse)
                        stringResource(Res.string.belgilangan_hududdan_tashqarida)
                    else stringResource(Res.string.belgilangan_hudud_ichida)

                    PolicySetupRuleItem(
                        title = stringResource(Res.string.joylashuv),
                        painter = painterResource(Res.drawable.per_location_enable),
                        subTitle = subTitle,
                        canRemove = sharedState.canUpdate,
                        onRemoveClick = {
                            sharedEvent(PolicySharedEvent.SetLocationRule(null))
                        },
                        onItemClick = {
                            navigator?.push(LocationRuleScreen())
                        },
                    )
                }

                // Shart qo'shish tugmasi
                if (sharedState.showAddRuleButton) {
                    Space(12.dp)
                    DashedBorderButton(
                        text = stringResource(Res.string.shartlar_kiritish),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DialogButtonHeight),
                        onClick = {
                            navigator?.push(RuleTypeSelectionScreen())
                                  },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = AppColors.text.accentEmphasis,
                            )
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(Res.string.bloklash_rejimi),
                    style = AppTypography.titleLgSemiBold,
                    color = AppColors.text.primary,
                    modifier = Modifier.weight(1f),
                )
                // Action badge
                val actionText = if (sharedState.policyAction == PolicyAction.DENY)
                    stringResource(Res.string.qora_ro_yxat)
                else stringResource(Res.string.oq_royhat)

                val actionIcon = if (sharedState.policyAction == PolicyAction.DENY)
                    painterResource(Res.drawable.blocklist)
                else
                    painterResource(Res.drawable.whitelist)

                Row(
                    modifier = Modifier
                        .singleClick {
                            if (sharedState.canUpdate) {
                                navigator?.push(PolicyActionScreen())
                            }
                        }
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = actionIcon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(20.dp),
                        tint = AppColors.icon.accentPrimary
                    )
                    Space(4.dp)
                    Text(
                        text = actionText,
                        style = AppTypography.titleLgSemiBold,
                        color = AppColors.text.accentEmphasis
                    )
                    Space(4.dp)

                    Icon(
                        painter = painterResource(Res.drawable.arrow_down_reg),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColors.icon.accentPrimary,
                    )
                }
            }
            Space(12.dp)
            Text(
                text = stringResource(Res.string.bloklamoqchi_bo_lgan_ilova_yoki_saytlarni_tanlang),
                style = AppTypography.bodyLgMedium,
                color = AppColors.text.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
            )
            Spacer(Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .simpleShadow(shape = RoundedCornerShape(20.dp))
                    .background(AppColors.bg.surface, RoundedCornerShape(20.dp))
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                        .singleClick {
                            navigator?.push(AppWebSelectionScreen(0))
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.ilovalar),
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "${sharedState.selectedPkgs.size}",
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                    )
                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right_rounded),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColors.icon.secondary,
                    )
                }

                HorizontalDivider(
                    color = AppColors.border.tertiarySubtle,
                    thickness = 1.dp
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                        .singleClick {
                            navigator?.push(AppWebSelectionScreen(0))
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.kategoriyalar),
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "${sharedState.selectedCategories.size}",
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                    )
                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right_rounded),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColors.icon.secondary,
                    )
                }

                HorizontalDivider(
                    color = AppColors.border.tertiarySubtle,
                    thickness = 1.dp
                )


                // Saytlar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .padding(horizontal = 16.dp)
                        .singleClick {
                            navigator?.push(AppWebSelectionScreen(1))
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(Res.string.saytlar),
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = "${sharedState.selectedSites.size}",
                        style = AppTypography.titleSmMedium,
                        color = AppColors.text.primary,
                    )
                    SpaceUltraSmall()
                    Icon(
                        painter = painterResource(Res.drawable.arrow_right_rounded),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = AppColors.icon.secondary,
                    )
                }
            }

            SpaceLarge()
        }


        // ── Saqlash tugmasi ──────────────────
        if (sharedState.canUpdate) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AppColors.bg.elevated, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ){
                CustomButtonNew(
                    enabled = sharedState.canSavePolicy,
                    text = stringResource(Res.string.saqlash),
                    onClick = { event(PolicySetupEvent.SavePolicy(sharedState)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }

        }
    }
}

// ── Subtitle helpers ─────────────────────────

@Composable
private fun buildTimeRuleSubtitle(sharedState: PolicySharedState): String {
    return if (sharedState.timeList.size == 1) {
        val rule = sharedState.timeList.first()
        val weekdays = if (rule.weekDays.size == 7) stringResource(Res.string.har_kuni)
        else rule.weekDays.map { it.weekdayLabel() }.joinToString(", ")

        val time = if (rule.allDay) {
            stringResource(Res.string.kun_davomida)
        } else {
            val sh = rule.startTime.hour.toString().padStart(2, '0')
            val sm = rule.startTime.minute.toString().padStart(2, '0')
            val eh = rule.endTime.hour.toString().padStart(2, '0')
            val em = rule.endTime.minute.toString().padStart(2, '0')
            "$sh:$sm - $eh:$em"
        }

        if (rule.reverse) "$weekdays  $time (${stringResource(Res.string.tashqarida)})"
        else "$weekdays  $time"
    } else {
        "${sharedState.timeList.size} ${stringResource(Res.string.ta_jadval)}"
    }
}

@Composable
private fun buildLimitRuleSubtitle(sharedState: PolicySharedState): String {
    return if (sharedState.limitList.size == 1) {
        val rule = sharedState.limitList.first()
        val weekdays = if (rule.weekDays.size == 7) stringResource(Res.string.har_kuni)
        else rule.weekDays.map { it.weekdayLabel() }.joinToString(", ")

        val time = buildString {
            if (rule.time.hour > 0) append("${rule.time.hour} ${stringResource(Res.string.soat)}, ")
            if (rule.time.minute > 0) append("${rule.time.minute} ${stringResource(Res.string.daqiqa)}")
        }
        val type = if (rule.limitType == DayHour.DAY) stringResource(Res.string.kunlik)
        else stringResource(Res.string.soatlik)

        "$weekdays  $time\n$type"
    } else {
        "${sharedState.limitList.size} ${stringResource(Res.string.ta_jadval)}"
    }
}

@Preview
@Composable
private fun PreviewTableScreen() {
    TikonchaParentTheme(ThemeMode.DARK) {
        PolicySetupUi(
            state = PolicySetupState(),
            event = {},
            sharedState = PolicySharedState(
                locationRule = LocationRule(
                    geoType = GeoType.CIRCLE,
                    reverse = false
                )
            ),
            sharedEvent = {}
        )
    }

}