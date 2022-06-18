package com.tabasumu.jambo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tabasumu.jambo.data.JamboLocalDB
import com.tabasumu.jambo.data.JamboLog
import com.tabasumu.jambo.data.JamboLogRepository
import com.tabasumu.jambo.data.LogType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/10/22 at 3:18 PM
 */
class JamboViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository = JamboLogRepository(JamboLocalDB.instance(application))

    private val tag = MutableStateFlow(LogType.ALL)
    private val query = MutableStateFlow("")

    private val _logs = combine(tag, query) { tag, query ->
        Pair(tag, query)
    }.flatMapLatest { (tag, query) -> repository.getLogs(tag, query) }
    val logs = _logs

    private val _selectedLog = MutableStateFlow<JamboLog?>(null)
    val selectedLog get() = _selectedLog

    fun deleteAll() {
        viewModelScope.launch {
            repository.clearLogs()
        }
    }

    fun saveLog(log: JamboLog) {
        viewModelScope.launch {
            repository.saveLogs(log)
        }
    }

    fun updateTagFilter(logType: LogType) {
        tag.value = logType
    }

    fun updateSearchQuery(message: String) {
        query.value = message
    }

    fun selectLog(log: JamboLog?) {
        _selectedLog.value = log
    }

}