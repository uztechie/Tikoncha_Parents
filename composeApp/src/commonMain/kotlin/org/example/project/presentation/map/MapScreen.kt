package org.example.project.presentation.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.example.project.presentation.base.CustomHeader
import org.example.project.presentation.base.theme.BackgroundColor

class MapScreen: Screen{
    @Composable
    override fun Content() {
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
    }
}