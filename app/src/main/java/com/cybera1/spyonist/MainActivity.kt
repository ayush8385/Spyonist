package com.cybera1.spyonist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils.SimpleStringSplitter
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cybera1.spyonist.Logs.AccessLogsActivity
import com.cybera1.spyonist.databinding.ActivityMainBinding
import com.cybera1.spyonist.databinding.ContentServiceEnabledBinding
import com.cybera1.spyonist.Logs.IndicatorPosition


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var serviceEnabledBinding: ContentServiceEnabledBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceEnabledBinding = binding.contentServiceEnabled
        val viewModelProviderFactory = MainViewModelProviderFactory(application,
            Preferences.getInstance(applicationContext)!!
        )
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)


        viewModel.cameraIndicatorStatus.observe(this, {
            serviceEnabledBinding.switchCamera.isChecked = it
            binding.indicatorsLayout.ivCam.visibility = if(it==true) View.VISIBLE else View.GONE
        })

        viewModel.microphoneIndicatorStatus.observe(this, {
            serviceEnabledBinding.switchMic.isChecked = it
            binding.indicatorsLayout.ivMic.visibility = if(it==true) View.VISIBLE else View.GONE
        })

        viewModel.vibrationAlertStatus.observe(this, {
            serviceEnabledBinding.switchSound.isChecked = it
        })

        serviceEnabledBinding.multiSwitchHorizontalHeight.selectedTab=viewModel.indicatorPosition.value!!.horizontal

//        val mainIntent = Intent(Intent.ACTION_MAIN, null)
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
//        val pkgAppsList: List<ResolveInfo> =
//            this.getPackageManager().queryIntentActivities(mainIntent, 0)
//
//        Log.e("sajkdbkas...........",pkgAppsList.toString())
//        Toast.makeText(applicationContext,pkgAppsList.toString(),Toast.LENGTH_LONG).show()


        // Passing the column number 1 to show online one column in each row.
//        recyclerViewLayoutManager = GridLayoutManager(this@MainActivity, 1)
//
//        recyclerView.setLayoutManager(recyclerViewLayoutManager)



        viewModel.indicatorBackgroundColor.observe(this, {
            binding.indicatorsLayout.llBackground.setBackgroundColor(Color.parseColor(it))
        })

        viewModel.indicatorForegroundColor.observe(this, {
            binding.indicatorsLayout.ivCam.setViewTint(it!!)
            binding.indicatorsLayout.ivMic.setViewTint(it)
        })



        binding.mainSwitch.setOnCheckedChangeListener { button, isEnabled ->
            if(isEnabled){
                if(isAccessibilityServiceEnabled(applicationContext))
                    serviceEnabled()
                else
                    openAccessibilitySettingsPage(isEnabled)
            }else {
                if(!isAccessibilityServiceEnabled(applicationContext))
                    serviceDisabled()
                else
                    openAccessibilitySettingsPage(isEnabled)
            }
        }

        serviceEnabledBinding.switchCamera.setOnCheckedChangeListener { button, isEnabled ->
            viewModel.setCameraIndicatorStatus(isEnabled)
        }

        serviceEnabledBinding.multiSwitchHorizontalHeight.setOnSwitchListener { horizontal, _ ->
//            val vertical = serviceEnabledBinding.multiSwitchHorizontalHeight.selectedTab
//            Toast.makeText(applicationContext,vertical.toString()+" "+horizontal.toString(),Toast.LENGTH_LONG).show()
            viewModel.setIndicatorPosition(IndicatorPosition.getIndicatorPosition(0, horizontal))
        }


        serviceEnabledBinding.switchMic.setOnCheckedChangeListener { button, isEnabled ->
            viewModel.setMicrophoneIndicatorStatus(isEnabled)
        }

        serviceEnabledBinding.switchSound.setOnCheckedChangeListener { button, isEnabled ->
            viewModel.setVibrationAlertStatus(isEnabled)
        }

        serviceEnabledBinding.logsText.setOnClickListener {
            openAccessLogsScreen()
        }

        serviceEnabledBinding.accessText.setOnClickListener {
            openAppAccessScreen()
        }

        serviceEnabledBinding.settingsText.setOnClickListener {
            openCustomizationScreen()
        }

        serviceEnabledBinding.switchSettings.setOnClickListener {
            openCustomizationScreen()
        }


    }

    private fun openAppAccessScreen() {
        this.goToActivity(AppsAccessActivity::class.java)
    }

    fun openAccessLogsScreen() {
        this.goToActivity(AccessLogsActivity::class.java)
    }

    private fun openCustomizationScreen() {
        val customizationFragment = Customization()
        supportFragmentManager.beginTransaction().apply {
            replace(binding.homeFragmentContainer.id, customizationFragment)
            addToBackStack("CUSTOMIZATION_SCREEN")
            commit()
        }
    }

    private fun openAccessibilitySettingsPage(isServiceDisabled:Boolean) {
        if (isServiceDisabled){
            Toast.makeText(this, "Turn On Spyonist", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Turn Off Spyonist", Toast.LENGTH_LONG).show()
        }
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    override fun onResume() {
        super.onResume()
        if (isAccessibilityServiceEnabled(applicationContext)) serviceEnabled() else serviceDisabled()
    }

    private fun isAccessibilityServiceEnabled(mContext: Context): Boolean {
        if(BuildConfig.IN_APP_TESTING_TOGGLE) return true
        val APPLICATION_ID = BuildConfig.APPLICATION_ID
        val ACCESSIBILITY_SERVICE = IndicatorService::class.java.canonicalName
        var accessibilityEnabled = 0
        val service = "$APPLICATION_ID/$ACCESSIBILITY_SERVICE"
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.applicationContext.contentResolver,
                    Settings.Secure.ACCESSIBILITY_ENABLED)
        } catch (e: SettingNotFoundException) {

        }
        val mStringColonSplitter = SimpleStringSplitter(':')
        if (accessibilityEnabled == 1) {
            val settingValue = Settings.Secure.getString(
                    mContext.applicationContext.contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue)
                while (mStringColonSplitter.hasNext()) {
                    val accessibilityService = mStringColonSplitter.next()
                    if (accessibilityService.equals(service, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun serviceEnabled() {
        binding.mainSwitch.isChecked = true
        binding.mainSwitch.text = "Enabled"
        binding.contentServiceDisabled.root.visibility = View.GONE
        serviceEnabledBinding.root.visibility = View.VISIBLE
        binding.contentCredits.root.visibility = View.VISIBLE
        binding.indicatorsLayout.root.visibility = View.VISIBLE
    }

    private fun serviceDisabled() {
        binding.mainSwitch.isChecked = false
        binding.mainSwitch.text = "Disabled"
        binding.contentServiceDisabled.root.visibility = View.VISIBLE
        serviceEnabledBinding.root.visibility = View.GONE
        binding.contentCredits.root.visibility = View.GONE
        binding.indicatorsLayout.root.visibility = View.GONE
    }
}