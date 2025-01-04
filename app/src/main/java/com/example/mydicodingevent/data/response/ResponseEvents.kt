package com.example.mydicodingevent.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


 data class ResponseEvents(

	 @field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem> = listOf(),

	 @field:SerializedName("error")
	val error: Boolean,

	 @field:SerializedName("message")
	val message: String
)

@Parcelize
 data class ListEventsItem(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("mediaCover")
	val mediaCover: String,

	@field:SerializedName("registrants")
	val registrants: Int,

	@field:SerializedName("imageLogo")
	val imageLogo: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ownerName")
	val ownerName: String,

	@field:SerializedName("cityName")
	val cityName: String,

	@field:SerializedName("quota")
	val quota: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("beginTime")
	val beginTime: String,

	@field:SerializedName("endTime")
	val endTime: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("is_favorite")
	var isFavorite: Boolean,

	@field:SerializedName("creation_at")
	val creationAt: String? = null,

): Parcelable
