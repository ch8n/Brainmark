package dev.ch8n.common.utils

import android.content.Context

actual object PlatformDependencies {
    private var _appContext: Context? = null
    val appContext: Context
        get() = _appContext ?: error("Application Context not set")

    fun setApplicationContext(context: Context) {
        _appContext = context
    }
}