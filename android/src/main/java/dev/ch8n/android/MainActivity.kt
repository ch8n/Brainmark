package dev.ch8n.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import dev.ch8n.android.ui.bookmark.BookmarkScreen
import dev.ch8n.android.ui.home.HomeScreen
import dev.ch8n.android.ui.tag.TagScreen
import dev.ch8n.common.ui.controllers.BookmarkScreenController
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
            BrainMarkTheme() {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    Children(routerState = navigation.rootRouterState) { child ->
                        when (val controller = child.instance) {
                            is BookmarkScreenController -> BookmarkScreen(controller)
                            is TagScreenController -> TagScreen(controller)
                            is HomeScreenController -> HomeScreen(controller)
                            else -> throw IllegalStateException("Unhandled controller and ui at navigation")
                        }
                    }
                }
            }
        }
    }
}

