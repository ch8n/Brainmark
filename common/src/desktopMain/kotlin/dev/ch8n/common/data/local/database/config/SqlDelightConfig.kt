package dev.ch8n.common.data.local.database.config

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import dev.ch8n.sqlDB.BrainmarkDB

actual class SqlDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BrainmarkDB.Schema.create(driver)
        return driver
    }
}