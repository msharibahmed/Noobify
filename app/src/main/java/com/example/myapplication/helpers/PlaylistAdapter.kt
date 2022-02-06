package com.example.myapplication.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PlaylistItemBinding
import com.example.myapplication.models.PlaylistItemModel

class PlaylistAdapter(private val context: Context) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistItemViewHolder>() {
    private var myList = ArrayList<PlaylistItemModel.Short>()
    inner class PlaylistItemViewHolder(binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var name = binding.textCreatorName
        var username = binding.textCreatorUsername
        var contact = binding.btnCreatorContact
        var avatar = binding.imgCreatorAvatar
        var createdAt = binding.textCreatedAt
        var songName = binding.textSongName

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
        holder.name.text = myList[position].title
    }

    override fun getItemCount(): Int {
        return myList.size
    }

     @SuppressLint("NotifyDataSetChanged")
     fun add(loadedData : PlaylistItemModel){
        myList.addAll(loadedData.shorts)
         notifyDataSetChanged()
    }

}