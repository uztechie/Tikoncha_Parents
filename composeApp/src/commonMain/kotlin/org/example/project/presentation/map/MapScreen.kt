package org.example.project.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.example.project.platform.isLocationServiceEnabled
import org.example.project.platform.openLocationSettings
import org.example.project.presentation.base.CustomDialog
import org.example.project.presentation.base.CustomHeader
import org.example.project.ui.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.compose.MapConfig
import ru.sulgik.mapkit.compose.MapLogoConfig
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
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

class MapScreen : Screen {

    @Preview
    @Composable
    override fun Content() {

        val TAG = "MapScreen"

        val LATITUDE = 40.776691
        val LONGITUDE = 72.342787

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        val scope = rememberCoroutineScope ()
        BindEffect(controller)

        val permissionViewModel = viewModel{
            PermissionViewModel(controller)
        }
        val permissionState by permissionViewModel.state.collectAsStateWithLifecycle()
        var cameFromSettings by remember { mutableStateOf(false) }


        val locationTracker = rememberLocationTrackerFactory(
            accuracy = LocationTrackerAccuracy.Best
        ).createLocationTracker(permissionsController = controller)

        val locationViewModel = viewModel {
            LocationViewModel(
                tracker = locationTracker
            )
        }
        BindLocationTrackerEffect(locationViewModel.tracker)
        val locationState by locationViewModel.state.collectAsStateWithLifecycle()

        var showGpsDialog by remember {
            mutableStateOf(false)
        }

        var showPermissionDialog by remember {
            mutableStateOf(false)
        }

        println(TAG+" gpsDialog ${locationState.showGpsDialog}")
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

        println(TAG+ " DATAAA = "+locationState.locationData)


        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                println(TAG+" DSASASASASASAS")
                permissionViewModel.refresh();
                cameFromSettings = false
            }
        )

        LaunchedEffect(Unit){
            permissionViewModel.requestPermission()
        }

        println(TAG+" permissionState=$permissionState")
        LaunchedEffect(permissionState){
            when(permissionState){
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


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            CustomHeader(
                title = stringResource(Res.string.xarita)
            ) {

            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            rememberAndInitializeMapKit().bindToLifecycleOwner()

            val userLocationState = rememberUserLocationState()

            val startPosition = CameraPosition(
                target = Point(
                    locationState.locationData?.latitude?:LATITUDE, locationState.locationData?.longitude?:LONGITUDE
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
                    isNightModeEnabled = false,
                    logo = MapLogoConfig(
                        alignment = LogoAlignment(
                            horizontal = LogoHorizontalAlignment.LEFT,
                            vertical = LogoVerticalAlignment.BOTTOM
                        )
                    )
                )
            )
            {

                println(TAG+" location data = "+locationState.locationData)
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



                val placeMarkState = rememberPlacemarkState(
                    geometry = Point(
                        40.776691, 72.342787
                    ),
                    )
                val placeMarkState2 = rememberPlacemarkState(
                    geometry = Point(
                        40.772191, 72.34999
                    ),

                    )
                Placemark(
                    state = placeMarkState,
                    contentSize = DpSize(100.dp, 50.dp)
                ) {
                    MapMarker(
                        title = "Ibroxim",
                    )
                }

                Placemark(
                    state = placeMarkState2,
                    contentSize = DpSize(100.dp, 50.dp)
                ) {
                    MapMarker(
                        title = "Abdurahimjonbek",
                    )
                }
            }

            FilledTonalIconButton(
                onClick = {
                    locationViewModel.reset()
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
            ){
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