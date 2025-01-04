package com.example.mydicodingevent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class EventEntity (
    @PrimaryKey
    val id: Int,

    val title: String,

    val imageUrl: String? = null,

    val description: String? = null,

    var beginTime: String,

    var endTime: String,

    val creationAt: String? = null,
)