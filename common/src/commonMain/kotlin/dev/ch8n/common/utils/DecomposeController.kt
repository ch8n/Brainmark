package dev.ch8n.common.utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import dev.ch8n.common.ui.navigation.Destination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext

class NavController {
    fun navigateTo(destination: Destination) {

    }

    fun navigateBack() {
        TODO("Not yet implemented")
    }

}


interface RootScreen {

    val backstack: StateFlow<ChildStack<*, Screen>>

    sealed class Screen : Parcelable {
        @Parcelize
        object Home : Screen()

        @Parcelize
        object Tags : Screen()

        @Parcelize
        object Bookmark : Screen()

        @Parcelize
        sealed class Browser : Screen()
    }
}

class RootComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    val router: Router<Screen, ScreenController> = router(
        initialConfiguration = Screen.Home,
        childFactory = ::createChildComponents,
        handleBackButton = true
    )

    private fun createChildComponents(
        screen: Screen, context: ComponentContext
    ): ScreenController {
        when (screen) {
            Screen.Bookmark -> TODO()
            Screen.Browse.ChromeTab -> TODO()
            Screen.Browse.EmbeddedWeb -> TODO()
            Screen.Browse.ReaderMode -> TODO()
            Screen.Home -> TODO()
            Screen.Tags -> TODO()
        }
    }

}


abstract class Controller : CoroutineScope {
    private var parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    fun cancel() {
        parentJob.cancelChildren()
    }
}

abstract class ScreenController(
    componentContext: ComponentContext
) : ChildDecompose(componentContext)

sealed class Screen : Parcelable {
    @Parcelize
    object Home : Screen()

    @Parcelize
    object Tags : Screen()

    @Parcelize
    object Bookmark : Screen()

    @Parcelize
    sealed class Browse : Screen() {
        @Parcelize
        object ReaderMode : Browse()

        @Parcelize
        object ChromeTab : Browse()

        @Parcelize
        object EmbeddedWeb : Browse()
    }
}


abstract class ChildDecompose(
    componentContext: ComponentContext
) : ComponentContext by componentContext


abstract class DecomposeController(
    componentContext: ComponentContext
) : ComponentContext by componentContext, CoroutineScope {
    private var parentJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    fun cancel() {
        parentJob.cancelChildren()
    }
}

