package dev.ch8n.common.data.local.database.config

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import dev.ch8n.common.BookmarkEntity
import dev.ch8n.common.foundations.ApplicationContext
import dev.ch8n.sqlDB.BrainmarkDB


expect class SqlDriverFactory(applicationContext: ApplicationContext) {
    fun createDriver(): SqlDriver
}

private val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

    override fun encode(value: List<String>) = value.joinToString(separator = ",")
}

fun createDatabase(factory: SqlDriverFactory): BrainmarkDB {
    val driver = factory.createDriver()
    return BrainmarkDB(
        driver = driver,
        BookmarkEntityAdapter = BookmarkEntity.Adapter(
            tagsIdsAdapter = listOfStringsAdapter
        )
    )
}

