package dev.ch8n.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import dev.ch8n.android.ui.screens.bookmarks.AndroidBookmarksController
import dev.ch8n.android.ui.screens.browser.AndroidPreviewBookmarkHomeController
import dev.ch8n.android.ui.screens.browser.clients.AndroidPreviewEmbeddedWebController
import dev.ch8n.android.ui.screens.browser.clients.AndroidPreviewReaderModeController
import dev.ch8n.android.ui.screens.createBookmark.AndroidCreateBookmarkController
import dev.ch8n.android.ui.screens.home.AndroidHomeController
import dev.ch8n.android.ui.screens.tagManager.AndroidTagManagerController
import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.ui.navigation.*
import dev.ch8n.common.ui.theme.BrainMarkTheme
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : AppCompatActivity() {

    private val _deeplinkStack = MutableStateFlow<List<Destinations>>(emptyList())
    private fun handleDeeplink(intent: Intent?) {
        var url = intent?.data?.toString()
        if (url == null) {
            url = intent?.extras?.getString("android.intent.extra.TEXT")
        }
        val updatedStack = listOfNotNull(
            HomeDestination,
            if (url != null) CreateBookmarksDestination(url) else null
        )
        _deeplinkStack.tryEmit(updatedStack)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext = DataInjector.applicationContext
        appContext.setContext(applicationContext)
        brainMarkAndroidApp()
    }

    override fun onDestroy() {
        val appContext = DataInjector.applicationContext
        appContext.clear()
        super.onDestroy()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun AppCompatActivity.brainMarkAndroidApp() {
        handleDeeplink(intent)
        val navController = createNavController(
            deeplinkStack = _deeplinkStack,
            componentContext = defaultComponentContext(),
            createDestinations = { destinations: Destinations, context: ComponentContext ->
                when (destinations) {
                    is BookmarksDestination -> AndroidBookmarksController(this)
                    is HomeDestination -> AndroidHomeController(this)
                    is PreviewBookmarkHomeDestination -> AndroidPreviewBookmarkHomeController(
                        this,
                        destinations.bookmark
                    )

                    is TagManagerDestination -> AndroidTagManagerController(this)
                    is PreviewBookmarkChromeTabDestination -> AndroidPreviewBookmarkHomeController(
                        this,
                        destinations.bookmark
                    )

                    is PreviewBookmarkEmbeddedWebDestination -> AndroidPreviewEmbeddedWebController(
                        this,
                        destinations.bookmark
                    )

                    is PreviewBookmarkReaderModeDestination -> AndroidPreviewReaderModeController(
                        this,
                        destinations.bookmark
                    )

                    is CreateBookmarksDestination -> AndroidCreateBookmarkController(this, destinations.url)
                }
            }
        )
        setContent {
            BrainMarkTheme(isDark = true) {
                Children(
                    stack = requireNotNull(navController.destinations),
                    modifier = Modifier.fillMaxSize()
                ) { destination ->
                    destination.instance.Render()
                }
            }
        }
    }
}