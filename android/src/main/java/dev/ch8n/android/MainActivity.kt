package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import dev.ch8n.android.ui.screens.createBookmark.CreateBookmarkContent
import dev.ch8n.android.ui.screens.home.HomeScreen
import dev.ch8n.android.ui.screens.tagManager.TagScreenManager
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.controllers.CreateBookmarkController
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.controllers.TagScreenController
import dev.ch8n.common.ui.navigation.NavHostComponent
import dev.ch8n.common.ui.theme.BrainMarkTheme
import dev.ch8n.common.utils.PlatformDependencies

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        brainMarkApp()
    }

    @OptIn(ExperimentalDecomposeApi::class)
    fun brainMarkApp() {
        PlatformDependencies.setApplicationContext(applicationContext)
        val navigation = NavHostComponent(defaultComponentContext())
        setContent {
            val (isDarkTheme, setDarkTheme) = remember { mutableStateOf(true) }
            BrainMarkTheme(isDark = isDarkTheme) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                ) {
                    Children(routerState = navigation.rootRouterState) { child ->
                        when (val controller = child.instance) {
                            is BookmarkScreenController -> BookmarkScreen(controller)
                            is TagScreenController -> TagScreenManager(controller)
                            is HomeScreenController -> HomeScreen(
                                controller = controller,
                                onSettingsClicked = {
                                    setDarkTheme.invoke(!isDarkTheme)
                                }
                            )
                            is CreateBookmarkController -> CreateBookmarkContent(
                                controller = controller
                            )
                            else -> throw IllegalStateException("Unhandled controller and ui at navigation")
                        }
                    }
                }
            }
        }
    }

    private fun BookmarkScreen(controller: BookmarkScreenController) {
        //TODO fix
    }
}