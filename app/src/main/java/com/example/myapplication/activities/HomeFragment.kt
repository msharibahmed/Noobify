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
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.helpers.Constants.TAG
import com.example.myapplication.helpers.SongAdapter
import com.example.myapplication.helpers.Status
import com.example.myapplication.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), CustomItemClickListener {

    private lateinit var mainViewModel: MainViewModel


    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        playlistAdapter = PlaylistAdapter(requireContext(), this)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        setupRecyclerView()
        subscribeToObservers()


    }

    private fun setupRecyclerView() = homeBinding.listSongsPlaylist.apply {
        setHasFixedSize(true)
        setItemViewCacheSize(10)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = playlistAdapter
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            Log.d("LOG:", homeBinding.loadingProgressBar.isVisible.toString())

            when (result.status) {

                Status.SUCCESS -> {
                    homeBinding.loadingProgressBar.isVisible = false
                    Log.d("LOG:", homeBinding.loadingProgressBar.isVisible.toString())


                    result.data?.let { songs ->
                        playlistAdapter.add(songs)
                        Log.d("LOG:size", songs.toString())

                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> homeBinding.loadingProgressBar.isVisible = true
            }
        }
    }

    override fun onItemClickChangeMiniPlayer(
        playlistItemModel: PlaylistItemModel.Short,
        index: Int
    ) {
        android.util.Log.d("play", "go3")

        mainViewModel.playOrToggleSong(playlistItemModel)
        android.util.Log.d("play", "go4")

    }
}
*/

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var songAdapter: SongAdapter

    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        homeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        setupRecyclerView()
        subscribeToObservers()

        songAdapter.setOnItemClickListener {
            Log.d("kk","kk")
            mainViewModel.playOrToggleSong(it)
        }
    }

    private fun setupRecyclerView() = homeBinding.listSongsPlaylist.apply {
        adapter = songAdapter
        layoutManager = LinearLayoutManager(requireContext())
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








