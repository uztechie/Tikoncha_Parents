package org.example.project.presentation.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
                title = "GPS o'chirilgan",
                message = "Xaritadan to'liq foydalanish uchun GPS ni yoqing!",
                buttonText = "Yoqish",
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
                    Text("Record-audio ruxsati berilgan.")
                    locationViewModel.checkGPS()

                }

                PermissionState.DeniedAlways -> {
                    Text("Ruxsat doimiy rad etilgan.")
                    Button(
                        onClick = {
                            cameFromSettings = true
                            controller.openAppSettings()
                        }
                    ) {
                        Text("Sozlamalarni ochish")
                    }
                }

                else -> {
                    Button(onClick = viewModel::requestPermission) {
                        Text("Ruxsat so‘rash")
                    }
                }
            }
        }

    }
}