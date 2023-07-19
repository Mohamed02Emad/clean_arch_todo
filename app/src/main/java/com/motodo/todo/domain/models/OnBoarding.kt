package com.motodo.todo.domain.models

import androidx.annotation.Keep

@Keep
data class OnBoarding(
    val image : Int,
    val title : String,
    val description : String,
    val pageNumber : Int
)
