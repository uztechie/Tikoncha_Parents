package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.presentation.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()){
            SlideTransition(it)
        }
    }
}