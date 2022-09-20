package dev.ch8n.common.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import dev.ch8n.common.ui.controllers.*
import dev.ch8n.common.utils.DecomposeController

class NavHostComponent(
    componentContext: ComponentContext,
) : DecomposeController(componentContext) {

    private val router: Router<Destinations, DecomposeController> = router(
        initialConfiguration = Destinations.Home,
        childFactory = ::createComponents,
        handleBackButton = true
    )

    private fun createComponents(
        destinations: Destinations,
        context: ComponentContext
    ): DecomposeController = when (destinations) {

        is Destinations.Bookmarks -> BookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destinations.Home -> HomeController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destinations.TagManager -> TagManagerController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destinations.CreateBookmark -> CreateBookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destinations.PreviewBookmark -> PreviewBookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkId = destinations.bookmarkId
        )

        is Destinations.ReaderScreen -> ReaderModeController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkUrl = destinations.url
        )

        is Destinations.WebView -> WebViewController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkUrl = destinations.url
        )
    }

    val rootRouterState: Value<RouterState<Destinations, DecomposeController>> = router.state

    private fun navigateTo(destinations: Destinations) {
        router.push(destinations)
    }

    private fun navigateBack() {
        router.pop()
    }


}
