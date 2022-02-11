package com.example.myapplication.helpers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PlaylistItemBinding
import com.example.myapplication.models.Song

open class SongAdapter(private val listener: CustomOnclickListener) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var name = binding.textCreatorName
        var username = binding.textCreatorUsername
        var createdAt = binding.textCreatedAt
        var songName = binding.textSongName

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickChangeMiniPlayer(songs[adapterPosition], adapterPosition)
        }

    }

    val songs = ArrayList<Song>()

    @SuppressLint("NotifyDataSetChanged")
    fun add(items: List<Song>) {
        songs.addAll(items)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]

        holder.apply {
            name.text = "Anonymous" + song.subtitle
            username.text = "@anonymous" + song.subtitle
            //createdAt.text = dateFormatter(song.dateCreated)
            songName.text = song.title
        }

    }

    override fun getItemCount(): Int {
        return songs.size
    }
}

interface CustomOnclickListener {
    fun onItemClickChangeMiniPlayer(song: Song, index: Int)
}