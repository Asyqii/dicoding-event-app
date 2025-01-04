package com.example.mydicodingevent.ui.detailevent

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydicodingevent.data.EventRepository
import com.example.mydicodingevent.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class DetailEventViewModel(application: Application) : ViewModel() {

    private val repository = EventRepository(application)

    fun insertFavorite(event: EventEntity) {
        viewModelScope.launch {
            repository.addFavorite(event)
            Log.d("DetailEventViewModel", "Event inserted: ${event.id}")
        }
    }

    fun deleteFavorite(event: EventEntity) {
        viewModelScope.launch {
            repository.removeFavorite(event)
            Log.d("DetailEventViewModel", "Event inserted: ${event.id}")
        }
    }

    fun isEventFavorite(eventId: Int): LiveData<Boolean> {
        return repository.isEventFavorite(eventId)
    }

}