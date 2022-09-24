package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController

abstract class PreviewEmbeddedWebController constructor(
    navController: NavController,
    val bookmark: Bookmark
) : UiController(navController)