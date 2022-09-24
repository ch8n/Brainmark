package dev.ch8n.common.ui.controllers

import dev.ch8n.common.data.model.Bookmark
import dev.ch8n.common.data.remote.services.readerview.ReaderViewDTO
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.NavController
import dev.ch8n.common.utils.UiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class PreviewReaderModeController constructor(
    navController: NavController,
    private val bookmark: Bookmark
) : UiController(navController) {

    private val readerService = DomainInjector
        .readerParserService

    private val _readerView = MutableStateFlow(ReaderViewDTO.empty)
    val reader = _readerView.asStateFlow()

    fun refreshReader() {
        launch(Dispatchers.IO) {
            val reader = readerService
                .getReaderViewContent(bookmark.bookmarkUrl)
            _readerView.update { reader }
        }
    }

}