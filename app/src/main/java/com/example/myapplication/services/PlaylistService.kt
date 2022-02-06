package com.example.myapplication.services

import com.example.myapplication.models.PlaylistItemModel
import retrofit2.Call
import retrofit2.http.GET

interface PlaylistItemService {
    @GET("shorts")
    fun getSongDetail(): Call<PlaylistItemModel>
}