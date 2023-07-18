package com.motodo.todo.presentation.fragmentSettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.motodo.todo.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClicks()
    }

    private fun setOnClicks() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardAlarm.setOnClickListener {
                getAlarmSound()
            }
        }
    }

    private fun getAlarmSound() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("audio/*")
        getAudioResult.launch(intent)
    }

    private val getAudioResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                viewModel.cacheAudioFromUri(uri, requireActivity())
            }
        }
    }
}