package com.example.myapplication.models

data class PlaylistItemModel(
    val shorts: List<Short>
) {
    data class Short(
        val audioPath: String,
        val creator: Creator,
        val dateCreated: String,
        val shortID: String,
        val title: String
    ) {
        data class Creator(
            val email: String,
            val userID: String
        )
    }
}