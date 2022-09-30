package dev.ch8n.common.domain.di

import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.data.remote.services.htmlParser.HtmlParserService
import dev.ch8n.common.data.remote.services.readerview.ReaderViewService
import dev.ch8n.common.domain.usecases.*
import io.ktor.client.*

object DomainResolver {

    private val ktorHttpClient by lazy { HttpClient() }
    fun provideHtmlParser(): HtmlParserService {
        return HtmlParserService(ktorHttpClient)
    }

    fun provideBookmarkUseCase(): BookmarkUseCases {
        val bookmarkDataSource = DataInjector.bookmarkDataSource
        return BookmarkUseCases(
            getBookmarkById = GetBookmarkByIdUseCase(bookmarkDataSource),
            upsertBookmark = UpsertBookmarkUseCase(bookmarkDataSource),
            deleteBookmark = DeleteBookmarkUseCase(bookmarkDataSource),
            getBookmarkByUrl = GetBookmarkByUrlUseCase(bookmarkDataSource),
            getAllBookmarksPaging = GetBookmarksPaging(bookmarkDataSource),
            getBookmarksByTagPaging = GetBookmarkByTagPaging(bookmarkDataSource),
            searchAllBookmarkPaging = SearchAllBookmarkPaging(bookmarkDataSource),
            searchBookmarkByTagPaging = SearchBookmarkByTagPaging(bookmarkDataSource),
            getBookmarksByLastReadPaging = GetBookmarksByLastReadPaging(bookmarkDataSource),
            getReadingRecommendations = GetReadingRecommendations(bookmarkDataSource),
            getRevisionRecommendations = GetRevisionRecommendations(bookmarkDataSource),
            searchUntaggedBookmarkPaging = SearchUntaggedBookmarkPaging(bookmarkDataSource),
            getUntaggedBookmarks = GetUntaggedBookmarkPaging(bookmarkDataSource)
        )
    }

    fun provideTagUseCase(): TagUseCases {
        val tagsDataSource = DataInjector.tagDataSource
        return TagUseCases(
            getAllTags = GetAllTagsUseCase(tagsDataSource),
            getTagById = GetTagByIdUseCase(tagsDataSource),
            getTagsByIds = GetTagsByIdsUseCase(tagsDataSource),
            upsertTag = UpsertTagUseCase(tagsDataSource),
            deleteTag = DeleteTagUseCase(tagsDataSource),
            getTagByName = GetTagByNameUseCase(tagsDataSource)
        )
    }

    fun provideReaderParser() = ReaderViewService()
}

object DomainInjector {

    val bookmarkUseCase by lazy {
        DomainResolver.provideBookmarkUseCase()
    }
    val tagUseCase by lazy {
        DomainResolver.provideTagUseCase()
    }
    val htmlParserService by lazy {
        DomainResolver.provideHtmlParser()
    }

    val readerParserService by lazy {
        DomainResolver.provideReaderParser()
    }

}