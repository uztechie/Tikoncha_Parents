package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.ui.graphics.painter.Painter
import uz.saidburxon.newedu.presentation.navigation.MainScreenRoutes

data class BottomNavItem(
    val route: MainScreenRoutes,
    val icon: Painter,
    val label: String
)