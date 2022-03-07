package com.cybera1.spyonist.Logs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cybera1.spyonist.AppsAccessActivity
import com.cybera1.spyonist.MainActivity
import com.cybera1.spyonist.R
import com.cybera1.spyonist.databinding.AppBarBinding

class AppBarFragment: Fragment(R.layout.app_bar) {

    private lateinit var binding: AppBarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AppBarBinding.bind(view)

        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {
        binding.tvBarHeader.text = when(activity){
            is AppsAccessActivity-> "Apps Access"
            is MainActivity -> "Customize Indicators"
            is AccessLogsActivity -> "Indicator Logs"
            else -> ""
        }
    }

    private fun setUpListeners() {
        binding.ivBackButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}