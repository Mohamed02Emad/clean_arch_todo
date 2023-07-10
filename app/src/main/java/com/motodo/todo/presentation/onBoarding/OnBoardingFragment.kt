package com.motodo.todo.presentation.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.motodo.todo.R
import com.motodo.todo.databinding.FragmentOnBoardingBinding
import com.motodo.todo.domain.models.OnBoarding

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
        setObservers()
        setOnClicks()
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
            Toast.makeText(requireContext(), "Get started", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCurrentCircles(pageNumber: Int) {
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

    private fun showButtons(onBoarding: OnBoarding) {
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
        showButtons(onBoarding)
        setCurrentCircles(onBoarding.pageNumber)
    }
}