package com.motodo.todo.data.receivers.todosAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import com.motodo.todo.domain.models.ToDo
import com.motodo.todo.utils.createExactAlarm
import com.motodo.todo.utils.getCalendarForTodoAlarm
import com.motodo.todo.utils.getUriOfCachedAudio
import com.motodo.todo.utils.setReminderCalendar
import com.motodo.todo.utils.showNotification

class TodosAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val todo: ToDo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getSerializableExtra("todo", ToDo::class.java)
        } else {
            intent?.getSerializableExtra("todo") as ToDo?
        }
        val useAlarm = intent?.getBooleanExtra("use_alarm", true)
        todo?.let {
            if (todo.isChecked) return

            val isPlayAlarm = todo.hasAlarm && useAlarm != false
            if (isPlayAlarm) {
                setMediaPlayers(context!!)
            }
            showNotification(context!!, todo.title, todo.id, isPlayAlarm)
        }
    }

    private fun setMediaPlayers(context: Context) {

        mediaPlayer = MediaPlayer()

        mediaPlayer!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        val audioUri = getUriOfCachedAudio(context)

        audioUri?.let {
            mediaPlayer = MediaPlayer.create(context, audioUri)
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                mediaPlayer!!.release()
            }
        }

    }

    companion object {

         var mediaPlayer: MediaPlayer? = null

        fun setTodoAlarm(context: Context, todo: ToDo) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TodosAlarmReceiver::class.java)
            try {
                intent.putExtra("todo", todo)
            } catch (_: Exception) {
                return
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, todo.id + 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val calendar = getCalendarForTodoAlarm(todo)
            createExactAlarm(calendar, alarmManager, pendingIntent)

            val reminderCalendar = setReminderCalendar(todo.remindBefore, calendar)

            reminderCalendar?.let { reminderCalendar ->
                intent.putExtra("use_alarm", false)
                val reminderPendingIntent = PendingIntent.getBroadcast(
                    context, -(todo.id + 1), intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                createExactAlarm(reminderCalendar, alarmManager, reminderPendingIntent)
            }
        }

    }

}