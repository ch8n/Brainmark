import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope


object Common {

    object Versions {
        // https://ktor.io/docs/getting-started-ktor-client.html#add-dependencies
        const val ktorVersion = "2.1.1"
    }

    object Dependencies {
        object Ktor {
            val core: String = "io.ktor:ktor-client-core:${Versions.ktorVersion}"
        }
    }
}

object Desktop {

    object Platform

    object Env

}

object Android {

    object Versions {

        // https://github.com/JetBrains/compose-jb/tree/master/tutorials/Getting_Started
        const val compose = "1.1.0"

        object androidX {
            const val composeActivity = "1.5.0"
        }

        //https://github.com/arkivanov/Essenty
        object essenty {
            private const val baseVersion = "0.4.1"

            //https://github.com/arkivanov/Essenty#lifecyle
            const val lifecycleVersion = baseVersion

            //https://github.com/arkivanov/Essenty#backpresseddispatcher
            const val backPressDispatcher = baseVersion

            //https://github.com/arkivanov/Essenty#parcelable-and-parcelize
            const val parcelable = baseVersion
        }

        //https://arkivanov.github.io/Decompose/extensions/compose/#extensions-for-jetpackjetbrains-compose
        const val decomposeVersion = "0.7.0"

        // https://coil-kt.github.io/coil/compose/
        const val coilVersion = "2.1.0"

        //https://github.com/google/accompanist
        object accompanist {
            //https://github.com/google/accompanist/releases/tag/v0.22.0-rc
            private const val baseVersion = "0.22.0-rc"

            //https://google.github.io/accompanist/flowlayout/
            const val flowLayout = baseVersion

            //https://google.github.io/accompanist/placeholder/
            const val placeholder = baseVersion
        }

        const val ktorVersion = Common.Versions.ktorVersion
    }

    object Plugin {
        const val compose = "org.jetbrains.compose"
        const val application = "com.android.application"
        const val kotlinxParcelize = "kotlin-parcelize"
    }

    object Platform {
        const val complieSDK = 31
        const val targetSDK = 31
        const val minSDK = 24
        const val appId = "dev.ch8n.android"
        const val versionCode = 1
        const val versionName = "1.0"
    }

    object Dependencies {

        object AndroidX {
            const val composeActivity = "androidx.activity:activity-compose:${Versions.androidX.composeActivity}"
        }

        object Decompose {
            const val core = "com.arkivanov.decompose:decompose:${Versions.decomposeVersion}"
            const val ktx = "com.arkivanov.decompose:extensions-compose-jetbrains:${Versions.decomposeVersion}"
        }

        object Essenty {
            const val lifecycle = "com.arkivanov.essenty:lifecycle:${Versions.essenty.lifecycleVersion}"
            const val backPressDispatcher =
                "com.arkivanov.essenty:back-pressed:${Versions.essenty.backPressDispatcher}"
            const val parcelable = "com.arkivanov.essenty:parcelable:${Versions.essenty.parcelable}"
        }

        val Coil = "io.coil-kt:coil-compose:${Versions.coilVersion}"

        object Accompanist {
            val flowLayout = "com.google.accompanist:accompanist-flowlayout:${Versions.accompanist.flowLayout}"
            val placeHolder = "com.google.accompanist:accompanist-placeholder:${Versions.accompanist.placeholder}"
        }

        object Ktor {
            val android = "io.ktor:ktor-client-okhttp:${Versions.ktorVersion}"
        }

    }

}




