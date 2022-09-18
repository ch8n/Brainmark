package dev.ch8n.android.ui.screens.browser.clients

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


fun Context.launchChromeTab(url: String) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent: CustomTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}