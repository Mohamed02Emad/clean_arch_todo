package com.motodo.todo.data.receivers.muteAlarmReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.motodo.todo.data.receivers.todosAlarm.TodosAlarmReceiver

class MuteAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val mediaPlayer = TodosAlarmReceiver.mediaPlayer
        mediaPlayer?.let {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
        }
    }

}