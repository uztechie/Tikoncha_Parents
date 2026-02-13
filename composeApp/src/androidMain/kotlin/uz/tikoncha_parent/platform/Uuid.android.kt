package uz.tikoncha_parent.platform

import java.util.UUID

actual fun randomUUID(): String {
    return UUID.randomUUID().toString()
}