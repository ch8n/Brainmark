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
import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.ui.navigation.*


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
            componentContext = defaultComponentContext(),
            createDestinations = { destinations: Destinations, context: ComponentContext ->
                when (destinations) {
                    is BookmarksDestination -> TODO()
                    is HomeDestination -> TODO()
                    is PreviewBookmarkHomeDestination -> TODO()
                    is TagManagerDestination -> TODO()
                    is PreviewBookmarkChromeTabDestination -> TODO()
                    is PreviewBookmarkEmbeddedWebDestination -> TODO()
                    is PreviewBookmarkReaderModeDestination -> TODO()
                }
            }
        )
        setContent {
            Children(
                stack = requireNotNull(navController.destinations),
                modifier = Modifier.fillMaxSize()
            ) { destination ->
                destination.instance.Render()
            }
        }
    }
}