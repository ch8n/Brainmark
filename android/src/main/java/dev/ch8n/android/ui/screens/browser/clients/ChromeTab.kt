package dev.ch8n.android.ui.screens.browser.clients

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent


fun launchChromeTab(url: String, context: Context) {
    val builder = CustomTabsIntent.Builder()
    val customTabsIntent: CustomTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}