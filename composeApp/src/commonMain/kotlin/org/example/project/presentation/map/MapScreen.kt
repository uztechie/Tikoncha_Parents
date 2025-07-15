package org.example.project.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.theme.BackgroundColor
import org.jetbrains.compose.resources.stringResource
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition
import tikoncha_parents.composeapp.generated.resources.Res
import tikoncha_parents.composeapp.generated.resources.xarita

class MapScreen: Screen{
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            CustomHeader(
                title = stringResource(Res.string.xarita)
            ) {

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        )
        {
            rememberAndInitializeMapKit().bindToLifecycleOwner()

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
            ){}

        }


    }
}