plugins {
    id("org.jetbrains.compose") version "1.1.0"
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}


object Android {
    object Versions {
        //https://github.com/arkivanov/Essenty#lifecyle
        const val essentyLifecycleVersion = "0.4.1"

        //https://github.com/arkivanov/Essenty#backpresseddispatcher
        const val essentyBackPressDispatcher = "0.4.1"

        //https://github.com/arkivanov/Essenty#parcelable-and-parcelize
        const val essentyParcelable = "0.4.1"

        //https://arkivanov.github.io/Decompose/extensions/compose/#extensions-for-jetpackjetbrains-compose
        const val decomposeVersion = "0.7.0"

        // https://coil-kt.github.io/coil/compose/
        const val coilVersion = "2.1.0"
    }

    object Dependencies {
        const val decompose = "com.arkivanov.decompose:decompose:${Versions.decomposeVersion}"
        const val decomposeKXT = "com.arkivanov.decompose:extensions-compose-jetbrains:${Versions.decomposeVersion}"
        const val essentyLifecycle = "com.arkivanov.essenty:lifecycle:${Versions.essentyLifecycleVersion}"
        const val essentyBackPressDispatcher =
            "com.arkivanov.essenty:back-pressed:${Versions.essentyBackPressDispatcher}"
        const val essentyParcelable = "com.arkivanov.essenty:parcelable:${Versions.essentyParcelable}"
        const val coil = "io.coil-kt:coil-compose:${Versions.coilVersion}"
    }
}


group = "dev.ch8n"
version = "1.0"

repositories {
    jcenter()
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.5.0")
    implementation(Android.Dependencies.decompose)
    implementation(Android.Dependencies.decomposeKXT)
    implementation(Android.Dependencies.coil)
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "dev.ch8n.android"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}