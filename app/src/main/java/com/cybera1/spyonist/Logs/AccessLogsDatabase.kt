package com.cybera1.spyonist.Logs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AccessLog::class], version = 1)
@TypeConverters(Converters::class)
abstract class AccessLogsDatabase: RoomDatabase() {

    abstract fun getAccessLogsDao():AccessLogsDao

    companion object {
        @Volatile
        private var instance: AccessLogsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(
                        context.applicationContext,
                        AccessLogsDatabase::class.java,
                        "access_logs_db.db"
                ).build()
    }
}