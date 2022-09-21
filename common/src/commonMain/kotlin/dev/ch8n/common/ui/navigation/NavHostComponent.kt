package dev.ch8n.common.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.*
import com.arkivanov.decompose.value.Value
import dev.ch8n.common.ui.controllers.*
import dev.ch8n.common.utils.DecomposeController


class NavHostComponent(
    componentContext: ComponentContext,
) : DecomposeController(componentContext) {

    private val router: Router<Destination, DecomposeController> = router(
        initialConfiguration = Destination.Home,
        childFactory = ::createComponents,
        handleBackButton = true
    )

    private fun createComponents(
        destination: Destination,
        context: ComponentContext
    ): DecomposeController = when (destination) {

        is Destination.Bookmarks -> BookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destination.Home -> HomeController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destination.TagManager -> TagManagerController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destination.CreateBookmark -> CreateBookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
        )

        is Destination.PreviewBookmark -> PreviewBookmarkController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkId = destination.bookmarkId
        )

        is Destination.ReaderScreen -> ReaderModeController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkUrl = destination.url
        )

        is Destination.WebView -> WebViewController(
            componentContext = context,
            navigateTo = ::navigateTo,
            onBack = ::navigateBack,
            bookmarkUrl = destination.url
        )
    }

    val rootRouterState: Value<RouterState<Destination, DecomposeController>> = router.state

    private fun navigateTo(destination: Destination) {
        router.push(destination)
    }

    private fun navigateBack() {
        router.pop()
    }


}
