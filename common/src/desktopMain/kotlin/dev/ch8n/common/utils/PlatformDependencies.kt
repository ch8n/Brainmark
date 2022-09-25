package dev.ch8n.common.utils

actual object PlatformDependencies {
    actual val currentPlatform: SupportedPlatform = SupportedPlatform.ComposeDesktopJVM
}