package dev.ch8n.common.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.router
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.domain.usecases.BookmarkUseCases
import dev.ch8n.common.domain.usecases.TagUseCases
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.controllers.TagScreenController
import dev.ch8n.common.utils.DecomposeController

class AppNavigation(
    componentContext: ComponentContext,
) : DecomposeController(componentContext) {

    private val bookmarkUseCases = DomainInjector.bookmarkUseCase
    private val tagUseCases = DomainInjector.tagUseCase

    val router: Router<Destinations, DecomposeController> = router(
        initialConfiguration = Destinations.Home,
        childFactory = ::createComponents,
        handleBackButton = true
    )

    private fun createComponents(
        destinations: Destinations, context: ComponentContext
    ): DecomposeController {
        return when (destinations) {
            is Destinations.Bookmark -> BookmarkScreenController(context, bookmarkUseCases)
            is Destinations.Home -> HomeScreenController(context)
            is Destinations.Tag -> TagScreenController(context, tagUseCases)
        }
    }

    @Composable
    fun render(content: @Composable () -> Unit) {
        content.invoke()
    }
}
