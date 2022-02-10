package com.example.myapplication.helpers.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.example.myapplication.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

const val NOTIFICATION_ID = 1

class MusicNotificationManager(
    private val context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener,
    private val songCallback: () -> Unit
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)
        notificationManager = PlayerNotificationManager.Builder(
            context, NOTIFICATION_ID, "music"
        )
            .setChannelNameResourceId(2)
            .setChannelDescriptionResourceId(3)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationListener)
            .build()
            .apply {
                setMediaSessionToken(sessionToken)
                setSmallIcon(R.drawable.noobify_splash_screen_icon)
            }
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)

    }
    private inner class DescriptionAdapter(private val mediaController: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return BitmapFactory.decodeResource(
                Resources.getSystem(),
                R.drawable.noobify_splash_screen_icon
            )
        }

    }

}