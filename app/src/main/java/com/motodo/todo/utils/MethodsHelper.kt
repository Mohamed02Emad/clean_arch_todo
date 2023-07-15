package com.motodo.todo.utils

import java.util.Calendar


fun isOldDate(day : Int , month : Int , year : Int): Boolean{
    val inputDate = Calendar.getInstance()
    inputDate.set(year, month , day)
    val todayDate = Calendar.getInstance()
    return inputDate.before(todayDate)
}