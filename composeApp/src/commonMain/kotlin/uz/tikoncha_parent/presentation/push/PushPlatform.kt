package uz.tikoncha_parent.presentation.push

expect object PushPlatform {
    fun initialize()
    fun requestNotificationPermissionIfNeeded()
}