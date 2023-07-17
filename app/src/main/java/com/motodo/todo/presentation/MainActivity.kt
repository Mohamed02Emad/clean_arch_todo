package com.motodo.todo.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.R
import com.motodo.todo.data.receivers.alarmReceiver.DailyCheckReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStore: DataStoreImpl
    private val REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1

    private val pushPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted ) {
        }
    }
    private val pushPermissionLauncherAlarm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted ) {
        }
    }

    private var isOnBoardingFinished : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            isOnBoardingFinished = dataStore.getIsOnBoardingFinished()
        }
        setContentView(R.layout.activity_main)
        requestForPermission()
        setDailyAlarm()

    }

    private fun setDailyAlarm() {
        DailyCheckReceiver.setDailyCheck(this.applicationContext)
    }

    fun undoFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
           controller.show(WindowInsetsCompat.Type.systemBars())
        }

    }

    fun handleFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

     suspend fun isOnBoardingFinished(): Boolean {
        return isOnBoardingFinished ?: dataStore.getIsOnBoardingFinished()
    }

    private fun requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        pushPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

}