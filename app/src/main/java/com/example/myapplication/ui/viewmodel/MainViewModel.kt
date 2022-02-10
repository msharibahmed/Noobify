package com.example.myapplication.ui.viewmodel

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.helpers.Resource
import com.example.myapplication.helpers.exoplayer.*
import com.example.myapplication.models.PlaylistItemModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {


    private val _mediaItems = MutableLiveData<Resource<List<PlaylistItemModel.Short>>>()
    val mediaItems: LiveData<Resource<List<PlaylistItemModel.Short>>> = _mediaItems

    val isConnection = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingSong = musicServiceConnection.curPlayingSong
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)

                    val items = children.map {
                        PlaylistItemModel.Short(
                            it.description.mediaUri.toString(),
                            PlaylistItemModel.Short.Creator(
                                it.description.subtitle.toString(),
                                it.description.subtitle.toString()
                            ),
                            "",
                            it.mediaId!!,
                            it.description.title.toString()
                        )
                    }
                    _mediaItems.postValue(Resource.success(items))
                }
            })
    }

    fun skipToNextSong() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun fastForward() {
        musicServiceConnection.transportControls.fastForward()
    }

    fun rewind() {
        musicServiceConnection.transportControls.rewind()
    }

    fun seekTo(speed: Float) {
        musicServiceConnection.transportControls.setPlaybackSpeed(speed)
    }


    fun playOrToggleSong(mediaItem: PlaylistItemModel.Short, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if(isPrepared && mediaItem.shortID ==
            curPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if(toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.shortID, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {})
    }


}