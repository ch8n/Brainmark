package dev.ch8n.common.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.controllers.BookmarkScreenController
import dev.ch8n.common.ui.controllers.HomeScreenController
import dev.ch8n.common.ui.controllers.TagScreenController
import dev.ch8n.common.utils.DecomposeController

class NavHostComponent(
    componentContext: ComponentContext,
) : DecomposeController(componentContext) {

    private val bookmarkUseCases = DomainInjector.bookmarkUseCase
    private val tagUseCases = DomainInjector.tagUseCase

    private val router: Router<Destinations, DecomposeController> = router(
        initialConfiguration = Destinations.Home,
        childFactory = ::createComponents,
        handleBackButton = true
    )

    private fun createComponents(
        destinations: Destinations,
        context: ComponentContext
    ): DecomposeController = when (destinations) {
        is Destinations.Bookmark -> BookmarkScreenController(
            componentContext = context,
            createBookmarkUseCase = bookmarkUseCases.createBookmarkUseCase,
            navigateTo = ::navigateTo,
            navigateBack = ::navigateBack,
        )
        is Destinations.Home -> HomeScreenController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )
        is Destinations.Tag -> TagScreenController(
            componentContext = context,
            tagUseCases = tagUseCases,
            navigateTo = ::navigateTo,
            navigateBack = ::navigateBack,
        )
    }

    val rootRouterState: Value<RouterState<Destinations, DecomposeController>> = router.state
    fun navigateTo(destinations: Destinations) {
        router.push(destinations)
    }

    fun navigateBack() {
        router.pop()
    }


}
