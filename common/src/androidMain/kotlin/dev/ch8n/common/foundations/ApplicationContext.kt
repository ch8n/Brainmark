package dev.ch8n.common.foundations

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
actual object ApplicationContext {

    private var _context: Context? = null

    val context: Context
        get() = _context ?: error("Application Context not set!")

    fun setContext(context: Context) {
        _context = context.applicationContext
    }

    fun clear() {
        _context = null
    }
}