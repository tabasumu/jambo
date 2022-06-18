package com.tabasumu.jambo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JamboLogRepository(private val db: JamboLocalDB) {

    private val jamboLogDao: JamboLogDao by lazy {
        db.jamboDao()
    }

    suspend fun saveLogs(vararg jamboLog: JamboLog) {
        withContext(Dispatchers.IO) {
            jamboLogDao.insert(*jamboLog)
        }
    }

    suspend fun clearLogs() {
        withContext(Dispatchers.IO) {
            jamboLogDao.deleteAll()
        }
    }

    fun getLogs(tag: LogType = LogType.ALL, message: String = "") = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {
            when (tag) {
                LogType.ALL -> jamboLogDao.searchJamboLogs(message = message)
                else -> jamboLogDao.filterJamboLogWithTagAndMsg(tag = tag.name, message = message)
            }

        }
    ).flow
}