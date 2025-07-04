package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.add_child.ChildScreen
import org.example.project.presentation.slider.SliderScreen
import org.example.project.presentation.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(SplashScreen())
//        Navigator(HomeScreen()){
//            SlideTransition(it)
//        }
    }
}