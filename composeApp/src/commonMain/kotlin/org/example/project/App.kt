package org.example.project

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import uz.saidburxon.newedu.presentation.feature.main.MainScreen


@Composable
@Preview
fun App() {
    MaterialTheme {



        Surface(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Navigator(MainScreen())
        }
//        Navigator(HomeScreen()){
//            SlideTransition(it)
//        }
    }
}