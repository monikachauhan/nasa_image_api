package com.example.astronomicalphotooftheday.presentation.main_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.example.astronomicalphotooftheday.R
import com.example.astronomicalphotooftheday.databinding.FragmentMainBinding
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
          cvToday.setOnClickListener {
                findNavController().navigate(R.id.navigateToApodTodayFragment)
            }
            cvRandom.setOnClickListener {
                findNavController().navigate(R.id.navigateToApodItemsFragment)
            }
            switchMode.setOnCheckedChangeListener { _, isChecked ->

                // if the button is checked, i.e., towards the right or enabled
                // enable dark mode, change the text to disable dark mode
                // else keep the switch text to enable dark mode
                if (switchMode.isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchMode.text = "Enable dark mode"

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchMode.text = "Disable dark mode"

                 }
            }
        }

    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)
}