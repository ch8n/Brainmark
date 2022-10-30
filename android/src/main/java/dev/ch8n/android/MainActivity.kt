package dev.ch8n.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
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
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.controllers.DeeplinkController
import dev.ch8n.common.ui.navigation.BookmarksDestination
import dev.ch8n.common.ui.navigation.CreateBookmarksDestination
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.ui.navigation.HomeDestination
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.ui.navigation.PreviewBookmarkChromeTabDestination
import dev.ch8n.common.ui.navigation.PreviewBookmarkEmbeddedWebDestination
import dev.ch8n.common.ui.navigation.PreviewBookmarkHomeDestination
import dev.ch8n.common.ui.navigation.PreviewBookmarkReaderModeDestination
import dev.ch8n.common.ui.navigation.TagManagerDestination
import dev.ch8n.common.ui.navigation.createNavController
import dev.ch8n.common.ui.theme.BrainMarkTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : AppCompatActivity() {

    private val _deeplinkIntent = MutableStateFlow<Intent?>(null)

    private fun handleDeeplink(intent: Intent?) {
        _deeplinkIntent.tryEmit(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContext = DataInjector.applicationContext
        appContext.setContext(applicationContext)
        handleDeeplink(intent)
        brainMarkAndroidApp()
    }

    override fun onDestroy() {
        val appContext = DataInjector.applicationContext
        appContext.clear()
        super.onDestroy()
    }

    private fun NavController.bookmarkController() = AndroidBookmarksController(this)
    private fun NavController.homeController() = AndroidHomeController(this)
    private fun NavController.bookmarkHomeController(bookmark: Bookmark) =
        AndroidPreviewBookmarkHomeController(
            this,
            bookmark
        )

    private fun NavController.tagManagerController() = AndroidTagManagerController(this)

    private fun NavController.createBookmarkController(url: String?) =
        AndroidCreateBookmarkController(
            this,
            url = url
        )

    private fun NavController.readerModeController(bookmark: Bookmark) =
        AndroidPreviewReaderModeController(
            this,
            bookmark
        )

    private fun NavController.embeddedWebController(bookmark: Bookmark) =
        AndroidPreviewEmbeddedWebController(
            this,
            bookmark
        )

    private fun NavController.chromeTab(bookmark: Bookmark) = AndroidPreviewBookmarkHomeController(
        this,
        bookmark
    )

    @OptIn(ExperimentalDecomposeApi::class)
    fun AppCompatActivity.brainMarkAndroidApp() {
        val navController = createNavController(
            initialDestination = HomeDestination,
            componentContext = defaultComponentContext(),
            createDestinations = { destinations: Destinations, context: ComponentContext ->
                when (destinations) {
                    is BookmarksDestination -> bookmarkController()
                    is HomeDestination -> homeController()
                    is PreviewBookmarkHomeDestination -> bookmarkHomeController(destinations.bookmark)
                    is TagManagerDestination -> tagManagerController()
                    is PreviewBookmarkChromeTabDestination -> chromeTab(destinations.bookmark)
                    is PreviewBookmarkEmbeddedWebDestination -> embeddedWebController(destinations.bookmark)
                    is PreviewBookmarkReaderModeDestination -> readerModeController(destinations.bookmark)
                    is CreateBookmarksDestination -> createBookmarkController(destinations.url)
                }
            }
        )
        setContent {

            LaunchedEffect(Unit) {
                _deeplinkIntent.collect {
                    val intent = _deeplinkIntent.value ?: return@collect
                    val url = intent.extras
                        ?.getString("android.intent.extra.TEXT") ?: return@collect
                    val destination = DeeplinkController.handleDeeplink(url) ?: return@collect
                    delay(500)
                    navController.routeTo(destination)
                }
            }

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