package com.cybera1.spyonist.Logs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LogsViewModelProviderFactory(
        private val application: Application,
        private val accessLogsRepo: AccessLogsRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccessLogsViewModel(application, accessLogsRepo) as T
    }
}