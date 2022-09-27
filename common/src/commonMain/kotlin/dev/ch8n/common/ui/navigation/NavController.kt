package dev.ch8n.common.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import dev.ch8n.common.utils.UiController


fun createNavController(
    componentContext: ComponentContext,
    stackNavigator: StackNavigation<Destinations> = StackNavigation(),
    initialDestination: Destinations,
    createDestinations: NavControllerImpl.(destinations: Destinations, context: ComponentContext) -> UiController
): NavController = NavControllerImpl(
    componentContext = componentContext,
    navigation = stackNavigator,
    createDestinations = createDestinations,
    initialDestination = initialDestination
)


class EmptyNavController(
    private val onRouteTo: (Destinations) -> Unit = {},
    private val onBack: () -> Unit = {},
) : NavController {

    override val destinations = null

    override fun routeTo(destinations: Destinations) {
        onRouteTo(destinations)
    }

    override fun back() {
        onBack.invoke()
    }
}

interface NavController {
    val destinations: Value<ChildStack<Destinations, UiController>>?
    fun routeTo(destinations: Destinations)
    fun back()
}

class NavControllerImpl(
    private val componentContext: ComponentContext,
    private val navigation: StackNavigation<Destinations>,
    private val createDestinations: NavControllerImpl.(
        destinations: Destinations,
        context: ComponentContext
    ) -> UiController,
    private val initialDestination: Destinations
) : NavController {

    override val destinations = componentContext.childStack(
        source = navigation,
        initialConfiguration = initialDestination,
        handleBackButton = true,
        childFactory = { configuration: Destinations, componentContext: ComponentContext ->
            createDestinations(this, configuration, componentContext)
        },
    )

    override fun routeTo(destinations: Destinations) {
        navigation.push(destinations)
    }

    override fun back() {
        navigation.pop()
    }
}

