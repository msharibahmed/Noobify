package com.example.myapplication.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.helpers.PlaylistAdapter
import com.example.myapplication.models.PlaylistItemModel
import com.example.myapplication.services.PlaylistItemService
import com.example.myapplication.services.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var layout: LinearLayoutManager
    private var playlistAdapter: PlaylistAdapter = PlaylistAdapter(this)
    var isScrolling: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeLayout()
    }


    private fun initializeLayout() {

        setTheme(R.style.Theme_MyApplication)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layout = LinearLayoutManager(this@MainActivity)
        setUpRecyclerView()
        loadPlaylistItems()
        setUpRecycleViewScrollListener()

    }

    private fun setUpRecyclerView() {

        binding.listSongsPlaylist.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            layoutManager = layout
            adapter = playlistAdapter
        }

    }

    private fun setUpRecycleViewScrollListener() {
        // using scroll listener to notify when list is finished and call api
        binding.listSongsPlaylist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                isScrolling = true
                Log.d("isScrolling $isScrolling", "during scrolling")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItems = playlistAdapter.itemCount //total items in the list
                val currentItems = layout.childCount  // current visible items on the screen
                val scrolledOutItems = layout.findFirstVisibleItemPosition() // scrolled out items

                if (isScrolling && (currentItems + scrolledOutItems >= totalItems)) {
                    isScrolling = false
                    Log.d("isScrolling $isScrolling", "after Scrolled")
                    loadPlaylistItems() //calling api again
                }

            }
        })

    }

    private fun loadPlaylistItems() {
        binding.progressBar.visibility = View.VISIBLE //using this progress bar during api call
        //initiate the service
        val destinationService = ServiceBuilder.buildService(PlaylistItemService::class.java)
        val requestCall = destinationService.getSongDetail()
        //make network call asynchronously
        requestCall.enqueue(object : Callback<PlaylistItemModel> {
            override fun onResponse(
                call: Call<PlaylistItemModel>,
                response: Response<PlaylistItemModel>
            ) {
                if (binding.progressBar.isVisible) {
                    binding.progressBar.visibility = View.GONE
                }
                // Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    val playlistItemList = response.body()!!
                    // Log.d("Response", "playlist size : ${playlistItemList.shorts.size}")

                    //adding data to the list using add() method in PlaylistAdapter class
                    playlistAdapter.add(playlistItemList)
                    showMiniPlayer()


                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Something went wrong ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PlaylistItemModel>, t: Throwable) {
                if (binding.progressBar.isVisible) { //hiding progressbar if failed to call api
                    binding.progressBar.visibility = View.GONE
                }

                Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
    private fun showMiniPlayer() {

            binding.miniPlayer.visibility = View.VISIBLE
    }

}


