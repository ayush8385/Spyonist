package com.cybera1.spyonist

import android.content.Context
import android.content.SharedPreferences
import com.cybera1.spyonist.Logs.IndicatorPosition

class Preferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    fun setappsAccess(context: Context,appPackage:String){
        appsAcccess!!.add(appPackage)
    }
    fun getappsAcces(appPackage: String):Boolean{
        if(!appsAcccess!!.isEmpty()){
            for(apps in appsAcccess!!){
                if(apps==appPackage){
                    return true
                }
            }
        }
        return false

    }

    fun removeappsAccess(context1: Context, applicationPackageName: String) {
        if(getappsAcces(applicationPackageName)){
            appsAcccess!!.remove(applicationPackageName)

        }
    }

    fun setString(context: Context?, key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String?, def_value: String?): String? {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
        return sharedPreferences.getString(key, def_value)
    }

    fun setInteger(context: Context, key: String?, value: Int) {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInteger(context: Context, key: String?, def_value: Int): Int {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
        return sharedPreferences.getInt(key, def_value)
    }

    fun setBoolean(context: Context, key: String?, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String?, def_value: Boolean): Boolean {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
        return sharedPreferences.getBoolean(key, def_value)
    }





    var isCameraIndicatorEnabled: Boolean
        get() = getBoolean(context, "CAMERA_ENABLED", DEFAULT_CAMERA_ENABLED)
        set(value) {
            setBoolean(context, "CAMERA_ENABLED", value)
        }
    var isMicIndicatorEnabled: Boolean
        get() = getBoolean(context, MIC_ENABLED, DEFAULT_MIC_ENABLED)
        set(value) {
            setBoolean(context, MIC_ENABLED, value)
        }
    var isSoundEnabled: Boolean
        get() = getBoolean(context, VIB_ENABLED, DEFAULT_VIB_ENABLED)
        set(value) {
            setBoolean(context, VIB_ENABLED, value)
        }
    var indicatorColor: String?
        get() = getString(context, INDICATOR_COLOR, DEFAULT_INDICATOR_COLOR)
        set(value) {
            setString(context, INDICATOR_COLOR, value)
        }
    var indicatorBackgroundColor: String?
        get() = getString(context, INDICATOR_BACKGROUND_COLOR, DEFAULT_INDICATOR_BACKGROUND_COLOR)
        set(value) {
            setString(context, INDICATOR_BACKGROUND_COLOR, value)
        }

    var indicatorPosition: IndicatorPosition
        get() = IndicatorPosition.valueOf(getString(context, INDICATOR_POSITION, DEFAULT_INDICATOR_POSITION)!!)
        set(value) {
            setString(context, INDICATOR_POSITION, value.name)
        }

    companion object {
        private const val SP_NAME = BuildConfig.APPLICATION_ID
        private const val ACCESS_MODE = Context.MODE_PRIVATE
        private const val MIC_ENABLED = "MIC_ENABLED"
        private const val NOTIFICATION_ENABLED = "NOTIFICATION_ENABLED"
        private const val LOC_ENABLED = "LOCATION_ENABLED"
        private const val VIB_ENABLED = "VIBRATION_ENABLED"
        private const val INDICATOR_COLOR = "INDICATOR_COLOR"
        private const val INDICATOR_POSITION = "INDICATOR_POSITION"
        private const val INDICATOR_BACKGROUND_COLOR = "INDICATOR_BACKGROUND_COLOR"
        private const val DEFAULT_INDICATOR_COLOR = "#FFFFFF"
        private const val DEFAULT_INDICATOR_BACKGROUND_COLOR = "#000000"
        private const val DEFAULT_CAMERA_ENABLED = true
        private const val DEFAULT_MIC_ENABLED = true
        private const val DEFAULT_LOC_ENABLED = false
        private const val DEFAULT_NOTIFICATION_ENABLED = false
        private const val DEFAULT_VIB_ENABLED = false
        private val DEFAULT_INDICATOR_POSITION: String = IndicatorPosition.TOP_RIGHT.name
        private var sharedPrefManager: Preferences? = null
        private var appsAcccess:ArrayList<String> = arrayListOf<String>()
        private var lockScreen:Boolean=true
        fun getInstance(context: Context): Preferences? {
            if (null == sharedPrefManager) {
                sharedPrefManager = Preferences(context)
            }
            return sharedPrefManager
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE)
//        if(!getappsAcces("com.android.systemui")){
//            appsAcccess.add("com.android.systemui")
//        }
    }
}