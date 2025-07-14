package org.example.project.presentation.location

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

class LocationScreen: Screen {
    @Composable
    override fun Content() {

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val viewModel = viewModel{
            PermissionViewModel(controller)
        }
        val state by viewModel.state.collectAsStateWithLifecycle()

        var cameFromSettings by remember { mutableStateOf(false) }

        OnScreenActive(
            launchedToSettings = cameFromSettings,
            onReturned = {
                println("DSASASASASASAS")
                viewModel.refresh();
                cameFromSettings = false
            }
        )


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                PermissionState.Granted -> {
                    Text("Record-audio ruxsati berilgan.")
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
                    Button(onClick = viewModel::requestLocation) {
                        Text("Ruxsat so‘rash")
                    }
                }
            }
        }

    }
}