package com.example.mydicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mydicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM event ORDER BY beginTime DESC")
    fun getEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(news: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)


    @Query("SELECT EXISTS(SELECT 1 FROM event WHERE id = :eventId)")
    fun isEventFavorited(eventId: Int): LiveData<Boolean>
}