package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.helpers.Constants.TAG
import com.example.myapplication.helpers.SongAdapter
import com.example.myapplication.helpers.Status
import com.example.myapplication.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var songAdapter: SongAdapter

    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var layout: LinearLayoutManager


    private var isScrolling = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        layout = LinearLayoutManager(requireContext())
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        subscribeToObservers()
        setUpRecycleViewScrollListener()

        songAdapter.setOnItemClickListener {
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() = homeBinding.listSongsPlaylist.apply {
        setHasFixedSize(true)
        setItemViewCacheSize(10)
        adapter = songAdapter
        layoutManager = layout
    }

    private fun setUpRecycleViewScrollListener() {
        // using scroll listener to notify when list is finished and call api
        homeBinding.listSongsPlaylist.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
                Log.d("isScrolling $isScrolling", "during scrolling")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItems = songAdapter.itemCount//total items in the list
                val currentItems = layout.childCount  // current visible items on the screen
                val scrolledOutItems = layout.findFirstVisibleItemPosition() // scrolled out items

                if (isScrolling && (currentItems + scrolledOutItems >= totalItems - 1)) {
                    isScrolling = false
                    Log.d("isScrolling $isScrolling", "after Scrolled")
                    subscribeToObservers() //calling api again
                }

            }
        })

    }


    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            Log.d(TAG, "subscribeToObservers: result.status = ${result.status}")
            when (result.status) {
                Status.SUCCESS -> {
                    homeBinding.loadingProgressBar.isVisible = false
                    result.data?.let { songs ->
                            songAdapter.add(songs)
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> homeBinding.loadingProgressBar.isVisible = true
            }
        }
    }
}








