package dev.ch8n.common.data.local.database.config

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dev.ch8n.brainmark.SharedConfig
import dev.ch8n.common.utils.PlatformDependencies
import dev.ch8n.sqlDB.BrainmarkDB


actual class SqlDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        val appContext = PlatformDependencies.appContext
        return AndroidSqliteDriver(
            schema = BrainmarkDB.Schema,
            context = appContext.applicationContext,
            name = SharedConfig.SqlDelightDbName
        )
    }
}
