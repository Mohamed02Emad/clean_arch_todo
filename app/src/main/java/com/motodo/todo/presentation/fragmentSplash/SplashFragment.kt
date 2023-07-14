package com.motodo.todo.presentation.fragmentSplash

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.R
import com.motodo.todo.utils.Constants.SPLASH_DELAY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var dataStore: DataStoreImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiLevel = Build.VERSION.SDK_INT
        if (apiLevel > Build.VERSION_CODES.S) {
            handleDirections()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                handleDirections()
            }, SPLASH_DELAY)
        }
    }

    private fun handleDirections() {
        lifecycleScope.launch {
            if (isOnBoardingFinished()) {
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

    private suspend fun isOnBoardingFinished(): Boolean {
        return dataStore.getIsOnBoardingFinished()
    }
}