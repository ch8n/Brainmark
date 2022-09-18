package dev.ch8n.common.data.remote.services.readerview

expect class ReaderViewService {
    suspend fun getReaderViewContent(url: String): ReaderViewDTO
}