import java.io.FileInputStream
import java.util.Properties

rootProject.name = "Tikoncha_Parents"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    val localProps = Properties().apply {
        val f = rootDir.resolve("local.properties")
        if (f.exists()) load(FileInputStream(f))
    }

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()

        maven {
            name = "TelegramLoginAndroid"
            url = uri("https://maven.pkg.github.com/TelegramMessenger/telegram-login-android")
            credentials {
                username = localProps.getProperty("gpr.user")
                    ?: System.getenv("GITHUB_USERNAME").orEmpty()
                password = localProps.getProperty("gpr.key")
                    ?: System.getenv("GITHUB_TOKEN").orEmpty()
            }
        }
    }
}

include(":composeApp")