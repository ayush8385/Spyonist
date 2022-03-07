package com.cybera1.spyonist

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.cybera1.spyonist.databinding.ContentCustomizationBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.cybera1.spyonist.databinding.ActivityCustomizationBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.switchmaterial.SwitchMaterial

class Customization : Fragment(R.layout.activity_customization) {

    lateinit var binding: ActivityCustomizationBinding

    lateinit var viewModel: MainViewModel



    lateinit var customizationBinding: ContentCustomizationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityCustomizationBinding.bind(view)

        customizationBinding = binding.contentCustomization
        viewModel = (activity as MainActivity).viewModel



        setUpObservers()
        setUpListeners()


    }


    private fun setUpObservers() {
        viewModel.indicatorForegroundColor.observe(viewLifecycleOwner, {
            customizationBinding.titleForeGround.setViewTint(it!!)
            binding.indicatorsLayout.ivCam.setViewTint(it)
            binding.indicatorsLayout.ivMic.setViewTint(it)
        })

        viewModel.indicatorBackgroundColor.observe(viewLifecycleOwner, {
            customizationBinding.titleBackGround.setViewTint(it!!)
            binding.indicatorsLayout.llBackground.setBackgroundColor(Color.parseColor(it))
        })

    }

    private fun setUpListeners() {
        customizationBinding.titleForeGround.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("Indicator Foreground Color")
                .setColorShape(ColorShape.CIRCLE)
                .setDefaultColor(viewModel.indicatorForegroundColor.value!!)
                .setColorListener { _, colorHex ->
                    viewModel.setIndicatorForegroundColor(colorHex)
                }.show()
        }

        customizationBinding.titleBackGround.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("Indicator Background Color")
                .setColorShape(ColorShape.CIRCLE)
                .setDefaultColor(viewModel.indicatorBackgroundColor.value!!)
                .setColorListener { _, colorHex ->
                    viewModel.setIndicatorBackgroundColor(colorHex)
                }.show()
        }



    }
}