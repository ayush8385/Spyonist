package com.cybera1.spyonist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
class MainViewModelProviderFactory(
    private val application: Application,
    private val sharedPrefManager: Preferences
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application, sharedPrefManager) as T
    }
}