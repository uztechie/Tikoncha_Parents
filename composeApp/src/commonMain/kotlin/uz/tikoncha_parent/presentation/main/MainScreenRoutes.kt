package uz.saidburxon.newedu.presentation.navigation

sealed class MainScreenRoutes(val route: String) {
    object Home : MainScreenRoutes("home")
    object Task : MainScreenRoutes("task")
    object Monitor : MainScreenRoutes("monitor")
    object Map : MainScreenRoutes("map")
    object Profile : MainScreenRoutes("profile")
}