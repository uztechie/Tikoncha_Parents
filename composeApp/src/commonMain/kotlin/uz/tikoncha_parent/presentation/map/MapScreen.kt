@file:OptIn(InternalVoyagerApi::class, ExperimentalVoyagerApi::class)

package uz.tikoncha_parent.presentation.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.DefaultScreenLifecycleOwner.onDispose
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import qrgenerator.qrkitpainter.event
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left
import tikoncha_parents.composeapp.generated.resources.dialog_failed
import tikoncha_parents.composeapp.generated.resources.dialog_info
import tikoncha_parents.composeapp.generated.resources.farzandingizni_tanlang
import tikoncha_parents.composeapp.generated.resources.farzandlaringiz
import tikoncha_parents.composeapp.generated.resources.gps_o_chirilgan
import tikoncha_parents.composeapp.generated.resources.joylashuv_uchun_ruxsat
import tikoncha_parents.composeapp.generated.resources.siz
import tikoncha_parents.composeapp.generated.resources.sozlamalar
import tikoncha_parents.composeapp.generated.resources.xarita
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_gps_ni_yoqing
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_joylashuvga_sozlamalardan_turib_ruxsat_bering
import tikoncha_parents.composeapp.generated.resources.yoqish
import uz.tikoncha_parent.data.local.AppSettings
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.platform.KmpWebViewController
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.UniversalJsonWebView
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.ChildSelectionButton
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
import uz.tikoncha_parent.presentation.common.CustomListDialog
import uz.tikoncha_parent.presentation.profile.subscription.subscription_payment.SubscriptionPaymentScreen
import uz.tikoncha_parent.ui.ContainerPadding
import uz.tikoncha_parent.ui.NormalIconButtonPadding
import uz.tikoncha_parent.ui.NormalIconButtonSize
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.ThemePrefs
import uz.tikoncha_parent.ui.theme.extendedColor

class MapScreen : Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        // -------------------- THEME --------------------
        val themeMode by rememberSaveable { mutableStateOf(ThemePrefs.load()) }
        val darkTheme = when (themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        // -------------------- PERMISSION + TRACKER --------------------
        val permissionsFactory = rememberPermissionsControllerFactory()
        val permissionsController = remember(permissionsFactory) {
            permissionsFactory.createPermissionsController()
        }

        val locationTracker = rememberLocationTrackerFactory(
            accuracy = LocationTrackerAccuracy.Best
        ).createLocationTracker(
            permissionsController = permissionsController
        )

        val useCase: ChildrenLocationUseCase = getKoin().get()

        // LocationViewModel ni AYNAN shu tracker bilan yaratamiz
        val locationViewModel: LocationViewModel = viewModel {
            LocationViewModel(
                tracker = locationTracker,
                childrenLocationUseCase = useCase
            )
        }

        val scope = rememberCoroutineScope()

        // PermissionsController’ni lifecycle bilan bog‘laymiz
        BindEffect(permissionsController)

        // Sen yozgan PermissionViewModel (mavjud deb hisoblaymiz)
        val permissionViewModel = viewModel {
            PermissionViewModel(permissionsController)
        }
        val permissionState by permissionViewModel.state.collectAsStateWithLifecycle()

        var cameFromSettings by remember { mutableStateOf(false) }

        // Tracker lifecycle-ni Compose bilan bog‘laymiz
        BindLocationTrackerEffect(locationTracker = locationViewModel.tracker)

        val locationState by locationViewModel.state.collectAsStateWithLifecycle()
        val locationEvent = locationViewModel::onEvent

        // -------------------- DIALOG HOLATLARI --------------------
        var showGpsDialog by remember { mutableStateOf(false) }
        var showPermissionDialog by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {

            onDispose{
                locationViewModel.stop()
            }
        }






        // Joylashuv permission deny always bo‘lsa
        CustomDialog(
            painter = painterResource(Res.drawable.dialog_info),
            show = showPermissionDialog,
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

        // Ekranga kirganimizda permission so‘raymiz
        LaunchedEffect(Unit) {
            permissionViewModel.requestPermission()
        }

        // Permission state o‘zgarganda reaksiya
        LaunchedEffect(permissionState) {
            when (permissionState) {
                PermissionState.Granted -> {
                    // 1) GPS yoqilgan-yoqilmaganini tekshiramiz
                    scope.launch(Dispatchers.Default) {
                        val gpsEnabled = isLocationServiceEnabled()
                        if (!gpsEnabled) {
                            showGpsDialog = true
                        } else {
                            // 2) GPS bor, permission bor -> Location tracker’ni BOSHLAYMIZ
                            locationViewModel.checkGPS()
                        }
                    }
                }

                PermissionState.DeniedAlways -> {
                    showPermissionDialog = true
                }

                else -> {
                    // boshqa holatlar uchun hozircha hech narsa qilmadik
                }
            }
        }

        // -------------------- JSON PAYLOAD --------------------
        val parentLat = locationState.locationData?.latitude
        val parentLng = locationState.locationData?.longitude

        val payload = ParentLocationPayload(
            name = stringResource(Res.string.siz),
            lat = parentLat,
            lng = parentLng,
            children = locationState.childrenLocationList.toPayloads()
        )

        var jsonString by remember {
            mutableStateOf(Json.encodeToString(payload))
        }

        LaunchedEffect(payload) {
            jsonString = Json.encodeToString(payload)
        }

        var webController by remember { mutableStateOf<KmpWebViewController?>(null) }

        LaunchedEffect(jsonString) {
            // Har safar parent/children location o‘zgarsa json qayta yuboriladi
            webController?.postJson(jsonString)
        }


        var pendingChildId by remember { mutableStateOf<String?>(null) }
        var pendingNav by remember { mutableStateOf(false) } // duplicate push oldini oladi

        LaunchedEffect(pendingNav, pendingChildId) {
            if (pendingNav && !pendingChildId.isNullOrBlank()) {
                val childId = pendingChildId!!
                val child = AppSettings.children.find { it.userId == childId }
                yield()
                navigator?.push(SubscriptionPaymentScreen(child))
                pendingNav = false
                pendingChildId = null

            }
        }

        // -------------------- UI --------------------
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Logger.d("MapScreen", "jsonString=$jsonString")


            UniversalJsonWebView(
                url = "https://tikoncha.uz/map/location/",
//                url = "https://yandex.uz/maps/10329/andijan/?ll=72.349754%2C40.777180&z=15.64",
                json = jsonString,
                onIncomingJson = { incomingJson->
                    val childId = extractChildIdOrEmpty(incomingJson)
                    Logger.d("MapScreen", "incoming json=$incomingJson, childId=$childId")

                    if (childId.isNotBlank()) {
                        pendingChildId = childId
                        pendingNav = true
                    }
                },
                onBackPressed = {
                    navigator?.pop()
                }
            )

        }
    }
}

fun extractChildIdOrEmpty(json: String?): String {
    json?:return ""
    return runCatching {
        Json.parseToJsonElement(json)
            .jsonObject["child_user_id"]
            ?.jsonPrimitive
            ?.content
    }.getOrNull().orEmpty()
}
