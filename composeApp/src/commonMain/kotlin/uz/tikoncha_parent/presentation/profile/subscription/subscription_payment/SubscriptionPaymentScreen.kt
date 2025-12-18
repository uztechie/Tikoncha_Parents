package uz.tikoncha_parent.presentation.profile.subscription.subscription_payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.base.CustomSelectionButton
import uz.tikoncha_parent.presentation.common.*
import uz.tikoncha_parent.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.*
import uz.saidburxon.newedu.presentation.base.CustomButton
import uz.saidburxon.newedu.presentation.base.CustomText
import uz.tikoncha_parent.domain.model.SubscriptionType
import uz.tikoncha_parent.domain.model.UserInfo
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.LoadingDialog
import uz.tikoncha_parent.presentation.profile.subscription.payment.PaymentTypeScreen
import uz.tikoncha_parent.presentation.ui_state.ResponseState
import uz.tikoncha_parent.presentation.ui_state.errorText
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.TikonchaParentTheme
import uz.tikoncha_parent.ui.theme.extendedColor

class SubscriptionPaymentScreen(val selectedChild: UserInfo? = null) : Screen {
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<SubscriptionPaymentViewModel>()
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

    var showButtonSheetState by remember { mutableStateOf(false) }
    val planLoading = state.subscriptionPlanState is ResponseState.Loading
    val planErrorText = state.subscriptionPlanState.errorText()


    LoadingDialog(planLoading)
    var showPlanErrorDialog by remember() {
        mutableStateOf(false)
    }

    LaunchedEffect(planErrorText) {
        if (planErrorText.isNotEmpty()) {
            showPlanErrorDialog = true
        }
    }

    CustomDialog(
//        lottieAsset = DialogLottie.ERROR,
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





    var showDialog by remember {
        mutableStateOf(false)
    }


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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.extendedColor.backgroundColor)
    ) {

        CustomHeader(
            title = stringResource(Res.string.obuna),
            showBackButton = true,
            onBackClick = {
                navigator?.pop()
            },
            fonWeight = FontWeight.W600,
            trailingIcon = {
                SpaceMedium()

                ChildSelectionButton(
                    modifier = Modifier
                        .widthIn(120.dp, 160.dp),
                    text = state.selectedChild?.name?:"",
                    label = stringResource(Res.string.farzandingizni_tanlang),
                    imageUrl = state.selectedChild?.avatarUrl?:"",
                    onClick = {
                        showDialog = true
                    },
                )
            }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ContainerPadding)
                .verticalScroll(rememberScrollState())
        ) {



            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.extendedColor.cardColor,
                        RoundedCornerShape(CardCornerRadius)
                    )
                    .padding(ContainerPadding),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(NormalIconButtonSize)
                                .clip(RoundedCornerShape(ShapeCornerRadius))
                                .background(MaterialTheme.extendedColor.backgroundColor),
                            contentAlignment = Alignment.Center
                        ) {
                            val painter = when (state.currentPlan) {
                                SubscriptionType.FREE -> {
                                    painterResource(Res.drawable.telegrams_star)
                                }

                                SubscriptionType.PLUS -> {
                                    painterResource(Res.drawable.crown)
                                }

                                SubscriptionType.PRO -> {
                                    painterResource(Res.drawable.crown)
                                }
                            }

                            Icon(
                                painter = painter,
                                contentDescription = "",
                                tint = PrimaryColor,
                                modifier = Modifier
                                    .fillMaxSize(0.6f)
                            )
                        }
                        SpaceSmall()
                        Column {
                            CustomText(
                                text = stringResource(Res.string.xozr_sizning_obunangiz),
                                fontSize = SmallTextSize,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.extendedColor.hintColor
                            )

                            val currentPlan = when (state.currentPlan) {
                                SubscriptionType.FREE -> {
                                    stringResource(Res.string.standart)
                                }

                                SubscriptionType.PLUS -> {
                                    "PLUS"
                                }

                                SubscriptionType.PRO -> {
                                    "PRO"
                                }
                            }

                            CustomText(
                                text = currentPlan,
                                fontSize = NormalTextSize,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryColor
                            )
                        }
                    }

                    SpaceSmall()

                    CustomText(
                        text = stringResource(Res.string.ilovaning_barcha_funksiyalaridan_foydalanish_uchun_pro_versiyasini_sotib_oling),
                        fontSize = SmallTextSize,
                        color = MaterialTheme.extendedColor.hintColor,

                    )
                }
            }

            SpaceMedium()
            CustomText(
                text = stringResource(Res.string.baxtli_foydalanuvchimisiz),
                fontSize = NormalTextSize,
                fontWeight = FontWeight.SemiBold,
            )
            SpaceMedium()
            if (state.subscriptionUi != null) {
                SubscriptionPaymentItem(
                    subscriptionUi = state.subscriptionUi,
                    onClick = {
                        showButtonSheetState = true
                    }
                )
            }
        }
    }
    if (showButtonSheetState && state.subscriptionUi != null) {
        SubscriptionBottomSheet(
            hasSubscription = state.currentPlan == SubscriptionType.PLUS,
            visible = showButtonSheetState,
            subscription = state.subscriptionUi,
            onDismiss = {
                showButtonSheetState = false
            },
            onByClick = { price, tier, planId ->
                showButtonSheetState = false
                navigator?.push(
                    PaymentTypeScreen(
                        amount = price,
                        subDuration = tier,
                        planId = planId
                    )
                )
            }
        )
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