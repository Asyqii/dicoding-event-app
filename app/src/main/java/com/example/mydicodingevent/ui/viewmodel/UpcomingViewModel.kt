package com.example.mydicodingevent.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.data.response.ResponseEvents
import com.example.mydicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpcomingViewModel : ViewModel() {

    val _events =  MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchEventsFromApi() {
        _isLoading.value = true
        val call = ApiConfig.getApiService().getUpcomingEvents()
        call.enqueue(object : Callback<ResponseEvents> {
            override fun onResponse(
                call: Call<ResponseEvents>,
                response: Response<ResponseEvents>
            ) {
                Log.d("On response", "Memanggil Fun OnResponse ")
                if (response.isSuccessful) {
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                    _errorMessage.value = "Gagal mengambil data"
                    Log.e("Data", "Response ${response.message()}") // Logging jika response tidak berhasil
                }

            }

            override fun onFailure(call: Call<ResponseEvents>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Gagal mengambil data"
                Log.e("onFailureViewModel", "Gagal mengabil data event : ${t.message}")

            }
        })

    }
    fun retryFetchingEvents() {
        _errorMessage.value = null
        fetchEventsFromApi()
    }

}