package uz.saidburxon.newedu.presentation.feature.main

import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen

data class BottomNavItem(
    val screen: Screen,
    val icon: Painter,
    val label: String
)