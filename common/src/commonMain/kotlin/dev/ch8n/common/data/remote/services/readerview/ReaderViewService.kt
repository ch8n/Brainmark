package dev.ch8n.common.data.remote.services.readerview

expect class ReaderViewService constructor() {
    suspend fun getReaderViewContent(url: String): ReaderViewDTO
}