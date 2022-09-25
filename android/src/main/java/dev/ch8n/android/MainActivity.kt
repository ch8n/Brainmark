package dev.ch8n.android

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


class MainActivity : AppCompatActivity() {
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
        val navController = createNavController(
            initialDestination = HomeDestination,
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

                    is CreateBookmarksDestination -> AndroidCreateBookmarkController(this)
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