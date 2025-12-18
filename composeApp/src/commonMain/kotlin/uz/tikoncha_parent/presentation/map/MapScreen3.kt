package uz.tikoncha_parent.presentation.map

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.tikoncha_parent.common.Util
import uz.tikoncha_parent.platform.isLocationServiceEnabled
import uz.tikoncha_parent.platform.openLocationSettings
import uz.tikoncha_parent.presentation.base.CustomDialog
import uz.tikoncha_parent.ui.*
import uz.tikoncha_parent.ui.theme.LocalBarsConfig
import uz.tikoncha_parent.ui.theme.ThemeMode
import uz.tikoncha_parent.ui.theme.ThemePrefs
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.getKoin
import ru.sulgik.mapkit.compose.MapConfig
import ru.sulgik.mapkit.compose.MapLogoConfig
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.YandexMapsComposeExperimentalApi
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.compose.user_location.UserLocationConfig
import ru.sulgik.mapkit.compose.user_location.rememberUserLocationState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.logo.LogoAlignment
import ru.sulgik.mapkit.logo.LogoHorizontalAlignment
import ru.sulgik.mapkit.logo.LogoVerticalAlignment
import ru.sulgik.mapkit.map.CameraPosition
import tikoncha_parents.composeapp.generated.resources.*
import uz.tikoncha_parent.domain.use_case.ChildrenLocationUseCase

class MapScreen3 : Screen {

    @OptIn(YandexMapsComposeExperimentalApi::class)
    @Preview
    @Composable
    override fun Content() {

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

//        val locationViewModel = koinViewModel<LocationViewModel>()


        val state by locationViewModel.state.collectAsStateWithLifecycle()
        val themeMode by rememberSaveable { mutableStateOf(ThemePrefs.load()) }
        val darkTheme = when (themeMode) {
            ThemeMode.DARK   -> true
            ThemeMode.LIGHT  -> false
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }

        val TAG = "MapScreen"

        val LATITUDE = 40.776691
        val LONGITUDE = 72.342787



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

        println(TAG + " gpsDialog ${locationState.showGpsDialog}")
//        LaunchedEffect(locationState.showGpsDialog){
//            showGpsDialog = locationState.showGpsDialog?:false
//        }

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

        println(TAG + " DATAAA = " + locationState.locationData)


        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                println(TAG + " DSASASASASASAS")
                permissionViewModel.refresh();
                cameFromSettings = false
            }
        )

        LaunchedEffect(Unit) {
            permissionViewModel.requestPermission()
        }

        println(TAG + " permissionState=$permissionState")
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

        val bars = LocalBarsConfig.current
        DisposableEffect(Unit) {
            val prev = bars.value
            bars.value = prev.copy(
                paddingEnabled = false,
                transparentStatusBar = true
            )
            onDispose { bars.value = prev }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            rememberAndInitializeMapKit().bindToLifecycleOwner()

            val userLocationState = rememberUserLocationState()


            val locationIcon = rememberLocationIconForMapMarker(
                title = "Abdurahim Sharipov", // → bu yerga uzun matn ketsa ham sigadi
                backgroundColor = Color.Black,
                contentColor = Color.White,
                icon = painterResource(Res.drawable.person),
                textMaxLines = 3,              // yoki Int.MAX_VALUE
                maxWidth = 280.dp              // kerak bo'lsa oshiring/ekranga nisbiy qiling
            )


            val startPosition = CameraPosition(
                target = Point(
                    locationState.locationData?.latitude ?: LATITUDE,
                    locationState.locationData?.longitude ?: LONGITUDE
                ),
                zoom = 15f,
                0f,
                tilt = 0f
            )
            val cameraPosition = rememberCameraPositionState {
                position = startPosition
            }
            YandexMap(
                modifier = Modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPosition,
                locationState = userLocationState,
                locationConfig = UserLocationConfig(
                    isVisible = true,
                    arrow = UserLocationConfig.LocationIcon(
                        image = imageProvider(size = DpSize(100.dp, 50.dp)) {
                            MapMarker(
                                title = "Siz",
                                backgroundColor = Color.Black
                            )
                        }
                    ),
                    pin = UserLocationConfig.LocationIcon(
                        image = imageProvider(size = DpSize(100.dp, 50.dp)) {
                            MapMarker(
                                title = "Siz",
                                backgroundColor = Color.Black
                            )
                        },
                    ),
                ),
                config = MapConfig(
                    isNightModeEnabled = darkTheme,
                    logo = MapLogoConfig(
                        alignment = LogoAlignment(
                            horizontal = LogoHorizontalAlignment.LEFT,
                            vertical = LogoVerticalAlignment.BOTTOM
                        )
                    )
                )
            )
            {

                println(TAG + " location data = " + locationState.locationData)
//                locationState.locationData?.let {location->
//                    val placeMarkMine = rememberPlacemarkState(
//                        geometry = Point(
//                            location.latitude, location.longitude
//                        ),
//                    )
//                    Placemark(
//                        state = placeMarkMine,
//                        contentSize = DpSize(100.dp, 50.dp)
//                    ) {
//                        MapMarker(
//                            title = "Siz",
//                            backgroundColor = Color.Black
//                        )
//                    }
//                }

                state.childrenLocationList.forEach { item ->
                    if (item.lat != null && item.lng != null){
                        val placeMarkState = rememberPlacemarkState(
                            geometry = Point(
                                item.lat, item.lng
                            ),
                        )

                        Placemark(
                            state = placeMarkState,
                            contentSize = DpSize(200.dp, 80.dp)
                        ) {
                            MapMarker(
                                title = "${item.first_name}",
                                lastUpdated = Util.reformatDateTime_dd_MM_hh_mm(item.updated_at)
                            )
                        }
                    }
                }
            }

            FilledTonalIconButton(
                onClick = {
                    permissionViewModel.requestPermission()
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = TonalButtonContainerColor,
                    contentColor = TextColor
                ),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(ContainerPadding)
                    .size(NormalIconButtonSize)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.find_location),
                    contentDescription = "",
                    modifier = Modifier
                        .size(NormalIconSize)

                )
            }
        }
    }
}