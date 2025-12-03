package uz.tikoncha_parent.presentation.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.arrow_left
import tikoncha_parents.composeapp.generated.resources.gps_o_chirilgan
import tikoncha_parents.composeapp.generated.resources.joylashuv_uchun_ruxsat
import tikoncha_parents.composeapp.generated.resources.siz
import tikoncha_parents.composeapp.generated.resources.sozlamalar
import tikoncha_parents.composeapp.generated.resources.xarita
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_gps_ni_yoqing
import tikoncha_parents.composeapp.generated.resources.xaritadan_to_liq_foydalanish_uchun_joylashuvga_sozlamalardan_turib_ruxsat_bering
import tikoncha_parents.composeapp.generated.resources.yoqish
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase
import uz.tikoncha_parent.platform.KmpWebView
import uz.tikoncha_parent.platform.KmpWebViewController
import uz.tikoncha_parent.platform.Logger
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.presentation.base.CustomHeader
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

        val json = """
{
  "name": "Siz",
  "lat": null,
  "lng": null,
  "children": []
}
""".trimIndent()

        val jsonDone = """
{
  "name": "Siz",
  "lat": null,
  "lng": null,
  "children": [
    {
      "name": "Ali",
      "lat": 40.7900,
      "lng": 72.3500
    },
    {
      "name": "Bek",
      "lat": 40.7750,
      "lng": 72.3300
    }
  ]
}
""".trimIndent()


        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }


        val locationTracker = rememberLocationTrackerFactory(
            accuracy = LocationTrackerAccuracy.Best
        ).createLocationTracker(permissionsController = controller)

        val useCase: ChildrenLocationUseCase = getKoin().get()

        val locationViewModel = viewModel {
            LocationViewModel(
                tracker = locationTracker,
                useCase
            )
        }


        val themeMode by rememberSaveable { mutableStateOf(ThemePrefs.load()) }
        val darkTheme = when (themeMode) {
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        val scope = rememberCoroutineScope()
        BindEffect(controller)
        val permissionViewModel = viewModel {
            PermissionViewModel(controller)
        }
        val permissionState by permissionViewModel.state.collectAsStateWithLifecycle()
        var cameFromSettings by remember { mutableStateOf(false) }

        BindLocationTrackerEffect(locationViewModel.tracker)
        val locationState by locationViewModel.state.collectAsStateWithLifecycle()

        var showGpsDialog by remember {
            mutableStateOf(false)
        }

        var showPermissionDialog by remember {
            mutableStateOf(false)
        }

        CustomDialog(
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
                controller.openAppSettings()
            }
        )
        CustomDialog(
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
                permissionViewModel.refresh();
                cameFromSettings = false
            }
        )

        LaunchedEffect(Unit) {
            permissionViewModel.requestPermission()
        }

        LaunchedEffect(permissionState) {
            when (permissionState) {
                PermissionState.Granted -> {
//                    locationViewModel.checkGPS()
                    scope.launch(Dispatchers.Default) {
                        showGpsDialog = !isLocationServiceEnabled()
                    }
                }

                PermissionState.DeniedAlways -> {
                    showPermissionDialog = true
                }

                else -> {

                }
            }
        }

        val parentLat = locationState.locationData?.latitude
        val parentLng = locationState.locationData?.longitude

        val payload = ParentLocationPayload(
            name = stringResource(Res.string.siz),
            lat = parentLat,
            lng = parentLng,
            children = locationState.childrenLocationList.map { it.toPayload() }
        )

        var jsonString by remember {
            mutableStateOf(
                Json.encodeToString(payload)
            )
        }

        LaunchedEffect(payload){
            jsonString = Json.encodeToString(payload)
        }

        var webController by remember { mutableStateOf<KmpWebViewController?>(null) }


        LaunchedEffect(jsonString) {
            webController?.postJson(jsonString)
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            Logger.d("MapScreen", "jsonString=$jsonString")


            KmpWebView(
//                url = "https://6lll1r3w-5173.inc1.devtunnels.ms/",
                url = "https://tikoncha.uz/map/location/",
//            url = "https://yandex.uz/maps/?ll=63.150118%2C41.765066&z=6",
                onCreated = { controller ->
                    webController = controller
                }
            )

            FilledTonalIconButton(
                modifier = Modifier
                    .padding(ContainerPadding)
                    .size(NormalIconButtonSize),
                onClick = {
                    navigator?.pop()
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.extendedColor.cardColor,
                    contentColor = MaterialTheme.extendedColor.onBackgroundColor
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_left),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(NormalIconButtonPadding)
                )
            }
        }

    }


}