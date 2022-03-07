package com.cybera1.spyonist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.cybera1.spyonist.Logs.IndicatorPosition

class MainViewModel(application: Application,
                    private val sharedPrefManager: Preferences
) : AndroidViewModel(application) {

    val cameraIndicatorStatus = MutableLiveData(sharedPrefManager.isCameraIndicatorEnabled)

    val microphoneIndicatorStatus = MutableLiveData(sharedPrefManager.isMicIndicatorEnabled)

    val vibrationAlertStatus = MutableLiveData(sharedPrefManager.isSoundEnabled)

    val indicatorForegroundColor = MutableLiveData(sharedPrefManager.indicatorColor)

    val indicatorPosition = MutableLiveData(sharedPrefManager.indicatorPosition)

    val indicatorBackgroundColor = MutableLiveData(sharedPrefManager.indicatorBackgroundColor)


    fun setCameraIndicatorStatus(isEnabled: Boolean) {
        sharedPrefManager.isCameraIndicatorEnabled = isEnabled
        cameraIndicatorStatus.value = isEnabled
    }

    fun setMicrophoneIndicatorStatus(isEnabled: Boolean) {
        sharedPrefManager.isMicIndicatorEnabled = isEnabled
        microphoneIndicatorStatus.value = isEnabled
    }

    fun setIndicatorPosition(position: IndicatorPosition) {
        sharedPrefManager.indicatorPosition = position
        indicatorPosition.value = position
    }

    fun setVibrationAlertStatus(isEnabled: Boolean) {
        sharedPrefManager.isSoundEnabled = isEnabled
        vibrationAlertStatus.value = isEnabled
    }


    fun setIndicatorForegroundColor(color: String) {
        sharedPrefManager.indicatorColor = color
        indicatorForegroundColor.value = color
    }

    fun setIndicatorBackgroundColor(color: String) {
        sharedPrefManager.indicatorBackgroundColor = color
        indicatorBackgroundColor.value = color
    }



}