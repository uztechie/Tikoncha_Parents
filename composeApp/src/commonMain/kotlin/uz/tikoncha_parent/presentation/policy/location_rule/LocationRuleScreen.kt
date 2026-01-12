package uz.tikoncha_parent.presentation.policy.location_rule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.bekor_qilish
import tikoncha_parents.composeapp.generated.resources.dialog_info
import tikoncha_parents.composeapp.generated.resources.gps_o_chirilgan
import tikoncha_parents.composeapp.generated.resources.joylashuv_uchun_ruxsat
import tikoncha_parents.composeapp.generated.resources.location_permission_request_message
import tikoncha_parents.composeapp.generated.resources.ruxsat_berish
import tikoncha_parents.composeapp.generated.resources.sozlamalar
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_gps_ni_yoqing
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_joylashuvga_sozlamalardan_turib_ruxsat_bering
import tikoncha_parents.composeapp.generated.resources.yoqish
import uz.tikoncha_parent.domain.model.LocationData
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.UniversalJsonWebView
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.map.LocationViewModel
import uz.tikoncha_parent.presentation.map.OnScreenActive
import uz.tikoncha_parent.presentation.map.PermissionViewModel
import uz.tikoncha_parent.presentation.policy.policy_setup.PolicySetupScreen
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedEvent
import uz.tikoncha_parent.presentation.policy.shared.PolicySharedModel
import uz.tikoncha_parent.ui.theme.rememberIsDarkTheme


class LocationRuleScreen: Screen {
    @Composable
    override fun Content() {

        val permissionsFactory = rememberPermissionsControllerFactory()
        val permissionsController = remember(permissionsFactory) {
            permissionsFactory.createPermissionsController()
        }


        val locationTracker = rememberLocationTrackerFactory(
            accuracy = LocationTrackerAccuracy.Best
        ).createLocationTracker(
            permissionsController = permissionsController
        )



        val locationViewModel: LocationViewModel = viewModel {
            LocationViewModel(
                tracker = locationTracker,
                childrenLocationUseCase = null
            )
        }

        val scope = rememberCoroutineScope()
        BindEffect(permissionsController)


        val permissionViewModel = viewModel {
            PermissionViewModel(permissionsController)
        }
        val permissionState by permissionViewModel.state.collectAsState()

        var cameFromSettings by remember { mutableStateOf(false) }

        // Tracker lifecycle-ni Compose bilan bog‘laymiz
        BindLocationTrackerEffect(locationTracker = locationViewModel.tracker)

        val locationState by locationViewModel.state.collectAsState()
        val locationEvent = locationViewModel::onEvent

        // -------------------- DIALOG HOLATLARI --------------------
        var showPermissionConfirmDialog by remember { mutableStateOf(false) }
        var showGpsDialog by remember { mutableStateOf(false) }
        var showPermissionDialog by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {

            onDispose{
                locationViewModel.stop()
            }
        }


        CustomDialog(
            painter = painterResource(Res.drawable.dialog_info),
            show = showPermissionConfirmDialog,
            showCloseButton = true,
            title = stringResource(Res.string.joylashuv_uchun_ruxsat),
            message = stringResource(Res.string.location_permission_request_message),
            buttonText = stringResource(Res.string.ruxsat_berish),
            buttonText2 = stringResource(Res.string.bekor_qilish),
            onDismiss = {
                showPermissionConfirmDialog = false
            },
            onButtonClick = {
                showPermissionConfirmDialog = false
                permissionViewModel.requestPermission()
            }
        )



        // Joylashuv permission deny always bo‘lsa
        CustomDialog(
            painter = painterResource(Res.drawable.dialog_info),
            show = showPermissionDialog,
            showCloseButton = true,
            title = stringResource(Res.string.joylashuv_uchun_ruxsat),
            message = stringResource(Res.string.xaritadan_to_liq_foydalanish_uchun_joylashuvga_sozlamalardan_turib_ruxsat_bering),
            buttonText = stringResource(Res.string.sozlamalar),
            onDismiss = {
                showPermissionDialog = false
            },
            onButtonClick = {
                showPermissionDialog = false
                cameFromSettings = true
                permissionsController.openAppSettings()
            }
        )

        // GPS o‘chiq bo‘lsa
        CustomDialog(
            painter = painterResource(Res.drawable.dialog_info),
            show = showGpsDialog,
            showCloseButton = true,
            title = stringResource(Res.string.gps_o_chirilgan),
            message = stringResource(Res.string.xaritadan_to_liq_foydalanish_uchun_gps_ni_yoqing),
            buttonText = stringResource(Res.string.yoqish),
            onDismiss = {
                showGpsDialog = false
            },
            onButtonClick = {
                showGpsDialog = false
                cameFromSettings = true
                openLocationSettings()
            }
        )

        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                permissionViewModel.refresh()
                cameFromSettings = false
            }
        )


        LaunchedEffect(permissionState, cameFromSettings) {
            Logger.d("LocationViewmodel", "permissionState=$permissionState")
            when (permissionState) {
                PermissionState.Granted -> {
                    val gpsEnabled = kotlinx.coroutines.withContext(Dispatchers.Default) {
                        isLocationServiceEnabled()
                    }
                    if (!gpsEnabled) {
                        showGpsDialog = true
                    } else {
                        showGpsDialog = false
                        locationViewModel.checkGPS()
                    }
                }

                PermissionState.Denied, PermissionState.NotGranted, PermissionState.NotDetermined -> {
                    if (!showPermissionConfirmDialog) {
                        showPermissionConfirmDialog = true
                    }
                }

                PermissionState.DeniedAlways -> {
                    if (!showPermissionDialog) {
                        showPermissionDialog = true
                    }
                }

                else -> Unit
            }
        }





        val viewModel = koinScreenModel<LocationRuleViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent

        val sharedViewModel = koinViewModel<PolicySharedModel>()
        val sharedEvent = sharedViewModel::onEvent
        val sharedState by sharedViewModel.state.collectAsStateWithLifecycle()

        val isDark = rememberIsDarkTheme()


        LaunchedEffect(locationState.locationData) {
            val locationData: LocationData? =
                locationState.locationData?.let { data ->
                    LocationData(
                        lat = data.latitude,
                        lng = data.longitude
                    )
                }

            event(
                LocationRuleEvent.SetLocation(
                    locationRule = sharedState.locationRule,
                    editable = sharedState.canUpdate,
                    policyName = sharedState.policyTitle,
                    isDark = isDark,
                    locationData = locationData
                )
            )
        }



        LocationRuleUi(
            state = state,
            event = event,
            sharedEvent = sharedEvent
        )


    }

}

@Composable
fun LocationRuleUi(
    state: LocationRuleState,
    event: (LocationRuleEvent) -> Unit,
    sharedEvent: (PolicySharedEvent) -> Unit
) {
    val navigator = LocalNavigator.current


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        UniversalJsonWebView(
            url = "https://tikoncha.uz/map/location/student",
            json = state.ruleJson,
            onIncomingJson = {
                Logger.d("LocationRuleScreen", "incoming json: $it")
                val json = it?:""
                val locationRuleUi = Json.decodeFromString<LocationRuleUi>(json)
                sharedEvent(PolicySharedEvent.SetLocationRule(locationRuleUi.location_rule))

                navigator?.popUntil {
                    it is PolicySetupScreen
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            onBackPressed = {
                navigator?.pop()
            }
        )

    }



}

@Preview
@Composable
private fun Pre() {
    LocationRuleUi(
        state = LocationRuleState(),
        event = {},
        sharedEvent = {}
    )
}