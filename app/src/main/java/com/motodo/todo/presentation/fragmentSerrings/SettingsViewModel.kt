package com.motodo.todo.presentation.fragmentSerrings

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.motodo.todo.domain.useCases.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(val useCases: TodoUseCases) : ViewModel(){


    fun cacheAudioFromUri(uri: Uri, context : Context): File? {

        val audioLocation = context.filesDir.absolutePath + File.separator
        val cacheDir = File(audioLocation)
        if (!cacheDir.exists()) cacheDir.mkdir()

        val inputStream: InputStream = context.contentResolver.openInputStream(uri) ?: return null

        val outputFile = File(cacheDir, "alarm_audio.mp3")
        val outputStream = FileOutputStream(outputFile)

        try {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
            return outputFile
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            outputStream.close()
        }

        return null
    }




}