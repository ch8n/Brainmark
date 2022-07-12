package dev.ch8n.common.domain.di

import dev.ch8n.common.data.di.DataInjector
import dev.ch8n.common.domain.usecases.*

object DomainResolver {

    fun provideBookmarkUseCase(): BookmarkUseCases {
        val bookmarkDataSource = DataInjector.bookmarkDataSource
        return BookmarkUseCases(
            getAllBookmarkUseCase = GetAllBookmarkUseCase(bookmarkDataSource),
            getBookmarkByIdUseCase = GetBookmarkByIdUseCase(bookmarkDataSource),
            createBookmarkUseCase = CreateBookmarkUseCase(bookmarkDataSource),
            updateBookmarkUseCase = UpdateBookmarkUseCase(bookmarkDataSource),
            deleteBookmarkUseCase = DeleteBookmarkUseCase(bookmarkDataSource)
        )
    }

    fun provideTagUseCase(): TagUseCases {
        val tagsDataSource = DataInjector.tagDataSource
        val getTagByIdUseCase = GetTagByIdUseCase(tagsDataSource)
        return TagUseCases(
            getAllTagsUseCase = GetAllTagsUseCase(tagsDataSource),
            getTagByIdUseCase = getTagByIdUseCase,
            getTagsByIdsUseCase = GetTagsByIdsUseCase(tagsDataSource),
            createTagUseCase = CreateTagUseCase(tagsDataSource),
            updateTagUseCase = UpdateTagUseCase(tagsDataSource, getTagByIdUseCase),
            deleteTagUseCase = DeleteTagUseCase(tagsDataSource),
        )
    }
}

object DomainInjector {
    val bookmarkUseCase by lazy {
        DomainResolver.provideBookmarkUseCase()
    }
    val tagUseCase by lazy {
        DomainResolver.provideTagUseCase()
    }
}