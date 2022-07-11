package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import dev.ch8n.android.ui.bookmark.BookmarkScreen
import dev.ch8n.android.ui.home.HomeScreen
import dev.ch8n.android.ui.tag.TagScreen
import dev.ch8n.common.ui.navigation.AppNavigation
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.controllers.TagScreenController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brainMarkApp()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun brainMarkApp() {
        val componentContext = defaultComponentContext()
        val navigation = AppNavigation(componentContext)
        setContent {
            MaterialTheme {
                navigation.render {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Children(routerState = navigation.router.state) { child ->
                            when (val controller = child.instance) {
                                is BookmarkScreenController -> BookmarkScreen(controller, navigation)
                                is TagScreenController -> TagScreen(controller, navigation)
                                is HomeScreenController -> HomeScreen(controller, navigation)
                                else -> throw IllegalStateException("Unhandled controller and ui at navigation")
                            }
                        }
                    }
                }
            }
        }
    }
}

