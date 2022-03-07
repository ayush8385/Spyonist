package com.cybera1.spyonist

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PixelFormat
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraManager.AvailabilityCallback
import android.media.AudioManager
import android.media.AudioManager.AudioRecordingCallback
import android.media.AudioRecordingConfiguration
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.cybera1.spyonist.Logs.AccessLog
import com.cybera1.spyonist.Logs.AccessLogsDatabase
import com.cybera1.spyonist.Logs.AccessLogsRepo
import com.cybera1.spyonist.Logs.IndicatorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.cybera1.spyonist.databinding.IndicatorsLayoutBinding
import android.app.KeyguardManager
import android.content.Context


class IndicatorService : AccessibilityService() {
    private lateinit var binding: IndicatorsLayoutBinding
    private var cameraManager: CameraManager? = null
    private var cameraCallback: AvailabilityCallback? = null
    private var audioManager: AudioManager? = null
    private var micCallback: AudioRecordingCallback? = null
    private lateinit var sharedPrefManager: Preferences
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private var isCameraOn = false
    private var isMicOn = false
    lateinit var accessLogsRepo:AccessLogsRepo
    private var currentAppId = BuildConfig.APPLICATION_ID
    var mp:MediaPlayer?=null


    override fun onCreate() {
        super.onCreate()
        fetchData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        createOverlay()
        setUpInnerViews()

        startCallBacks()

    }

    private fun fetchData() {
        mp= MediaPlayer.create(this,R.raw.emergency_alert)
        sharedPrefManager = Preferences.getInstance(applicationContext)!!
        accessLogsRepo = AccessLogsRepo(AccessLogsDatabase(this))
    }

    private fun startCallBacks() {
        if (cameraManager == null) cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraManager!!.registerAvailabilityCallback(getCameraCallback(), null)

        if (audioManager == null) audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager!!.registerAudioRecordingCallback(getMicCallback(), null)

    }

    private fun getCameraCallback(): AvailabilityCallback {
        cameraCallback = object : AvailabilityCallback() {
            override fun onCameraAvailable(cameraId: String) {
                super.onCameraAvailable(cameraId)
                isCameraOn = false
                hideCam()
            }

            override fun onCameraUnavailable(cameraId: String) {
                super.onCameraUnavailable(cameraId)
                val km = applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if(km.inKeyguardRestrictedInputMode()){
                    if(sharedPrefManager.getBoolean(this@IndicatorService,"lockScreen",false)){
                        isCameraOn = true
                        showCam()
                        triggerVibration()
                    }
                }
                else{
                    if(!sharedPrefManager.getappsAcces(currentAppId)){
                        isCameraOn = true
                        showCam()
                        triggerVibration()
                    }
                }

            }
        }
        return cameraCallback as AvailabilityCallback
    }

    private fun getMicCallback(): AudioRecordingCallback {
        micCallback = object : AudioRecordingCallback() {
            override fun onRecordingConfigChanged(configs: List<AudioRecordingConfiguration>) {
                if (configs.size > 0) {
                    if(!sharedPrefManager.getappsAcces(currentAppId)){
                        isMicOn = true
                        showMic()
                        triggerVibration()
                    }

                } else {
                    isMicOn = false
                    hideMic()
                }
            }
        }
        return micCallback as AudioRecordingCallback
    }


    private fun triggerVibration() {
        if (sharedPrefManager.isSoundEnabled) {
            mp =MediaPlayer.create(this,R.raw.emergency_alert)
            mp!!.start()
            val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                v.vibrate(500)
            }
        }
    }

    private fun setUpInnerViews() {
        setViewColors()
        showInitialAnimation(true)
    }

    private fun showInitialAnimation(isEnabled: Boolean) {
        val delay = if (isEnabled) 1000 else 0
        binding.ivCam.postDelayed({
            binding.ivCam.visibility = View.GONE
            binding.ivMic.visibility = View.GONE
        }, delay.toLong())
    }

    private fun createOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        layoutParams.apply {
            type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = layoutGravity
        }
        binding = IndicatorsLayoutBinding.inflate(LayoutInflater.from(this))
        windowManager.addView(binding.root, layoutParams)
    }

    private fun updateLayoutGravity() {
        layoutParams.gravity = layoutGravity
        windowManager.updateViewLayout(binding.root, layoutParams)
    }

    private val layoutGravity: Int
        get() =sharedPrefManager.indicatorPosition.layoutGravity

    private fun makeLog(indicatorType: IndicatorType) {
        if (isLogEligible(currentAppId)) {
            val log = AccessLog(System.currentTimeMillis(), currentAppId, getAppName(currentAppId), indicatorType)
            GlobalScope.launch(Dispatchers.IO) {
                accessLogsRepo.save(log)
            }
        }
    }

    private fun getAppName(packageName: String): String {
        val packageManager = applicationContext.packageManager
        return try {
            packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)) as String
        } catch (exp: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    private fun isLogEligible(currentAppId: String): Boolean {
        return currentAppId != BuildConfig.APPLICATION_ID
    }


    private fun showMic() {
        if (sharedPrefManager.isMicIndicatorEnabled) {
            updateIndicatorProperties()
            binding.ivMic.visibility = View.VISIBLE
            makeLog(IndicatorType.MICROPHONE)
        }
    }

    private fun hideMic() {
        binding.ivMic.visibility = View.GONE
        if(mp!!.isPlaying()==true){
            mp!!.stop()
        }
    }

    private fun showCam() {
        if (sharedPrefManager.isCameraIndicatorEnabled) {
            updateIndicatorProperties()
            binding.ivCam.visibility = View.VISIBLE
            makeLog(IndicatorType.CAMERA)
        }
    }

    private fun hideCam() {
        binding.ivCam.visibility = View.GONE
        if(mp!!.isPlaying()==true){
            mp!!.stop()
        }
    }

    private fun updateIndicatorProperties() {
        updateLayoutGravity()
        updateIndicatorsSizeAndOpacity()
        setViewColors()
    }

    private fun setViewColors() {
        val dotsTint = sharedPrefManager.indicatorColor
        val indicatorBackground = sharedPrefManager.indicatorBackgroundColor
        binding.ivCam.setViewTint(dotsTint!!)
        binding.ivMic.setViewTint(dotsTint)
        binding.llBackground.setBackgroundColor(Color.parseColor(indicatorBackground))
    }

    private fun updateIndicatorsSizeAndOpacity() {
        val size = 60
        binding.cvIndicators.radius = (size / 2).toFloat()
        binding.ivCam.updateSize(size)
        binding.ivMic.updateSize(size)
        binding.root.updateOpacity(0.8F)
    }

    override fun onInterrupt() {}
    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent) {
        if (accessibilityEvent.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            && accessibilityEvent.packageName != null) {
            currentAppId = try {
                val componentName = ComponentName(accessibilityEvent.packageName.toString(), accessibilityEvent.className.toString())
                componentName.packageName
            } catch (exp: Exception) {
                BuildConfig.APPLICATION_ID
            }
        }
    }

    private fun unRegisterCameraCallBack() {
        if (cameraManager != null
            && cameraCallback != null) {
            cameraManager!!.unregisterAvailabilityCallback(cameraCallback!!)
        }
    }

    private fun unRegisterMicCallback() {
        if (audioManager != null
            && micCallback != null) {
            audioManager!!.unregisterAudioRecordingCallback(micCallback!!)
        }
    }

    override fun onDestroy() {
        unRegisterCameraCallBack()
        unRegisterMicCallback()
        super.onDestroy()
    }
}