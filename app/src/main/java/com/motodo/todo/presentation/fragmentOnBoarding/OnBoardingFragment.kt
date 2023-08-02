package com.motodo.todo.presentation.fragmentOnBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.motodo.todo.databinding.FragmentOnBoardingBinding
import com.motodo.todo.presentation.adapters.OnBoardingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {


    private lateinit var binding: FragmentOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()
    private lateinit var viewPager: ViewPager
    private lateinit var myAdapter: OnBoardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnBoardingAdapter()
        setOnClicks()
    }

    private fun setOnBoardingAdapter() {
        viewPager = binding.viewPager
        myAdapter = OnBoardingAdapter(viewModel.onBoardings)
        viewPager.adapter = myAdapter
        binding.dotsIndicator.attachTo(viewPager)
    }

    private fun setOnClicks() {
        binding.btnNext.setOnClickListener {
            btnNextClicked()
        }


    }

    private fun btnNextClicked() {
        val nextItem = viewPager.currentItem + 1
        if (nextItem < myAdapter.count) {
            viewPager.currentItem = nextItem
        } else {
            navigateToHome()
        }
    }

    private fun btnPreviousClicked() {
        val previousItem = viewPager.currentItem - 1
        if (previousItem >= 0) {
            viewPager.currentItem = previousItem
        }
    }

    private fun navigateToHome() {
        lifecycleScope.launch {
            viewModel.setIsOnBoardingFinished(true)
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
        }
    }

}