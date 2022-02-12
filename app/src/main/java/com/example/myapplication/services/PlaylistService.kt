package com.example.myapplication.services

import com.example.myapplication.models.PlaylistItemModel
import retrofit2.http.GET

interface PlaylistItemService {
    @GET("shorts")
    suspend fun getSongDetail(): PlaylistItemModel
}