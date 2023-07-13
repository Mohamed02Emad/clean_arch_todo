package com.mo_chatting.chatapp.data.dataStore

interface DataStore {
    suspend fun getIsOnBoardingFinished():Boolean

   suspend fun setIsOnBoardingFinished(isOnBoardingFinished:Boolean)

}
