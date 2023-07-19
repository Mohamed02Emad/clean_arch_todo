package com.mo_chatting.chatapp.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


private val Context.dataStore by preferencesDataStore("user_data")

class DataStoreImpl(
    appContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataStore {

    private val mDataStore by lazy {
        appContext.dataStore
    }

    companion object {
        const val ON_BOARDING = "onBoarding"
        const val ALARM_NAME = "alarmName"
    }

    override suspend fun getIsOnBoardingFinished(): Boolean = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[booleanPreferencesKey(ON_BOARDING)] ?: false
        }.first()
    }

    override suspend fun setIsOnBoardingFinished(isOnBoardingFinished: Boolean) {
        withContext(dispatcher) {
            mDataStore.edit { settings ->
                settings[booleanPreferencesKey(ON_BOARDING)] = isOnBoardingFinished
            }
        }
    }

    override suspend fun getAlarmName(): String = withContext(dispatcher) {
        mDataStore.data.map { settings ->
            settings[stringPreferencesKey(ALARM_NAME)] ?: "default"
        }.first()
    }
    override suspend fun setAlarmName(alarmName: String) {
        withContext(dispatcher) {
            mDataStore.edit { settings ->
                settings[stringPreferencesKey(ALARM_NAME)] = alarmName
            }
        }
    }

}