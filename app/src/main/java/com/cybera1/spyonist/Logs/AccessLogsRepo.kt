package com.cybera1.spyonist.Logs


class AccessLogsRepo(
        val db: AccessLogsDatabase
) {
    suspend fun save(accessLog: AccessLog) = db.getAccessLogsDao().upsert(accessLog)

    suspend fun clear() = db.getAccessLogsDao().clearLogs()

    fun fetchAll() = db.getAccessLogsDao().getAllLogs()
}