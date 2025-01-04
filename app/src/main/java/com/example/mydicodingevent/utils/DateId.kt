package com.example.mydicodingevent.utils
import java.text.SimpleDateFormat
import java.util.Locale

fun String.toIdnDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale("id", "ID"))

    return try {
        val date = inputFormat.parse(this)
        if (date != null) {
            outputFormat.format(date)
        } else {
            this
        }
    } catch (e: Exception) {
        this
    }
}
