package uz.tikoncha_parent

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

