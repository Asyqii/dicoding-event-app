package com.example.mydicodingevent.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevent.data.response.ListEventsItem
import com.example.mydicodingevent.data.response.ResponseEvents
import com.example.mydicodingevent.data.retrofit.ApiConfig
import com.example.mydicodingevent.databinding.FragmentFinishedBinding
import com.example.mydicodingevent.ui.adapter.ReviewAdapter
import com.example.mydicodingevent.ui.detailevent.DetailEventActivity
import com.example.mydicodingevent.ui.viewmodel.FinishedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedFragment : Fragment() {

    companion object {
        const val TAG = "FinishedFragment"
    }

    private val finishedViewModel by viewModels<FinishedViewModel>()

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private lateinit var reviewAdapter: ReviewAdapter

    private fun onEventClick(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFinishedData()

        reviewAdapter = ReviewAdapter{event -> onEventClick(event)}

        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        binding.btnClick.setOnClickListener { finishedViewModel.retryFetchingEvents() }

        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
            if (!events.isNullOrEmpty()) {
                reviewAdapter.submitList(events)
                binding.rvReview.visibility = View.VISIBLE
                binding.errUpcoming.visibility = View.GONE
                binding.btnClick.visibility = View.GONE
            } else {
                binding.rvReview.visibility = View.GONE
            }
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.errUpcoming.visibility = View.GONE
            }
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) { err ->
            if (!finishedViewModel.isLoading.value!! && !err.isNullOrBlank()) {
                binding.rvReview.visibility = View.VISIBLE
                binding.loading.visibility = View.VISIBLE
                binding.errUpcoming.visibility = View.VISIBLE
            } else {
                binding.errUpcoming.visibility = View.GONE
                binding.btnClick.visibility = View.GONE
            }
        }


        finishedViewModel.fetchEventsFromApi()
    }

    private fun setFinishedData() {
        if (finishedViewModel.events.value != null) return

        finishedViewModel._isLoading.value = true
        finishedViewModel.fetchEventsFromApi()
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<ResponseEvents> {
            override fun onResponse(
                call: Call<ResponseEvents>,
                response: Response<ResponseEvents>
            ) {
                if (response.isSuccessful) {
                    finishedViewModel._isLoading.value = false
                    val data = response.body()?.listEvents
                    finishedViewModel._events.value = data
                } else {
                    finishedViewModel._isLoading.value = false
                    finishedViewModel._errorMessage.value = "Gagal mengambil data"
                    Log.e(FinishedFragment.TAG, "Response Tidak berhasil ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseEvents>, t: Throwable) {
                finishedViewModel._isLoading.value = false
                finishedViewModel._errorMessage.value = "Gagal mengambil data"
                Log.e(FinishedFragment.TAG, "Gagal mengambil data ${t.message}")
            }

        })
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}