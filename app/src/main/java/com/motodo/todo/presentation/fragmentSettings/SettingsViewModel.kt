package com.motodo.todo.presentation.fragmentSettings

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.domain.useCases.TodoUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(val useCases: TodoUseCases ) : ViewModel() {
   @Inject
    lateinit var dataStore: DataStoreImpl

    private val _alarmName: MutableLiveData<String> = MutableLiveData("default")
    val alarmName: LiveData<String> = _alarmName

    fun myInit() {
        viewModelScope.launch {
            _alarmName.value = dataStore.getAlarmName()
        }
    }

    fun cacheAudioFromUri(uri: Uri, context: Context): File? {


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

            val audioName = getAudioFileNameFromUri(uri, context)
            audioName?.let { audioName ->
                viewModelScope.launch {
                    cacheAudioName(audioName)
                }
            }

            return outputFile
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            outputStream.close()
        }

        return null
    }


    private fun getAudioFileNameFromUri(uri: Uri, context: Context): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    val displayName = it.getString(displayNameIndex)
                    return displayName
                }
            }
        }
        return null
    }

    private suspend fun cacheAudioName(name: String) {
        dataStore.setAlarmName(name)
        _alarmName.value = name
    }


}