package com.motodo.todo.data.workManagers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.motodo.todo.utils.Constants.TAG

class NewDayWorkManager(
    val context: Context,
    val workerParams: WorkerParameters
) : Worker(context, workerParams) {


    override fun doWork(): Result {
        showToast("Scheduled task running at 00:00 AM!")
        return Result.success()
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "showToast: Worker")
    }

}
