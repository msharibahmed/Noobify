package com.example.myapplication.helpers

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.myapplication.databinding.PlaylistItemBinding
import com.example.myapplication.models.Song
import javax.inject.Inject

open class SongAdapter
@Inject
constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(binding: PlaylistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.textCreatorName
        var username = binding.textCreatorUsername
        var createdAt = binding.textCreatedAt
        var songName = binding.textSongName

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

            itemView.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(song)
                    Log.d("kl", song.toString())
                }
            }
        }
    }

    private var onItemClickListener: ((Song) -> Unit)? = null

    fun setOnItemClickListener(listener: (Song) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return songs.size
    }
}

/*
class PlaylistAdapter
@Inject
constructor(
    private val glide: RequestManager,
    private val context: Context,
    private val listener: CustomItemClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistItemViewHolder>() {

    inner class PlaylistItemViewHolder(binding: PlaylistItemBinding) :
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


    val songs = ArrayList<PlaylistItemModel.Short>()

    @SuppressLint("NotifyDataSetChanged")
    fun add(items: List<PlaylistItemModel.Short>) {
        songs.addAll(items)
        notifyDataSetChanged()
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
        val song: PlaylistItemModel.Short = songs[position]
        holder.apply {
            name.text = "Anonymous" + song.creator.userID
            username.text = "@anonymous" + song.creator.userID
            //createdAt.text = dateFormatter(song.dateCreated)
            songName.text = song.title
        }


    }

    override fun getItemCount(): Int {
        return songs.size
    }

    fun getNextItem(index: Int): PlaylistItemModel.Short {
        return songs[index]
    }


}

@SuppressLint("SimpleDateFormat")
fun dateFormatter(dateStr: String?): String {
    val inputFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MMM-dd")

    val date: Date? = inputFormat.parse(dateStr)
    //Log.d("date","$niceDateStr")
    return DateUtils.getRelativeTimeSpanString(
        date!!.time,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS
    ).toString()
}


interface CustomItemClickListener {
    fun onItemClickChangeMiniPlayer(playlistItemModel: PlaylistItemModel.Short, index: Int)
} */