package com.motodo.todo.presentation.fragmentSplash

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.R
import com.motodo.todo.presentation.MainActivity
import com.motodo.todo.utils.Constants.SPLASH_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment() {

    var systemVersion : Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return if (systemVersion!! >  31) {
            null
        }else {
            inflater.inflate(R.layout.fragment_splash, container, false)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        systemVersion = Build.VERSION.SDK_INT
        if (systemVersion!! > 31) {
            handleDirections()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity). handleFullScreen()
            Handler(Looper.getMainLooper()).postDelayed({
                handleDirections()
            }, SPLASH_DELAY)
    }

    private fun handleDirections() {
        lifecycleScope.launch {
            if ((activity as MainActivity).isOnBoardingFinished()) {
                openHomeFragment()
            } else {
                openOnBoardingFragment()
            }
        }
    }

    private fun openOnBoardingFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToOnBoardingFragment())
    }

    private fun openHomeFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).undoFullScreen()
    }
}