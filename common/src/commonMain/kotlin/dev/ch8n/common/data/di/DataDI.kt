package dev.ch8n.common.data.di

import dev.ch8n.common.data.local.database.config.SqlDriverFactory
import dev.ch8n.common.data.local.database.config.createDatabase
import dev.ch8n.common.data.local.database.sources.BookmarkDataSource
import dev.ch8n.common.data.local.database.sources.BookmarkDataSourceImpl
import dev.ch8n.common.data.local.database.sources.TagsDataSource
import dev.ch8n.common.data.local.database.sources.TagsDataSourceImpl
import dev.ch8n.sqlDB.BrainmarkDB

object DataResolver {

    fun provideSqlDriver(): SqlDriverFactory {
        return SqlDriverFactory()
    }
    fun provideDatabase(sqlDriverFactory: SqlDriverFactory): BrainmarkDB {
        return createDatabase(sqlDriverFactory)
    }
    fun provideBookmarkDataSource(database: BrainmarkDB): BookmarkDataSource {
        return BookmarkDataSourceImpl(database)
    }

    fun provideTagDataSource(database: BrainmarkDB): TagsDataSource {
        return TagsDataSourceImpl(database)
    }

}

object DataInjector {
    private val sqlDriver by lazy {
        DataResolver.provideSqlDriver()
    }

    private val appDatabase by lazy {
        DataResolver.provideDatabase(sqlDriver)
    }

    val bookmarkDataSource by lazy {
        DataResolver.provideBookmarkDataSource(appDatabase)
    }

    val tagDataSource by lazy {
        DataResolver.provideTagDataSource(appDatabase)
    }
}