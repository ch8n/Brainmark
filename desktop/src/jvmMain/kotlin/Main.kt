import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import dev.ch8n.common.ui.theme.BrainMarkTheme
import ui.screen.TagScreenPreview

fun main() = BrainMarkDesktopApp()


fun BrainMarkDesktopApp() = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        state = WindowState(size = DpSize(1300.dp, 800.dp))
    ) {
        val (isDarkTheme, setDarkTheme) = remember { mutableStateOf(true) }
        BrainMarkTheme(isDarkTheme) {
            TagScreenPreview()
        }
    }
}

