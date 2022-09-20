package dev.ch8n.android.ui.screens.browser.clients

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import dev.ch8n.android.utils.rememberMutableState
import dev.ch8n.common.ui.controllers.WebViewController

@Composable
fun AndroidWebView(
    controller: WebViewController
) {

    var isLoading by rememberMutableState(false)
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    val webViewClient = remember {
        object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                isLoading = true
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
            }
        }
    }

    AndroidView(
        factory = {
            webView.also {
                it.webViewClient = webViewClient
                it.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
                with(it.settings) {
                    userAgentString =
                        "Mozilla/5.0 (Linux; Android 6.0.1; Nexus 5X Build/MMB29P) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/W.X.Y.Z Mobile Safari/537.36 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"
                    loadsImagesAutomatically = true
                    javaScriptEnabled = true
                }
                it.loadUrl(controller.bookmarkUrl)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .placeholder(
                visible = isLoading,
                color = MaterialTheme.colors.surface,
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = MaterialTheme.colors.onSurface,
                    animationSpec = InfiniteRepeatableSpec(
                        animation = tween(durationMillis = 400)
                    )
                )
            )
    )
}