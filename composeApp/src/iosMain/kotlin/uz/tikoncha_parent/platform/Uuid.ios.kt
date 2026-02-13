package uz.tikoncha_parent.platform

import platform.Foundation.NSUUID

actual fun randomUUID(): String {
    return NSUUID().UUIDString
}
