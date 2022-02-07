package com.example.myapplication.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PlaylistItemBinding
import com.example.myapplication.models.PlaylistItemModel
import java.text.SimpleDateFormat
import java.util.*

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
        holder.name.text = "Anonymous"+ myList[position].creator.userID
        holder.username.text = "@anonymous"+ myList[position].creator.userID
        holder.createdAt.text = dateFormatter(myList[position].dateCreated)
        holder.songName.text = myList[position].title
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(loadedData: PlaylistItemModel) {
        myList.addAll(loadedData.shorts)
        notifyDataSetChanged()
    }

}

fun dateFormatter(dateStr:String):String{
    val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MMM-dd")
    var date : Date = inputFormat.parse(dateStr);
    var niceDateStr :String = DateUtils.getRelativeTimeSpanString(date.time,Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS).toString()
    Log.d("daaate","$niceDateStr")
    return niceDateStr
}