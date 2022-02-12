package com.example.myapplication.activities

import android.os.Bundle
import android.os.Handler
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.helpers.Constants.TAG
import com.example.myapplication.helpers.CustomOnclickListener
import com.example.myapplication.helpers.SongAdapter
import com.example.myapplication.helpers.Status
import com.example.myapplication.helpers.exoplayer.isPlaying
import com.example.myapplication.models.Song
import com.example.myapplication.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), CustomOnclickListener {

    lateinit var mainViewModel: MainViewModel


    var songAdapter: SongAdapter = SongAdapter(this)
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var layout: LinearLayoutManager

    private var isScrolling = false

    private var curPlayingSong: Song? = null
    private var curPlayingSongIndex: Int? = null
    private var playbackState: PlaybackStateCompat? = null
    private var shouldUpdateSeekbar = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        observeSubscribedData()
        setMiniPLayerViewListeners()

    }

    private fun setMiniPLayerViewListeners() {
        miniPlayerToggleListener()
        nextSongBtnListener()
        prevSongBtnListener()
        forwardSongBtnListener()
        rewindSongBtnListener()
        seekbarBtnListener()

    }

    private fun miniPlayerToggleListener() {
        homeBinding.miniPlayerPlayBtn.setOnClickListener {
            curPlayingSong?.let {
                mainViewModel.playOrToggleSong(it, true)
            }
        }

    }

    private fun nextSongBtnListener() {
        homeBinding.miniPlayerNextBtn.setOnClickListener {
            mainViewModel.skipToNextSong()
        }
    }

    private fun prevSongBtnListener() {
        homeBinding.miniPlayerPrevBtn.setOnClickListener {
            mainViewModel.skipToPreviousSong()
        }
    }

    private fun forwardSongBtnListener() {
        homeBinding.miniPlayerForwardBtn.setOnClickListener {
            mainViewModel.forwardSong()
        }
    }

    private fun rewindSongBtnListener() {
        homeBinding.miniPlayerRewindBtn.setOnClickListener {
            mainViewModel.rewindSong()
        }
    }

    private fun seekbarBtnListener() {
        homeBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })
    }

    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        homeBinding.tvCurTime.text = dateFormat.format(ms - 1800000L)
    }

    private fun observeSubscribedData() {
        mainViewModel.curPlayingSong.observe(viewLifecycleOwner) {
            curPlayingSong = it?.ToSong()
            curPlayingSong?.let { it1 ->
                setMiniPLayerContent(it1, songAdapter.songs.indexOf(it1))
                // Log.d("ggggggggg",songAdapter.songs.indexOf(it1).toString())
            }
        }
        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            homeBinding.miniPlayerPlayBtn.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.pause_btn_icon else R.drawable.play_btn_icon
            )
            homeBinding.seekBar.progress = it?.position?.toInt() ?: 0
        }

        mainViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                homeBinding.seekBar.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        mainViewModel.curSongDuration.observe(viewLifecycleOwner) {
            if (it != 9223372036852975809) {
                homeBinding.seekBar.max = it.toInt()
            }
            //  Log.d("currentSongLength + 30 Min->","$it")
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            homeBinding.tvSongDuration.text = dateFormat.format(it - 1800000L)
        }

        mainViewModel.isConnected.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.ERROR -> Snackbar.make(
                        homeBinding.root,
                        it.message ?: "An error occurred",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }
        mainViewModel.networkError.observe(viewLifecycleOwner) { event ->
            event?.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.ERROR -> Snackbar.make(
                        homeBinding.root,
                        it.message ?: "An error occurred",
                        Snackbar.LENGTH_LONG
                    ).show()
                    else -> Unit
                }
            }
        }
    }

    private fun setupRecyclerView() = homeBinding.listSongsPlaylist.apply {
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
                // Log.d("isScrolling $isScrolling", "during scrolling")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItems = songAdapter.itemCount//total items in the list
                val currentItems = layout.childCount  // current visible items on the screen
                val scrolledOutItems = layout.findFirstVisibleItemPosition() // scrolled out items

                if (isScrolling && (currentItems + scrolledOutItems >= totalItems)) {
                    isScrolling = false
                    val handler = Handler()
                    handler.post {
                        homeBinding.loadingProgressBar.isVisible = true
                    }
                    //   Log.d("isScrolling $isScrolling", "after Scrolled")
                    // Log.d("isVisivle ", homeBinding.loadingProgressBar.isVisible.toString())
                    subscribeToObservers() //calling api again
                    handler.postDelayed({
                        homeBinding.loadingProgressBar.isVisible = false
                        //  Log.d("Hnadler", homeBinding.loadingProgressBar.isVisible.toString())
                    }, 600L)
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
                        if (curPlayingSong == null && songs.isNotEmpty()) {
                            homeBinding.apply {
                                miniPlayerCard.isVisible = true
                                setMiniPLayerContent(songs[0], 0)
                            }
                            if (curPlayingSongIndex == 0) {
                                homeBinding.miniPlayerPrevBtn.isEnabled = false
                            }

                        }
                        homeBinding.listSongsPlaylist.post {
                            songAdapter.add(songs)
                        }
                    }
                }
                Status.ERROR -> Unit
                Status.LOADING -> homeBinding.loadingProgressBar.isVisible = true
            }
        }
    }

    override fun onItemClickChangeMiniPlayer(song: Song, index: Int) {
        setMiniPLayerContent(song, index)
        mainViewModel.playOrToggleSong(song)

    }

    fun MediaMetadataCompat.ToSong(): Song? {
        return description?.let {
            Song(
                it.mediaId ?: "",
                it.title.toString(),
                it.subtitle.toString(),
                it.mediaUri.toString(),
                it.iconUri.toString()
            )
        }
    }

    private fun setMiniPLayerContent(song: Song, index: Int) {
        homeBinding.apply {
            miniPlayerSongName.text = song.title
            miniPlayerCreatorName.text = "Anonymous" + song.subtitle

            if (index == 0) {
                miniPlayerPrevBtn.isEnabled = false
                miniPlayerPrevBtn.setColorFilter(resources.getColor(R.color.white))
            } else {

                miniPlayerPrevBtn.isEnabled = true
                miniPlayerPrevBtn.setColorFilter(resources.getColor(R.color.black))
            }
        }
        curPlayingSong = song
        curPlayingSongIndex = index
    }
}








