package com.example.myapplication.services

import android.util.Log
import com.example.myapplication.models.PlaylistItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicDatabase {

    fun getAllSongs(): List<PlaylistItemModel.Short> {
        var playlistItemList: List<PlaylistItemModel.Short> = emptyList()
        //initiate the service
        val destinationService = ServiceBuilder.buildService(PlaylistItemService::class.java)
        val requestCall = destinationService.getSongDetail()

        //make network call asynchronously
        requestCall.enqueue(object : Callback<PlaylistItemModel> {
            override fun onResponse(
                call: Call<PlaylistItemModel>,
                response: Response<PlaylistItemModel>
            ) {

                Log.d("Response", "onResponse: ${response.body()}")
                playlistItemList = if (response.isSuccessful) {
                    response.body()!!.shorts
                } else {
                    emptyList()
                }
            }

            override fun onFailure(call: Call<PlaylistItemModel>, t: Throwable) {
                playlistItemList = emptyList()
            }
        })
        return playlistItemList
    }

}