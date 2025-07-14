package org.example.project.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.theme.BackgroundColor
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.user_location.UserLocationConfig
import ru.sulgik.mapkit.compose.user_location.rememberUserLocationState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.alarm
import tikoncha_parents.composeapp.generated.resources.calendar
import tikoncha_parents.composeapp.generated.resources.call

class MapScreen: Screen{
    @Composable
    override fun Content() {

        val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
        val controller: PermissionsController = remember(factory) { factory.createPermissionsController() }
        val coroutineScope: CoroutineScope = rememberCoroutineScope()


        LaunchedEffect(true){
            delay(2000)
            controller.providePermission(Permission.LOCATION)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            CustomHeader(
                title = "Xarita"
            ) {

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            rememberAndInitializeMapKit().bindToLifecycleOwner()

//            val userLocationState = rememberUserLocationState()

            val startPosition = CameraPosition(
                target = Point(
                    40.776691, 72.342787
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
//                locationState = userLocationState,
//                locationConfig = UserLocationConfig(
//                    isVisible = true,
//                    arrow = UserLocationConfig.LocationIcon(
//                        image = imageProvider(Res.drawable.call),
//                    ),
//                    pin = UserLocationConfig.LocationIcon(
//                        image = imageProvider(Res.drawable.call),
//                    ),
//                    accuracy = UserLocationConfig.LocationAccuracy(
//                        fillColor = Color.Yellow
//                    )
//                ),
            ){}

        }


    }
}