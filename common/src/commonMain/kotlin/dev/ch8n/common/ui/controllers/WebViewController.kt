package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.ui.navigation.Destination
import dev.ch8n.common.utils.DecomposeController

class WebViewController constructor(
    componentContext: ComponentContext,
    val navigateTo: (Destination) -> Unit,
    val onBack: () -> Unit,
    val bookmarkUrl: String,
) : DecomposeController(componentContext)