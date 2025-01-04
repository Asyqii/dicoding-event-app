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
import com.example.mydicodingevent.databinding.FragmentUpcomingBinding
import com.example.mydicodingevent.data.retrofit.ApiConfig
import com.example.mydicodingevent.ui.adapter.ReviewAdapter
import com.example.mydicodingevent.ui.detailevent.DetailEventActivity
import com.example.mydicodingevent.ui.viewmodel.UpcomingViewModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class UpcomingFragment : Fragment() {

    companion object {
        const val TAG = "UpcomingFragment"
    }

    private val  viewModel by viewModels<UpcomingViewModel>()

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var reviewAdapter: ReviewAdapter

    private fun onEventClick(event: ListEventsItem) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpcomingData()
        
        reviewAdapter = ReviewAdapter { event -> onEventClick(event) }

        binding.rvReview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            setHasFixedSize(true)
        }

        binding.btnClick.setOnClickListener { viewModel.retryFetchingEvents() }

    viewModel.events.observe(viewLifecycleOwner) { events ->
        if (!events.isNullOrEmpty()) {
            reviewAdapter.submitList(events)
            binding.rvReview.visibility = View.VISIBLE
            binding.btnClick.visibility = View.GONE
            binding.errUpcoming.visibility = View.GONE
        } else {
            binding.rvReview.visibility = View.GONE
        }
    }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.errUpcoming.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { err ->
            if (!viewModel.isLoading.value!! && !err.isNullOrBlank()) {
                binding.rvReview.visibility = View.VISIBLE
                binding.loading.visibility = View.GONE
                binding.errUpcoming.visibility = View.VISIBLE
            } else {
                binding.errUpcoming.visibility = View.GONE
                binding.btnClick.visibility = View.GONE
            }
        }

        viewModel.fetchEventsFromApi()
    }

private fun setUpcomingData() {
    if (viewModel.events.value != null) return

    viewModel._isLoading.value = true
    viewModel.fetchEventsFromApi()
    val client = ApiConfig.getApiService().getUpcomingEvents()
    client.enqueue(object : Callback<ResponseEvents> {
        override fun onResponse(
            call: Call<ResponseEvents>,
            response: Response<ResponseEvents>
        ) {
            if (response.isSuccessful) {
                viewModel._isLoading.value = false
                val data = response.body()?.listEvents
                    viewModel._events.value = data
            } else {
                viewModel._isLoading.value = false
                viewModel._errorMessage.value = "Gagal mengambil data"
                Log.e(TAG, "Response Tidak berhasil ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ResponseEvents>, t: Throwable) {
            viewModel._isLoading.value = false
            viewModel._errorMessage.value = "Gagal mengambil data"
            Log.e(TAG, "Gagal mengambil data ${t.message}")
        }

    })
}


}