package dev.ch8n.common.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.router
import dev.ch8n.common.domain.usecases.BookmarkUseCases
import dev.ch8n.common.utils.DecomposeController

class AppNavigation(componentContext: ComponentContext) : DecomposeController(componentContext) {

    val router: Router<Destinations, DecomposeController> = router(
        initialConfiguration = Destinations.Home,
        childFactory = ::createDestinations,
        handleBackButton = true
    )

    private fun createDestinations(
        destinations: Destinations,
        context: ComponentContext
    ): DecomposeController {
        return when (destinations) {
            Destinations.Bookmark -> TODO()
            Destinations.Home -> TODO()
            Destinations.Tag -> TODO()
        }
    }

    @Composable
    fun render(content: @Composable () -> Unit) {
        content.invoke()
    }
}
