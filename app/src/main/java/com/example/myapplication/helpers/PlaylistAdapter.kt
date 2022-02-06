package com.example.myapplication.helpers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PlaylistItemBinding

class PlaylistAdapter(private val context: Context, private val songsList: ArrayList<String>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistItemViewHolder>() {

    inner class PlaylistItemViewHolder(binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name = binding.textCreatorName
        var username = binding.textCreatorUsername
        var contact = binding.btnCreatorContact
        var avatar = binding.imgCreatorAvatar
        var createdAt = binding.textCreatedAt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemViewHolder {
        return PlaylistItemViewHolder(
            PlaylistItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {
        holder.name.text = songsList[position]
    }

    override fun getItemCount(): Int {
        return songsList.size
    }


}