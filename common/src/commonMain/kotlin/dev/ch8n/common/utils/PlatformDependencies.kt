package dev.ch8n.common.utils


enum class SupportedPlatform {
    ComposeAndroid,
    SwiftIOS,
    ReactWeb,
    KtorServer,
    ComposeDesktopJVM
}

expect object PlatformDependencies {
    val currentPlatform: SupportedPlatform
}