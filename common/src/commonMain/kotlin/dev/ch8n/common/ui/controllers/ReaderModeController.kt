package dev.ch8n.common.ui.controllers

import com.arkivanov.decompose.ComponentContext
import dev.ch8n.common.data.remote.services.readerview.ReaderViewDTO
import dev.ch8n.common.domain.di.DomainInjector
import dev.ch8n.common.ui.navigation.Destination
import dev.ch8n.common.utils.DecomposeController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReaderModeController constructor(
    componentContext: ComponentContext,
    val navigateTo: (Destination) -> Unit,
    val onBack: () -> Unit,
    private val bookmarkUrl: String,
) : DecomposeController(componentContext) {

    private val readerService = DomainInjector
        .readerParserService

    private val _readerView = MutableStateFlow(ReaderViewDTO.empty)
    val reader = _readerView.asStateFlow()

    fun refreshReader() {
        launch(Dispatchers.IO) {
            val reader = readerService.getReaderViewContent(bookmarkUrl)
            _readerView.update { reader }
        }
    }

}