plugins {
    id(Android.Plugin.compose) version Android.Versions.compose
    id(Android.Plugin.application)
    kotlin("android")
    id(Android.Plugin.kotlinxParcelize)
}

group = Android.Platform.appId
version = Android.Platform.versionName

repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(project(":common"))
    androidX()
    decompose()
    accompanist()
    implementation(Android.Dependencies.Coil)
}

android {
    compileSdkVersion(Android.Platform.complieSDK)
    defaultConfig {
        applicationId = Android.Platform.appId
        minSdkVersion(Android.Platform.minSDK)
        targetSdkVersion(Android.Platform.targetSDK)
        versionCode = Android.Platform.versionCode
        versionName = Android.Platform.versionName
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}


// Dependency groups
fun DependencyHandlerScope.androidX() {
    implementation(Android.Dependencies.AndroidX.composeActivity)
}

fun DependencyHandlerScope.decompose() {
    implementation(Android.Dependencies.Decompose.core)
    implementation(Android.Dependencies.Decompose.ktx)
}

fun DependencyHandlerScope.accompanist() {
    implementation(Android.Dependencies.Accompanist.flowLayout)
    implementation(Android.Dependencies.Accompanist.placeHolder)
}
