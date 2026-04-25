package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.common.*
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.model.SubscriptionDuration
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.CustomButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.base.singleClick
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentTypeScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.AppColors
import uz.tikoncha_parent.ui.theme.AppTypography
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme

class SubscriptionPaymentScreen(val selectedChild: UserInfo? = null) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinScreenModel<SubscriptionPaymentViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        LaunchedEffect(selectedChild){
            if (selectedChild != null){
                Logger.d("SubscriptionPaymentScreen", "selectedChild=$selectedChild")
                event(SubscriptionPaymentEvent.SetSelectedChild(selectedChild))
            }
        }

        SubscriptionPaymentUi(
            navigator = navigator,
            state = state,
            event = event
        )
    }
}

@Composable
fun SubscriptionPaymentUi(
    navigator: Navigator?,
    state: SubscriptionPaymentState = SubscriptionPaymentState(),
    event: (SubscriptionPaymentEvent) -> Unit,

) {
    val planLoading = state.subscriptionPlanState is ResponseState.Loading
    val planErrorText = state.subscriptionPlanState.errorText()
    var showPlanErrorDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LoadingDialog(planLoading)

    LaunchedEffect(planErrorText) {
        if (planErrorText.isNotEmpty()) {
            showPlanErrorDialog = true
        }
    }

    CustomDialog(
        painter = painterResource(Res.drawable.dialog_failed),
        show = showPlanErrorDialog,
        title = stringResource(Res.string.xatolik),
        message = planErrorText,
        buttonText = stringResource(Res.string.ok),
        showCloseButton = false,
        onDismiss = {
            showPlanErrorDialog = false
            event(SubscriptionPaymentEvent.ResetResponseState)
        },
        onButtonClick = {
            showPlanErrorDialog = false
            event(SubscriptionPaymentEvent.ResetResponseState)
        }
    )

    CustomListDialog(
        title = stringResource(Res.string.farzandingiz),
        items = state.children,
        show = showDialog,
        onItemSelected = { child ->
            event(SubscriptionPaymentEvent.SetSelectedChild(child))
        },
        onDismiss = {
            showDialog = false
        }
    )

    var selectedPlan by rememberSaveable { mutableIntStateOf(0) } // 0 = Yillik, 1 = Oylik

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFC3955B),
            Color(0xFF291E0F),
        )
    )

    val subscription = state.subscriptionUi
    val hasSubscription = state.currentPlan != SubscriptionType.FREE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = bgGradient)
    ) {
        // ── Scrollable content ─────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            // Close button
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .singleClick {
                        navigator?.pop()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.close_remove),
                    contentDescription = "Close",
                    tint = AppColors.icon.inverse,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.tikoncha_plus_new),
                    contentDescription = "Tikoncha mascot",
                    modifier = Modifier.size(140.dp)
                )
                Space(11.dp)

                Image(
                    painter = painterResource(Res.drawable.subskription_tikoncha_plus),
                    contentDescription = "Tikoncha PLUS",
                    modifier = Modifier
                        .width(188.dp)
                        .height(42.dp)
                )
                Space(12.dp)

                Text(
                    text = stringResource(Res.string.farzand_nazorat_tavsifi),
                    fontSize = 14.sp,
                    color = AppColors.text.inverse.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            Space(27.dp)

            // ── Pricing cards ──────────────────────────────
            if (subscription != null) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubscriptionPlanCard(
                        title = stringResource(Res.string.yillik),
                        pricePerMonth = subscription.annual.price / 12,
                        totalPrice = subscription.annual.price,
                        badgeText = stringResource(Res.string.eng_foydali_tanlov),
                        isSelected = selectedPlan == 0,
                        onClick = { selectedPlan = 0 }
                    )

                    SubscriptionPlanCard(
                        title = stringResource(Res.string.oylik),
                        pricePerMonth = subscription.monthly.price,
                        totalPrice = null,
                        badgeText = null,
                        isSelected = selectedPlan == 1,
                        onClick = { selectedPlan = 1 }
                    )
                }
            }
            Space(16.dp)

            // ── Feature list ───────────────────────────────
            if (subscription != null) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(Color.White.copy(0.05f), RoundedCornerShape(24.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    SubscriptionFeatureItem(
                        iconRes = Res.drawable.chart_sub,
                        title = stringResource(Res.string.toliq_nazorat_statistika),
                        description = stringResource(Res.string.farzand_ilova_kuzatish)
                    )

                    SubscriptionFeatureItem(
                        iconRes = Res.drawable.layers_sub,
                        title = stringResource(Res.string.uchtagacha_jadval),
                        description = stringResource(Res.string.jadval_cheklovlar)
                    )

                    SubscriptionFeatureItem(
                        iconRes = Res.drawable.circle_star,
                        title = stringResource(Res.string.qattiq_bloklash_qalqon),
                        description = stringResource(Res.string.farzand_cheklov_ozgartira_olmaydi)
                    )

                    SubscriptionFeatureItem(
                        iconRes = Res.drawable.lock_sub,
                        title = stringResource(Res.string.tez_bloklash_timer),
                        description = stringResource(Res.string.vaqtinchalik_bloklash_tezi)
                    )

                    SubscriptionFeatureItem(
                        iconRes = Res.drawable.attach_sub,
                        title = stringResource(Res.string.hisobot_tahlil),
                        description = stringResource(Res.string.foydalanish_tahlil)
                    )
                }
            }
            Space(24.dp)
            Space(130.dp)
        }

        // ── Bottom button ──────────────────────────────────
        val bottomGradient = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                Color(0xFF141A15).copy(alpha = 0.3f),
                Color(0xFF141A15).copy(alpha = 0.7f),
                Color(0xFF141A15).copy(alpha = 0.95f),
                Color(0xFF141A15),
            )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(bottomGradient)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 18.dp)
                .padding(top = 13.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                enabled = !hasSubscription && subscription != null,
                text = if (hasSubscription) stringResource(Res.string.obuna_faollashtirilgan)
                else stringResource(Res.string.obuna_bolish),
                onClick = {
                    subscription?.let { sub ->
                        val plan = if (selectedPlan == 0) sub.annual else sub.monthly
                        val duration = if (selectedPlan == 0)
                            SubscriptionDuration.ANNUAL
                        else
                            SubscriptionDuration.MONTHLY

                        navigator?.push(
                            PaymentTypeScreen(
                                amount = plan.price,
                                subDuration = duration,
                                planId = sub.planId
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Space(12.dp)

            Text(
                text = stringResource(Res.string.istalgan_vaqtda_bekor),
                style = AppTypography.bodyMdMedium,
                color = AppColors.text.inverse,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun PreviewSubscriptionScreen() {
    TikonchaParentTheme(
        ThemeMode.LIGHT,
    ){
        SubscriptionPaymentUi(
            navigator = null,
            state = SubscriptionPaymentState(),
            event = {}
        )
    }
}