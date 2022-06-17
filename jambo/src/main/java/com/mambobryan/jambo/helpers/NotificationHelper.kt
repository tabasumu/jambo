package com.mambobryan.jambo.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.mambobryan.jambo.data.JamboLog
import com.mambobryan.jambo.data.LogType


/**
 * Jambo
 * @author Mambo Bryan
 * @email mambobryan@gmail.com
 * Created 6/17/22 at 2:49 PM
 */
class NotificationHelper(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val NOTIFICATION_ID = 404
        private const val NORMAL_NOTIFICATION_ID = "normal_channel"
        private const val ERROR_NOTIFICATION_ID = "error_channel"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setupChannels()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val normalChannelName = "Normal channel"
        val normalChannelDescription =
            "Notifications sent when info, debug, verbose, warn and assert is logged"

        val normalChannel = NotificationChannel(
            NORMAL_NOTIFICATION_ID,
            normalChannelName,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = normalChannelDescription
            enableLights(false)
            lightColor = Color.RED
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(normalChannel)

        val errorChannelName = "Error channel"
        val errorChannelDescription = "Notifications for when error is logged"

        val errorChannel = NotificationChannel(
            ERROR_NOTIFICATION_ID,
            errorChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = errorChannelDescription
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(errorChannel)

    }

    fun notify(jamboLog: JamboLog) {

        val (channelId, autoCancel) = when (jamboLog.type) {
            LogType.ALL,
            LogType.INFO,
            LogType.VERBOSE,
            LogType.DEBUG,
            LogType.WARN,
            LogType.ASSERT -> Pair(NORMAL_NOTIFICATION_ID, true)
            LogType.ERROR -> Pair(ERROR_NOTIFICATION_ID, false)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.mipmap.jambo_launcher)
            .setContentTitle(jamboLog.type.name.lowercase().replaceFirstChar { it.uppercase() })
            .setContentText(jamboLog.tag)
            .setAutoCancel(autoCancel)
            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)

        notificationManager.notify(NOTIFICATION_ID, builder.build())

    }


}