package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.model.Tags
import dev.ch8n.common.ui.navigation.Destinations
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.flow.MutableStateFlow

class BrowserController(
    componentContext: ComponentContext,
    val navigateTo: (Destinations) -> Unit,
    val onBack: () -> Unit,
) : DecomposeController(componentContext) {

    val bookmark = MutableStateFlow(Bookmark.SAMPLE)
    val tags = MutableStateFlow(
        listOf(
            Tags.TAG_JAVA,
            Tags.TAG_KMM,
            Tags.TAG_KOTLIN,
            Tags.TAG_KMP,
            Tags.TAG_WEB_DEV,
        )
    )
}