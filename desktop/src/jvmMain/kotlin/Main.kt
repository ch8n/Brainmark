import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.ch8n.common.ui.controllers.*
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.ui.navigation.NavHostComponent
import dev.ch8n.common.ui.theme.BrainMarkTheme

fun main() {
    val lifecycle = LifecycleRegistry()
    val defaultComponentContext = DefaultComponentContext(lifecycle = lifecycle)
    BrainMarkDesktopApp(defaultComponentContext, lifecycle)
}


@OptIn(ExperimentalDecomposeApi::class)
fun BrainMarkDesktopApp(defaultComponentContext: DefaultComponentContext, lifecycle: LifecycleRegistry) = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(size = DpSize(1800.dp, 800.dp))
    ) {
        val windowState = rememberWindowState()
        LifecycleController(lifecycle, windowState)
        val navigation = remember { NavHostComponent(defaultComponentContext) }
        val (isDarkTheme, setDarkTheme) = remember { mutableStateOf(true) }
        BrainMarkTheme(isDarkTheme) {
            CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.surface)
                ) {
                    Children(routerState = navigation.rootRouterState) { child ->
                        when (val controller = child.instance) {
                            is BookmarkController -> WorkInProgress(
                                controller = controller,
                                onSettingsClicked = {
                                    setDarkTheme.invoke(!isDarkTheme)
                                }
                            )

                            is TagManagerController -> {}

//                                TagScreenPreview(
//                                controller = controller,
//                                onSettingsClicked = {
//                                    setDarkTheme.invoke(!isDarkTheme)
//                                }
//                            )
                            is HomeController -> WorkInProgress(
                                controller = controller,
                                onSettingsClicked = {
                                    setDarkTheme.invoke(!isDarkTheme)
                                }
                            )

                            is CreateBookmarkController -> WorkInProgress(
                                controller = controller,
                                onSettingsClicked = {}
                            )

                            is PreviewBookmarkController -> WorkInProgress(
                                controller = controller,
                                onSettingsClicked = {}
                            )

                            else -> throw IllegalStateException("Unhandled controller and ui at navigation")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WorkInProgress(controller: Any, onSettingsClicked: () -> Unit) {

    LaunchedEffect(Unit) {
        if (controller is HomeController) {
            controller.navigateTo(Destinations.TagManager)
        }
    }
}

