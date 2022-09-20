package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController

class WebViewController constructor(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
    val bookmarkUrl: String,
) : DecomposeController(componentContext)