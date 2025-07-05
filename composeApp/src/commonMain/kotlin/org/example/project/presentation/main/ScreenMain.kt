package uz.saidburxon.newedu.presentation.navigation

sealed class ScreenMain(val route: String) {
    object Home : ScreenMain("home")
    object Task : ScreenMain("task")
    object Conversation : ScreenMain("conversation")
    object Profile : ScreenMain("profile")
}