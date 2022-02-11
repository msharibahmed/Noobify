package com.example.myapplication.services

import com.example.myapplication.models.Song

class MusicDatabase {
    private val destinationService = ServiceBuilder.buildService(PlaylistItemService::class.java)
    suspend fun getAllSongs(): List<Song> {
        return try {

           val res =  destinationService.getSongDetail().shorts
            val songs = ArrayList<Song>()
            //Log.d("LOG gOT THE RESULTS ApI",res.toString())
            res.forEach { it->
                songs.add(Song(it.shortID,it.title,it.creator.userID,it.audioPath,"https://img.icons8.com/color/344/audio-wave--v1.png"))
            }

            songs.toList()
        } catch (e: Exception) {
            emptyList()
        }

    }
}