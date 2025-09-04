import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.cocoapods)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    cocoapods {
        // Podspec -> shared.podspec avto-generate bo‘ladi
        version = "1.0.0"
        summary = "KMP + Compose + Google Maps namunasi"
        homepage = "https://example.com"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
        pod("YandexMapsMobile") {
            version = "4.17.0-lite"
        }
    }
    
    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation("com.google.android.gms:play-services-location:21.2.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            api(libs.koin.annotations)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.remember.settings)


            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
            implementation("network.chaintech:qr-kit:3.1.2")

            implementation("ru.sulgik.mapkit:yandex-mapkit-kmp-compose:0.3.0")
            implementation("ru.sulgik.mapkit:yandex-mapkit-kmp:0.3.0")
            implementation("ru.sulgik.mapkit:yandex-mapkit-kmp-moko:0.3.0") // optional: moko support
            implementation("ru.sulgik.mapkit:yandex-mapkit-kmp-moko-compose:0.3.0")


            //location
            api("dev.icerock.moko:permissions:0.19.1")
            api("dev.icerock.moko:permissions-compose:0.19.1")
            api("dev.icerock.moko:permissions-location:0.19.1")

            // Lokatsiya trakeri
            api("dev.icerock.moko:geo:0.8.0")
            // Compose yordamchi adapterlari
            api("dev.icerock.moko:geo-compose:0.8.0")

            api("dev.icerock.moko:resources:0.25.0")
            api("dev.icerock.moko:resources-compose:0.25.0")


            implementation("com.russhwolf:multiplatform-settings:1.3.0")
            implementation("com.russhwolf:multiplatform-settings-no-arg:1.3.0")

            // Coil
            implementation("io.coil-kt.coil3:coil-compose:3.3.0")
            implementation("io.coil-kt.coil3:coil-network-ktor3:3.3.0")

        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }


    }
    sourceSets.named("commonMain").configure {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}

ksp {
    arg("KOIN_USE_COMPOSE_VIEWMODEL","true")
    arg("KOIN_CONFIG_CHECK","true")
}
dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
}

project.tasks.withType(KotlinCompilationTask::class.java).configureEach {
    if(name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

android {
    namespace = "uz.tikoncha_parent"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "uz.tikoncha_parent"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

