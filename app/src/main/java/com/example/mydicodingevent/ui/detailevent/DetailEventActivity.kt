package com.example.mydicodingevent.ui.detailevent

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.mydicodingevent.R
import com.example.mydicodingevent.data.response.Event
import com.example.mydicodingevent.data.response.EventDetailResponse
import com.example.mydicodingevent.data.retrofit.ApiConfig
import com.example.mydicodingevent.databinding.ActivityDetailEventBinding
import com.example.mydicodingevent.utils.toIdnDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
import com.example.mydicodingevent.data.local.entity.EventEntity


class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    val isLoading = MutableLiveData<Boolean>()
    private var isFavorited: Boolean = false
    private lateinit var viewModel: DetailEventViewModel
    val eventDetail = MutableLiveData<Event?>()


    companion object {
        const val EXTRA_EVENT_ID = "data_activity_event"
        const val EXTRA_EVENT = "extra_event"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


            val factory = DetailViewModelFactory(application)
            viewModel = ViewModelProvider(this, factory)[DetailEventViewModel::class.java]

        isLoading.observe(this) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
                hideViews()
            } else {
                binding.progressBar.visibility = View.GONE
                showViews()
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)

        if (eventId != 0) {
            fetchEventDetail(eventId)
        } else {
            Toast.makeText(this, "Event ID tidak valid", Toast.LENGTH_SHORT).show()
        }

        eventDetail.observe(this) { event ->
            event?.id?.let { eventId ->
                viewModel.isEventFavorite(event.id).observe(this) { isFav ->
                    val favoriteIcon = if (isFav) {
                        R.drawable.ic_favorited
                    } else {
                        R.drawable.ic_favorite
                    }
                    binding.favIcon.setImageResource(favoriteIcon)
                }

                eventId.let {
                    viewModel.isEventFavorite(it).observe(this@DetailEventActivity) { isFav ->
                    binding.btnFav.setOnClickListener {
                        val eventFav = EventEntity(
                            event.id,
                            event.ownerName,
                            event.mediaCover,
                            event.description,
                            event.link,
                            event.beginTime
                        )
                            if (isFav) {
                                viewModel.deleteFavorite(eventFav)
                                binding.favIcon.setImageResource(R.drawable.ic_favorite)
                                Toast.makeText(
                                    this,
                                    "Event telah dihapus dari favorit",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.insertFavorite(eventFav)
                                binding.favIcon.setImageResource(R.drawable.ic_favorited)
                                Toast.makeText(
                                    this,
                                    "Event ditambahkan ke daftar favorit",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun fetchEventDetail(eventId: Int) {
        isLoading.value = true
        val apiService = ApiConfig.getApiService()
        val call = apiService.getDetailEvent(eventId)
        call.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                isLoading.value = false
                if (response.isSuccessful) {
                   eventDetail.value = response.body()?.event
                    eventDetail.value?.let { event ->
                        displayEventDetail(event)
                    }
                } else {
                    Toast.makeText(this@DetailEventActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                isLoading.value = false
                Toast.makeText(this@DetailEventActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("StringFormatMatches")
    private fun displayEventDetail(event: Event) {
        binding.tvName.text = event.name
        binding.tvOwnerName.text = getString(R.string.diselenggarakan_oleh, event.ownerName)
        binding.tvRegistrans.text = getString(R.string.sisa_kouta, (event.quota - event.registrants))
        binding.tvBeginTime.text = getString(R.string.mulai, event.beginTime.toIdnDate())
        binding.tvEndTime.text = getString(R.string.selesai, event.endTime.toIdnDate())
        binding.tvDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)

        Glide.with(this).load(event.mediaCover).into(binding.mediaCover)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val endDate: Date? = sdf.parse(event.endTime)
        val currentDate = Date()

        if (endDate != null && currentDate.after(endDate)) {
            binding.buttonRegister.text = getString(R.string.title_pendaftaran_ditutup)
            binding.buttonRegister.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }
        } else {
            binding.buttonRegister.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.link)
                startActivity(intent)
            }
        }
    }

    private fun hideViews() {
        binding.btnFav.visibility = View.GONE
        binding.backBtn.visibility = View.GONE
        binding.tvName.visibility = View.GONE
        binding.tvOwnerName.visibility = View.GONE
        binding.tvRegistrans.visibility = View.GONE
        binding.tvBeginTime.visibility = View.GONE
        binding.tvEndTime.visibility = View.GONE
        binding.tvDescription.visibility = View.GONE
        binding.mediaCover.visibility = View.GONE
        binding.buttonRegister.visibility = View.GONE
        binding.exDescription.visibility = View.GONE
        binding.exCalendar.visibility = View.GONE
    }

    private fun showViews() {
        binding.btnFav.visibility = View.VISIBLE
        binding.backBtn.visibility = View.VISIBLE
        binding.tvName.visibility = View.VISIBLE
        binding.tvOwnerName.visibility = View.VISIBLE
        binding.tvRegistrans.visibility = View.VISIBLE
        binding.tvBeginTime.visibility = View.VISIBLE
        binding.tvEndTime.visibility = View.VISIBLE
        binding.tvDescription.visibility = View.VISIBLE
        binding.mediaCover.visibility = View.VISIBLE
        binding.buttonRegister.visibility = View.VISIBLE
        binding.exDescription.visibility = View.VISIBLE
        binding.exCalendar.visibility = View.VISIBLE
    }

}