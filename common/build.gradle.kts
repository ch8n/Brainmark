import org.jetbrains.compose.compose

object Common {
    object SqlDelight {
        const val dbName = "BrainmarkDB"
        const val packageName = "dev.ch8n.common"
    }

    object Versions {
        const val kotlinVersion = "1.6.10"
        const val coroutinesVersion = "1.6.3"
        const val serializationVersion = "1.3.3"
        const val ktorVersion = "2.0.3"
        const val sqlDelightVersion = "1.5.3"
    }

    object Dependencies {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
        const val serializationKTX = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationVersion}"
        const val ktorClient = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
        const val sqlDelight = "com.squareup.sqldelight:runtime:${Versions.sqlDelightVersion}"
    }
}

object Desktop {
    const val name = "desktop"
    const val jvmTarget = "11"
}

object Android {
    object Versions {
        const val appCompat = "1.4.2"
        const val coreKtx = "1.8.0"
        const val ktorVersion = Common.Versions.ktorVersion
        const val coroutineVersion = Common.Versions.coroutinesVersion
        const val sqlDelightVersion = Common.Versions.sqlDelightVersion
    }

    object Dependencies {
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val ktorAndroid = "io.ktor:ktor-client-okhttp:${Versions.ktorVersion}"
        const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutineVersion}"
        const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelightVersion}"
    }
}

plugins {
    val kotlinVersion = "1.6.10"
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.1.0"
    kotlin("plugin.serialization") version kotlinVersion
    id("com.android.library")
    id("com.squareup.sqldelight")
}

sqldelight {
    database(Common.SqlDelight.dbName) {
        packageName = Common.SqlDelight.packageName
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
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdkVersion(32)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(32)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}