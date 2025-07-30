package org.example.project.presentation.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.example.project.platform.openLocationSettings
import org.example.project.presentation.base.CustomDialog
import org.jetbrains.compose.resources.stringResource
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.*

class LocationScreen: Screen {
    @Composable
    override fun Content() {
        val TAG = "LocationScreen"
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        val scope = rememberCoroutineScope ()
        BindEffect(controller)

        val viewModel = viewModel{
            PermissionViewModel(controller)
        }
        val permissionState by viewModel.state.collectAsStateWithLifecycle()
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

        LaunchedEffect(locationState.showGpsDialog){
//            showGpsDialog = locationState.showGpsDialog
        }

        if (showGpsDialog){
            CustomDialog(
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
        }

        println(TAG+ " DATAAA = "+locationState.locationData)


        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                println(TAG+" DSASASASASASAS")
                viewModel.refresh();
                cameFromSettings = false
            }
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (permissionState) {
                PermissionState.Granted -> {
                    Text(stringResource(Res.string.record_audio_ruxsati_berilgan))
                    locationViewModel.checkGPS()

                }

                PermissionState.DeniedAlways -> {
                    Text(stringResource(Res.string.ruxsat_doimiy_rad_etilgan))
                    Button(
                        onClick = {
                            cameFromSettings = true
                            controller.openAppSettings()
                        }
                    ) {
                        Text(stringResource(Res.string.sozlamalarni_ochish))
                    }
                }

                else -> {
                    Button(onClick = viewModel::requestPermission) {
                        Text(stringResource(Res.string.ruxsat_so_rash))
                    }
                }
            }
        }
    }
}