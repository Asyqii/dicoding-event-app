package com.example.mydicodingevent.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mydicodingevent.data.local.entity.EventEntity
import com.example.mydicodingevent.data.local.room.EventDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class EventRepository(application: Application) {

    private val eventDao = EventDatabase.getInstance(application.applicationContext).eventDao()

    fun getAllFavorites(): LiveData<List<EventEntity>> = eventDao.getBookmarkedEvent()

    suspend fun addFavorite(event: EventEntity) {
        withContext(Dispatchers.IO) {
            eventDao.insertEvent(event)
        }
    }

    suspend fun removeFavorite(event: EventEntity) {
        withContext(Dispatchers.IO) {
            eventDao.delete(event)
        }
    }

    fun isEventFavorite(eventId: Int): LiveData<Boolean> {
        return eventDao.isEventFavorited(eventId)
    }


}