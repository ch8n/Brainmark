package dev.ch8n.common.data.local.database.config

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dev.ch8n.brainmark.SharedConfig
import dev.ch8n.common.foundations.ApplicationContext
import dev.ch8n.sqlDB.BrainmarkDB

actual class SqlDriverFactory actual constructor(
    private val applicationContext: ApplicationContext
) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = BrainmarkDB.Schema,
        context = applicationContext.context,
        name = SharedConfig.SqlDelightDbName
    )
}