package com.mobile.animauxdomestiques.utils

import android.app.Activity.NOTIFICATION_SERVICE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.mobile.animauxdomestiques.MainActivity
import com.mobile.animauxdomestiques.NotificationWorker
import com.mobile.animauxdomestiques.model.ActivityConfigurationModel
import com.mobile.animauxdomestiques.model.enums.ReminderType
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

const val CHANNEL_ID = "activity_canal"

fun createActivityChannel(c: Context) {
    val channel = NotificationChannel(CHANNEL_ID, "ActivityChannel", NotificationManager.IMPORTANCE_DEFAULT)
    channel.description = "Activity Reminder Channel"
    val notificationManager = c.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}

fun createNotif(c: Context, title : String, message : String) {
    val notificationManager = c.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(c, MainActivity::class.java)
    val pending = PendingIntent.getActivity(c, 1, intent, PendingIntent.FLAG_IMMUTABLE)
    val builder = NotificationCompat.Builder(c, CHANNEL_ID).setSmallIcon(android.R.drawable.star_on)
        .setContentTitle(title).setContentText(message).setContentIntent(pending)
    val notification = builder.build()
    val uniqueId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
    notificationManager.notify(uniqueId, notification)
}

fun scheduleActivity(c: Context, activity: ActivityConfigurationModel, dateTime: LocalDateTime, reminderType: ReminderType) {
    val activityTag = "Reminder_${activity.activityConfigurationId}"
    val workManager = WorkManager.getInstance(c)
    workManager.cancelAllWorkByTag(activityTag)

    val now = LocalDateTime.now()
    var adjustedDateTime = dateTime
    if (dateTime.isBefore(now)) {
        adjustedDateTime = dateTime.plusDays(1)
    }

    val inputData = workDataOf(
        "title" to activity.name,
        "message" to activity.description
    )

    when (reminderType) {
        ReminderType.DAILY -> scheduleDaily(c, adjustedDateTime, activityTag, inputData)
        ReminderType.WEEKLY -> scheduleWeekly(c, adjustedDateTime, activityTag, inputData)
        ReminderType.UNIQUE -> scheduleOneShot(c, adjustedDateTime, activityTag, inputData)
    }
}

fun scheduleDaily(c: Context, dateTime: LocalDateTime, activityTag : String, inputData: Data) {
    val activityDate = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val delay = activityDate - System.currentTimeMillis()

    val periodicRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
        .setInputData(inputData)
        .addTag(activityTag)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(c).enqueueUniquePeriodicWork(
        activityTag,
        ExistingPeriodicWorkPolicy.UPDATE,
        periodicRequest
    )
}

fun scheduleWeekly(c: Context, dateTime: LocalDateTime, activityTag : String, inputData: Data) {
    val activityDate = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val delay = activityDate - System.currentTimeMillis()

    val periodicRequest = PeriodicWorkRequestBuilder<NotificationWorker>(7, TimeUnit.DAYS)
        .setInputData(inputData)
        .addTag(activityTag)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(c).enqueueUniquePeriodicWork(
        activityTag,
        ExistingPeriodicWorkPolicy.UPDATE,
        periodicRequest
    )
}

fun scheduleOneShot(c: Context, dateTime: LocalDateTime, activityTag : String, inputData: Data) {
    val activityDate = dateTime.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
    val delay = activityDate - System.currentTimeMillis()
    if (delay > 0) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(activityTag)
            .build()

        WorkManager.getInstance(c).enqueueUniqueWork(
            activityTag,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}