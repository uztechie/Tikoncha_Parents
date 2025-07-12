package org.example.project

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.project.presentation.child_confirm_cod.ChildConfirmCodScreen
import org.example.project.presentation.completedTask.CompletedTaskScreen
import org.example.project.presentation.splash.SplashScreen
import cafe.adriel.voyager.transitions.SlideTransition
import org.example.project.presentation.home.HomeScreen
import org.example.project.presentation.profile.subscription.PaymentScreen
import org.example.project.presentation.profile.subscription.SubscriptionScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import ru.sulgik.mapkit.MapKit
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
            Navigator(ChildConfirmCodScreen())
        }
//        Navigator(HomeScreen()){
//            SlideTransition(it)
//        }
    }
}

fun initMapKit() {
    val MAP_KEY: String = "21612db3-4394-4fde-b579-d2e7a1f9afa3"
    MapKit.setApiKey(MAP_KEY)
}
