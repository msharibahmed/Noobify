package com.example.myapplication.models

data class PlaylistItemModel(
    val shorts: List<Short>
) {
    data class Short(
        val shortID: String,
        val title: String,
        val creator: Creator,
        val audioPath: String,
        val dateCreated: String = "2022-Nov-12",

    ) {
        data class Creator(
            val email: String,
            val userID: String
        )
    }
}