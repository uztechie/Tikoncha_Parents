package uz.tikoncha_parent.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.value
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.SystemConfiguration.SCNetworkReachabilityCreateWithName
import platform.SystemConfiguration.SCNetworkReachabilityGetFlags
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsConnectionRequired
import platform.SystemConfiguration.kSCNetworkReachabilityFlagsReachable

@OptIn(ExperimentalForeignApi::class)
actual fun isInternetAvailable(): Boolean {
    return try {
        memScoped {
            val reachability = SCNetworkReachabilityCreateWithName(null, "apple.com")
                ?: return@memScoped true
            val flags = alloc<UIntVar>()
            if (!SCNetworkReachabilityGetFlags(reachability, flags.ptr)) {
                return@memScoped true
            }
            val isReachable = (flags.value and kSCNetworkReachabilityFlagsReachable) != 0u
            val needsConnection = (flags.value and kSCNetworkReachabilityFlagsConnectionRequired) != 0u
            isReachable && !needsConnection
        }
    } catch (e: Throwable) {  // ⬅️ Exception → Throwable
        true
    }
}