package com.xl4998.piggy.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.xl4998.piggy.MainActivity
import com.xl4998.piggy.R

/**
 * Helps with notification setup
 *
 * https://www.raywenderlich.com/1214490-android-notifications-tutorial-getting-started
 */
class NotificationHelper {

    /**
     * Creates a notification channel if the android version is > O
     */
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // Android versions > O requires channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Prepare channel
            val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // Create channel
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    /**
     * Creates the actual notification
     */
    fun showNotification(
        context: Context,
        title: String,
        message: String,
        id: Int
    ) {
        // Channel ID
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.round_card_membership_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setGroupSummary(true)
            .setGroup(channelId)

        // Prepare notification on click to show MainActivity
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        notificationBuilder.setContentIntent(pendingIntent)

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, notificationBuilder.build())
    }
}
