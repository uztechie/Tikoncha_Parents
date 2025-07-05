package com.yourpackage.utils

import platform.Foundation.*

actual fun formatTwoDigits(number: Int): String {
    return NSString.stringWithFormat("%02d", number.toLong()) as String
}
