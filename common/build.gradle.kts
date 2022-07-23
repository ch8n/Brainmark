import com.codingfeline.buildkonfig.compiler.FieldSpec.*
import org.jetbrains.compose.compose


/**
 * Common dependencies
 */
object Common {

    object Application {
        const val packageName = "dev.ch8n.brainmark"
        const val sharedResource = "SharedRes"
        const val sharedConfig = "SharedConfig"
    }

    object SqlDelight {
        const val databaseName = "BrainmarkDB"
        const val packageName = "dev.ch8n.sqlDB"
    }

    object Versions {

        const val kotlinVersion = "1.6.10"
        const val coroutinesVersion = "1.6.3"
        const val serializationVersion = "1.3.3"

        // https://cashapp.github.io/sqldelight/
        const val sqlDelightVersion = "1.5.3"

        //https://github.com/benasher44/uuid/releases/tag/0.4.0
        const val uuidVersion = "0.4.0"

        //https://github.com/arkivanov/Essenty#lifecyle
        const val essentyLifecycleVersion = "0.4.1"

        //https://github.com/arkivanov/Essenty#backpresseddispatcher
        const val essentyBackPressDispatcher = "0.4.1"

        //https://github.com/arkivanov/Essenty#parcelable-and-parcelize
        const val essentyParcelable = "0.4.1"

        //https://arkivanov.github.io/Decompose/extensions/compose/#extensions-for-jetpackjetbrains-compose
        const val decomposeVersion = "0.7.0"

        //https://ktor.io/docs/getting-started-ktor-client-multiplatform-mobile.html
        const val ktorVersion = "2.0.3"

        const val mokoResource = "0.20.1"
        const val kotlinXDateTime = "0.4.0"
    }

    object Dependencies {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
        const val serializationKTX = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationVersion}"
        const val ktorClient = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
        const val sqlDelight = "com.squareup.sqldelight:runtime:${Versions.sqlDelightVersion}"
        const val sqlDelightCoroutineKTX = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelightVersion}"
        const val uuid = "com.benasher44:uuid:${Versions.uuidVersion}"
        const val decompose = "com.arkivanov.decompose:decompose:${Versions.decomposeVersion}"
        const val decomposeKXT = "com.arkivanov.decompose:extensions-compose-jetbrains:${Versions.decomposeVersion}"
        const val essentyLifecycle = "com.arkivanov.essenty:lifecycle:${Versions.essentyLifecycleVersion}"
        const val essentyBackPressDispatcher =
            "com.arkivanov.essenty:back-pressed:${Versions.essentyBackPressDispatcher}"
        const val essentyParcelable = "com.arkivanov.essenty:parcelable:${Versions.essentyParcelable}"
        const val mokoResource = "dev.icerock.moko:resources:${Versions.mokoResource}"
        const val kotlinXDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinXDateTime}"
    }
}

/**
 * Desktop dependencies
 */
object Desktop {
    const val name = "desktop"
    const val jvmTarget = "11"

    object Versions {
        const val sqlDelightVersion = Common.Versions.sqlDelightVersion
        const val mokoResource = "0.20.1"
    }

    object Dependencies {
        const val sqlDelightDesktop = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelightVersion}"
        const val mokoResource = "dev.icerock.moko:resources-compose:${Versions.mokoResource}"
    }
}

/**
 * Android dependencies
 */
object Android {

    const val sqlDelightDatabaseName = Common.SqlDelight.databaseName

    object Versions {
        const val appCompat = "1.4.2"
        const val coreKtx = "1.8.0"
        const val ktorVersion = Common.Versions.ktorVersion
        const val coroutineVersion = Common.Versions.coroutinesVersion
        const val sqlDelightVersion = Common.Versions.sqlDelightVersion
        const val mokoResource = "0.20.1"
    }

    object Dependencies {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val ktorAndroid = "io.ktor:ktor-client-okhttp:${Versions.ktorVersion}"
        const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutineVersion}"
        const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelightVersion}"
        const val mokoResource = "dev.icerock.moko:resources-compose:${Versions.mokoResource}"
    }
}

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.1.0"
    kotlin("plugin.serialization") version kotlinVersion
    id("com.android.library")
    id("com.squareup.sqldelight")
    id("com.codingfeline.buildkonfig")
    id("kotlin-parcelize")
    id("dev.icerock.mobile.multiplatform-resources")
}

sqldelight {
    database(Common.SqlDelight.databaseName) {
        packageName = Common.SqlDelight.packageName
    }
}

buildkonfig {
    packageName = Common.Application.packageName
    objectName = Common.Application.sharedConfig

    defaultConfigs {
        buildConfigField(Type.STRING, "SqlDelightDbName", "${Common.SqlDelight.databaseName}.db")
    }
}

group = "dev.ch8n"
version = "1.0"

kotlin {
    android()
    jvm(Desktop.name) {
        compilations.all {
            kotlinOptions.jvmTarget = Desktop.jvmTarget
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                implementation(Common.Dependencies.coroutines)
                implementation(Common.Dependencies.serializationKTX)
                implementation(Common.Dependencies.ktorClient)
                implementation(Common.Dependencies.sqlDelight)
                implementation(Common.Dependencies.sqlDelightCoroutineKTX)
                implementation(Common.Dependencies.uuid)
                implementation(Common.Dependencies.decompose)
                implementation(Common.Dependencies.decomposeKXT)
                implementation(Common.Dependencies.essentyLifecycle)
                implementation(Common.Dependencies.essentyBackPressDispatcher)
                implementation(Common.Dependencies.essentyParcelable)
                implementation(Common.Dependencies.mokoResource)
                implementation(Common.Dependencies.kotlinXDateTime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(Android.Dependencies.appCompat)
                api(Android.Dependencies.coreKtx)
                api(Android.Dependencies.ktorAndroid)
                api(Android.Dependencies.sqlDelightAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api(Desktop.Dependencies.sqlDelightDesktop)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(31)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

multiplatformResources {
    multiplatformResourcesPackage = Common.Application.packageName
    multiplatformResourcesClassName = Common.Application.sharedResource
    disableStaticFrameworkWarning = true
}