package com.example.myapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.helpers.PlaylistAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var playlistAdapter: PlaylistAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLayout()

    }


    private fun initializeLayout() {
        setTheme(R.style.Theme_MyApplication)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val playlist  = ArrayList<String>()
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")
        playlist.add("Add songs")

        binding.listSongsPlaylist.setHasFixedSize(true)
        binding.listSongsPlaylist.setItemViewCacheSize(10)
        binding.listSongsPlaylist.layoutManager = LinearLayoutManager(this@MainActivity)
        playlistAdapter = PlaylistAdapter(context = this@MainActivity,playlist )
        binding.listSongsPlaylist.adapter = playlistAdapter
    }
}

