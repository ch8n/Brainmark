package dev.ch8n.common.data.local.database.config

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dev.ch8n.brainmark.SharedConfig
import dev.ch8n.sqlDB.BrainmarkDB


actual class SqlDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = BrainmarkDB.Schema,
            context = context.applicationContext,
            name = SharedConfig.SqlDelightDbName
        )
    }
}