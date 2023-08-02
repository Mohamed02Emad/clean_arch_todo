package com.motodo.todo.presentation.fragmentOnBoarding

import androidx.datastore.dataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mo_chatting.chatapp.data.dataStore.DataStoreImpl
import com.motodo.todo.R
import com.motodo.todo.domain.models.OnBoarding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(val dataStoreImpl: DataStoreImpl): ViewModel() {

    internal val onBoardings : ArrayList<OnBoarding> = ArrayList()

    init {
        onBoardings.add(OnBoarding(R.drawable.ic_human_one, "Create a todo" , "and save it ",0))
        onBoardings.add(OnBoarding(R.drawable.ic_human_two, "Set Time for todo alarm" , "and you will get notified ",1))
        onBoardings.add(OnBoarding(R.drawable.ic_human_three, "Set Reminder " , "and we will remind you before the alarm",2))
    }

    suspend fun setIsOnBoardingFinished(isOnBoardingFinished: Boolean) = withContext(Dispatchers.IO){
        dataStoreImpl.setIsOnBoardingFinished(isOnBoardingFinished)
    }

}