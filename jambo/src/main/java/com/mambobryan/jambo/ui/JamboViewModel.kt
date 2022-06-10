package com.mambobryan.jambo.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mambobryan.jambo.data.JamboLocalDB
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.JamboLogRepository
import com.mambobryan.jambo.data.LogType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*

/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/10/22 at 3:18 PM
 */
class JamboViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = JamboLogRepository(JamboLocalDB.instance(application))

    private val tag = MutableStateFlow(LogType.ALL)
    private val query = MutableStateFlow("")

    private val _logs = combine(tag, query) { tag, query ->
        Pair(tag, query)
    }.flatMapLatest { (tag, query) -> repository.getLogs(tag, query) }
    val logs = _logs

    init {
        viewModelScope.launch {
            repository.saveLogs(
                JamboLog(
                    tag = "I/Main",
                    packageName = "com.mambo.me",
                    message = "I was logged recent ${Calendar.getInstance().time}",
                    type = LogType.INFO
                )
            )
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.clearLogs()
        }
    }

    fun updateSearchQuery(message: String) {
        viewModelScope.launch {
            query.value = message
        }
    }

}