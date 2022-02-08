package com.example.myapplication.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PlaylistItemBinding
import com.example.myapplication.models.PlaylistItemModel
import java.text.SimpleDateFormat
import java.util.*

class PlaylistAdapter(private val context: Context, private val listener: CustomItemClickListener) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistItemViewHolder>() {
    private var myList = ArrayList<PlaylistItemModel.Short>()

    inner class PlaylistItemViewHolder(binding: PlaylistItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var name = binding.textCreatorName
        var username = binding.textCreatorUsername
        var contact = binding.btnCreatorContact
        var avatar = binding.imgCreatorAvatar
        var createdAt = binding.textCreatedAt
        var songName = binding.textSongName


        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClickChangeMiniPlayer(myList[adapterPosition], adapterPosition)
        }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaylistItemViewHolder, position: Int) {

        holder.name.text = "Anonymous" + myList[position].creator.userID
        holder.username.text = "@anonymous" + myList[position].creator.userID
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
    fun getNextItem(index: Int):PlaylistItemModel.Short{
        return myList[index]
    }


}

@SuppressLint("SimpleDateFormat")
fun dateFormatter(dateStr: String): String {
    val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MMM-dd")
    val date: Date? = inputFormat.parse(dateStr);
    //Log.d("daaate","$niceDateStr")
    return DateUtils.getRelativeTimeSpanString(
        date!!.time,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}

interface CustomItemClickListener {
    fun onItemClickChangeMiniPlayer(playlistItemModel: PlaylistItemModel.Short, index: Int)
}
