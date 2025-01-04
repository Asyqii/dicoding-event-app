package com.example.mydicodingevent.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydicodingevent.databinding.FragmentFavoritedBinding
import com.example.mydicodingevent.ui.viewmodel.FavoritedViewModel
import com.example.mydicodingevent.ui.adapter.FavoriteAdapter


class FavoritedFragment : Fragment() {


    private var _binding: FragmentFavoritedBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: FavoriteAdapter
    private lateinit var favViewModel: FavoritedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventAdapter = FavoriteAdapter(listOf())




    }

    override fun onResume() {
        super.onResume()

        favViewModel = ViewModelProvider(this)[FavoritedViewModel::class.java]
        binding.rvReview.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReview.adapter = eventAdapter

        favViewModel.getAllFavorites().observe(viewLifecycleOwner) { fav ->
            if (fav.isEmpty()) {
                binding.emptyFav.visibility = View.VISIBLE
                binding.rvReview.visibility = View.GONE
            } else {
                binding.emptyFav.visibility = View.GONE
                binding.rvReview.visibility = View.VISIBLE
                eventAdapter.setData(fav)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "favorited_fragment"

    }
}