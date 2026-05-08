package uz.tikoncha_parent.presentation.policy.app_site_selection

data class AppFeatureUi(
    val key: String,            // "YOUTUBE_SHORTS", "INSTAGRAM_REELS"
    val name: String,           // "YouTube Shorts"
    val parentPackage: String,  // "com.google.android.youtube"
)

object AppFeatures {

    const val YOUTUBE_PACKAGE = "com.google.android.youtube"
    const val INSTAGRAM_PACKAGE = "com.instagram.android"

    const val KEY_YOUTUBE_SHORTS = "YOUTUBE_SHORTS"
    const val KEY_INSTAGRAM_REELS = "INSTAGRAM_REELS"

    private val REGISTRY: Map<String, List<AppFeatureUi>> = mapOf(
        YOUTUBE_PACKAGE to listOf(
            AppFeatureUi(
                key = KEY_YOUTUBE_SHORTS,
                name = "YouTube Shorts",
                parentPackage = YOUTUBE_PACKAGE,
            )
        ),
        INSTAGRAM_PACKAGE to listOf(
            AppFeatureUi(
                key = KEY_INSTAGRAM_REELS,
                name = "Instagram Reels",
                parentPackage = INSTAGRAM_PACKAGE,
            )
        ),
    )

    fun featuresFor(packageName: String): List<AppFeatureUi> =
        REGISTRY[packageName].orEmpty()

    fun visibleFeaturesFor(app: AppSelectionUi, searchQuery: String): List<AppFeatureUi> {
        val features = featuresFor(app.packageName)
        if (features.isEmpty()) return emptyList()
        if (searchQuery.isBlank()) return features
        if (app.name.contains(searchQuery, ignoreCase = true)) return features
        return features.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    fun matchesSearch(app: AppSelectionUi, searchQuery: String): Boolean {
        if (searchQuery.isBlank()) return true
        if (app.name.contains(searchQuery, ignoreCase = true)) return true
        return featuresFor(app.packageName).any {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }
}