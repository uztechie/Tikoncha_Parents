package com.yourpackage.utils

actual fun formatTwoDigits(number: Int): String {
    return String.format("%02d", number)
}