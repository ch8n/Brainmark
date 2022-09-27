import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.ch8n.common.ui.controllers.HomeController
import dev.ch8n.common.ui.navigation.*
import dev.ch8n.common.ui.theme.BrainMarkTheme

fun main() {
    val lifecycle = LifecycleRegistry()
    val defaultComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    val navController = createNavController(
        initialDestination = HomeDestination,
        componentContext = defaultComponentContext,
        createDestinations = { destinations: Destinations, context: ComponentContext ->
            when (destinations) {
                is BookmarksDestination -> TODO()
                is HomeDestination -> TODO()
                is PreviewBookmarkChromeTabDestination -> TODO()
                is PreviewBookmarkEmbeddedWebDestination -> TODO()
                is PreviewBookmarkHomeDestination -> TODO()
                is PreviewBookmarkReaderModeDestination -> TODO()
                is TagManagerDestination -> TODO()
                is CreateBookmarksDestination -> TODO()
            }
        }
    )
    BrainMarkDesktopApp(lifecycle, navController)
}


@OptIn(ExperimentalDecomposeApi::class)
fun BrainMarkDesktopApp(
    lifecycle: LifecycleRegistry,
    navController: NavController
) = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(size = DpSize(1800.dp, 800.dp))
    ) {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        val (isDarkTheme, setDarkTheme) = remember { mutableStateOf(true) }
        BrainMarkTheme(isDarkTheme) {
            CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                Children(
                    stack = navController.destinations,
                    modifier = Modifier.fillMaxSize()
                ) {
                    it.instance.Render()
                }
            }
        }
    }
}


@Composable
fun WorkInProgress(controller: Any, onSettingsClicked: () -> Unit) {

    LaunchedEffect(Unit) {
        if (controller is HomeController) {
            controller.routeTo(TagManagerDestination)
        }
    }
}

