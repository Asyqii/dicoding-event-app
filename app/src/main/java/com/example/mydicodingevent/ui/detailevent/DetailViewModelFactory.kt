package com.example.mydicodingevent.ui.detailevent

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class DetailViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailEventViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}