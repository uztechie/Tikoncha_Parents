package uz.tikoncha_parent.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.Flow
import uz.tikoncha_parent.AppHolder

actual fun isInternetAvailable(): Boolean {
    return try {
        val context = AppHolder.app
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as? ConnectivityManager ?: return true
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } catch (e: Throwable) {  // ⬅️ Exception → Throwable
        true  // Crash bo'lmasin, IOException baribir tutadi
    }
}

//actual fun observeInternetConnection(): Flow<Boolean> {
//    val context = AppHolder.app
//    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
//
//    if (cm == null) {
//        trySend(true)
//        close()
//        return
//    }
//}