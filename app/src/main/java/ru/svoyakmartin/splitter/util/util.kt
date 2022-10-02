package ru.svoyakmartin.splitter.util

import java.text.SimpleDateFormat
import java.util.*


object util {
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    fun num2String(number: Double): String {
        return String.format("%.2f", number).replace(',', '.')
    }

    fun getFormattedDate(timeInMillis: Long): String{
        return dateFormat.format(timeInMillis)
    }
}