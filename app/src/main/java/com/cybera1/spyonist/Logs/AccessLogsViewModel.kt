package com.cybera1.spyonist.Logs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AccessLogsViewModel(app: Application, private val accessLogsRepo: AccessLogsRepo): AndroidViewModel(app) {

    val allAccessLogs = accessLogsRepo.fetchAll()

    fun clearAllLogs() = viewModelScope.launch {
        accessLogsRepo.clear()
    }

}