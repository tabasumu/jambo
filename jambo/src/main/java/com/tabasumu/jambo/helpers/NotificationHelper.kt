package com.tabasumu.jambo.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tabasumu.jambo.BuildConfig
import com.tabasumu.jambo.R
import com.tabasumu.jambo.data.JamboLog
import com.tabasumu.jambo.data.LogType
import com.tabasumu.jambo.ui.JamboActivity


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
        private const val NORMAL_NOTIFICATION_ID = "normal_channel"
        private const val ERROR_NOTIFICATION_ID = "error_channel"
        private const val GROUP_NOTIFICATION_ID = "jambo_notifications"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setupChannels()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val normalChannelName = "Normal Logs"
        val normalChannelDescription =
            "Notifications sent when info, debug, verbose, warn and assert is logged"

        val normalChannel = NotificationChannel(
            NORMAL_NOTIFICATION_ID,
            normalChannelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = normalChannelDescription
            lightColor = Color.BLUE
            enableLights(false)
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(normalChannel)

        val errorChannelName = "Error Logs"
        val errorChannelDescription = "Notifications for when error is logged"

        val defaultRing = Uri.parse(
            "android.resource://"
                    + BuildConfig.LIBRARY_PACKAGE_NAME + "/" + R.raw.notification
        );
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val errorChannel = NotificationChannel(
            ERROR_NOTIFICATION_ID,
            errorChannelName,
            NotificationManager.IMPORTANCE_MAX
        ).apply {
            description = errorChannelDescription
            lightColor = Color.RED
            setSound(defaultRing, audioAttributes)
            enableLights(true)
            enableVibration(true)
        }

        notificationManager.createNotificationChannel(errorChannel)

    }

    fun notify(jamboLog: JamboLog) {

        val (channelId, notifyId) = when (jamboLog.type) {
            LogType.ALL,
            LogType.INFO,
            LogType.VERBOSE,
            LogType.DEBUG,
            LogType.WARN,
            LogType.ASSERT -> Pair(NORMAL_NOTIFICATION_ID, 200)
            LogType.ERROR -> Pair(ERROR_NOTIFICATION_ID, 404)
        }

        val notificationIntent = Intent(context, JamboActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.jambo_notification)
            .setContentTitle(jamboLog.getFullTag())
            .setContentText(jamboLog.message)
            .setAutoCancel(true)
            .setGroup(GROUP_NOTIFICATION_ID)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notifyId, builder.build())

    }


}