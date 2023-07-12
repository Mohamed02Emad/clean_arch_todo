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

    private val _currentPage: MutableLiveData<OnBoarding> = MutableLiveData()
    val currentPage: LiveData<OnBoarding> = _currentPage
    private val onBoardings : ArrayList<OnBoarding> = ArrayList()

    init {
        onBoardings.add(OnBoarding(R.drawable.ic_human_one, "Create Tasks" , "what do you need to do",0))
        onBoardings.add(OnBoarding(R.drawable.ic_human_two, "Pin the task" , "save the task to next day",1))
        onBoardings.add(OnBoarding(R.drawable.ic_human_three, "Mark completed tasks" , "we know, you like it",2))
        _currentPage.value= onBoardings[0]
    }

    suspend fun isOnBoardingFinished() : Boolean= withContext(Dispatchers.IO){
        dataStoreImpl.getIsOnBoardingFinished()
    }

    suspend fun setIsOnBoardingFinished(isOnBoardingFinished: Boolean) = withContext(Dispatchers.IO){
        dataStoreImpl.setIsOnBoardingFinished(isOnBoardingFinished)
    }
    fun isFirstPage(currentPage: Int): Boolean = currentPage == 0
    fun isLastPage(currentPage: Int): Boolean = currentPage == 2

    fun getPreviousPage(): OnBoarding? {
        val currentPage = _currentPage.value!!.pageNumber
        return if (isFirstPage(currentPage)) {
            null
        } else {
            onBoardings[currentPage-1]
        }
    }

    fun getNextPage(): OnBoarding? {
        val currentPage = _currentPage.value!!.pageNumber
        return if (isLastPage(currentPage)) {
            null
        } else {
            onBoardings[currentPage+1]
        }
    }

    fun setCurrentPage(currentPage: OnBoarding){
        _currentPage.value = currentPage
    }
}