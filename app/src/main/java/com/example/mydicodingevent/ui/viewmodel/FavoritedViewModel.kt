package com.example.mydicodingevent.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mydicodingevent.data.EventRepository
import com.example.mydicodingevent.data.local.entity.EventEntity

class FavoritedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: EventRepository = EventRepository(application)
    fun getAllFavorites(): LiveData<List<EventEntity>> {
        return repository.getAllFavorites()
    }
}