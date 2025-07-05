package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.ui.graphics.painter.Painter
import uz.saidburxon.newedu.presentation.navigation.ScreenMain

data class BottomNavItem(
    val route: ScreenMain,
    val icon: Painter,
    val label: String
)