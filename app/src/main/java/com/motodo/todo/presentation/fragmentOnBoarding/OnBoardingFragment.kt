package com.motodo.todo.presentation.fragmentOnBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.motodo.todo.R
import com.motodo.todo.databinding.FragmentOnBoardingBinding
import com.motodo.todo.domain.models.OnBoarding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {


    private lateinit var binding: FragmentOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {

            if(viewModel.isOnBoardingFinished()){
                findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
            }else{
                setObservers()
                setOnClicks()
            }

        }
    }


    private fun setObservers() {
        viewModel.currentPage.observe(viewLifecycleOwner){
            setOnBoarding(it)
        }
    }

    private fun setOnClicks() {
        binding.btnNext.setOnClickListener {
            val nextPage = viewModel.getNextPage()
            nextPage?.let {next->
                viewModel.setCurrentPage(next)
            }
        }

        binding.btnPrevious.setOnClickListener {
            val previousPage = viewModel.getPreviousPage()
            previousPage?.let {previous ->
                viewModel.setCurrentPage(previous)
            }
        }

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.setIsOnBoardingFinished(true)
            }
        }
    }

    private fun setCurrentCircle(pageNumber: Int) {
        when (pageNumber){
            0 ->{
                binding.apply {
                    ivFirstCircle.setImageResource(R.drawable.ic_fill_circle)
                    ivSecondCircle.setImageResource(R.drawable.ic_holo_circle)
                    ivThirdCircle.setImageResource(R.drawable.ic_holo_circle)
                }
            }
            1 ->{
                binding.apply {
                    ivFirstCircle.setImageResource(R.drawable.ic_holo_circle)
                    ivSecondCircle.setImageResource(R.drawable.ic_fill_circle)
                    ivThirdCircle.setImageResource(R.drawable.ic_holo_circle)
                }
            }
            2 ->{
                binding.apply {
                    ivFirstCircle.setImageResource(R.drawable.ic_holo_circle)
                    ivSecondCircle.setImageResource(R.drawable.ic_holo_circle)
                    ivThirdCircle.setImageResource(R.drawable.ic_fill_circle)
                }
            }
        }
    }

    private fun setButtonsVisibility(onBoarding: OnBoarding) {
        when (onBoarding.pageNumber){
            0 ->{
                binding.apply {
                    btnPrevious.isGone = true
                    btnNext.isGone = false
                    btnGetStarted.isGone = true
                }
            }
            1 ->{
                binding.apply {
                    btnPrevious.isGone = false
                    btnNext.isGone = false
                    btnGetStarted.isGone = true
                }
            }
            2 ->{
                binding.apply {
                    btnPrevious.isGone = true
                    btnNext.isGone = true
                    btnGetStarted.isGone = false
                }
            }
        }
    }

    private fun setOnBoarding(onBoarding: OnBoarding) {
        binding.ivOnboarding.setImageResource(onBoarding.image)
        binding.tvUpperText.text = onBoarding.title
        binding.tvLowerText.text = onBoarding.description
        setButtonsVisibility(onBoarding)
        setCurrentCircle(onBoarding.pageNumber)
    }
}