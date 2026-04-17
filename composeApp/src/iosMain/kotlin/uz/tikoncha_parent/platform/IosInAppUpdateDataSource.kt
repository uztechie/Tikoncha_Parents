package uz.tikoncha_parent.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import uz.tikoncha_parent.data.in_app_update.InAppUpdateDataSource
import uz.tikoncha_parent.domain.model.in_app_update.InstallEvent
import uz.tikoncha_parent.domain.model.in_app_update.UpdateStatus
import uz.tikoncha_parent.domain.model.in_app_update.UpdateType

@OptIn(ExperimentalForeignApi::class)
class IosInAppUpdateDataSource(
    private val appId: String? = null
) : InAppUpdateDataSource {

    override suspend fun checkUpdate(): UpdateStatus {
        return try {
            val bundleId = NSBundle.mainBundle.bundleIdentifier ?: return UpdateStatus.NotSupported
            val currentVersion = NSBundle.mainBundle.infoDictionary
                ?.get("CFBundleShortVersionString") as? String
                ?: return UpdateStatus.NotSupported

            val storeVersion = fetchAppStoreVersion(bundleId)
                ?: return UpdateStatus.NoUpdate

            if (isNewerVersion(storeVersion, currentVersion)) {
                UpdateStatus.UpdateAvailable(
                    allowedTypes = setOf(UpdateType.IMMEDIATE),
                    versionCodeAvailable = null
                )
            } else {
                UpdateStatus.NoUpdate
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UpdateStatus.Failed(e.message)
        }
    }

    override fun observeInstallEvents(): Flow<InstallEvent> = emptyFlow()

    override suspend fun completeFlexibleUpdate() {
        // iOS'da flexible update yo'q — no-op
    }

    /**
     * iTunes Lookup API orqali App Store'dagi joriy versiyani olish.
     */
    @Suppress("UNCHECKED_CAST")
    private fun fetchAppStoreVersion(bundleId: String): String? {
        val urlString = "https://itunes.apple.com/lookup?bundleId=$bundleId"
        val url = NSURL.URLWithString(urlString) ?: return null
        val data: NSData = NSData.dataWithContentsOfURL(url) ?: return null

        val json = NSJSONSerialization.JSONObjectWithData(data, 0u, null) as? Map<String, Any>
            ?: return null

        val results = json["results"] as? List<Map<String, Any>> ?: return null
        if (results.isEmpty()) return null

        return results[0]["version"] as? String
    }

    /**
     * Semantik versiya taqqoslash: "1.2.3" vs "1.2.2"
     * store > current bo'lsa true qaytaradi.
     */
    private fun isNewerVersion(store: String, current: String): Boolean {
        val storeParts = store.split(".").mapNotNull { it.toIntOrNull() }
        val currentParts = current.split(".").mapNotNull { it.toIntOrNull() }

        val maxLen = maxOf(storeParts.size, currentParts.size)
        for (i in 0 until maxLen) {
            val s = storeParts.getOrElse(i) { 0 }
            val c = currentParts.getOrElse(i) { 0 }
            if (s > c) return true
            if (s < c) return false
        }
        return false
    }
}