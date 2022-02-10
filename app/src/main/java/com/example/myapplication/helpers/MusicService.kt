package com.example.myapplication.helpers

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import javax.inject.Inject

private const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService :
    MediaBrowserServiceCompat() { //showing foreground music service in the status notification bar

    @Inject
    lateinit var dataSourceFactory: DefaultMediaSourceFactory

    @Inject
    lateinit var exoPlayer: ExoPlayer

    /*handles coroutines task scope and lifetime i.e remains only
    *in our service scope and dies after our service dies*/
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession:MediaSessionCompat //Allows interaction with media controllers
    private lateinit var mediaSessionConnector: MediaSessionConnector //connects mediaSessionCompat to Exoplayer

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        //our activity will open when our mediaPlayer in the notification get clicked
        val activity = packageManager?.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this,0,it,0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activity) //opens the app activity
            isActive = true //Sets if this session is currently active
        }

        sessionToken = mediaSession.sessionToken //sets currentmedia player sessionToken

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(exoPlayer)


    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() //making sure all coroutines launched in the serviceScope cancels
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }
}