package com.mobile.animauxdomestiques

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mobile.animauxdomestiques.utils.createNotif

class NotificationWorker(val context: Context, params: WorkerParameters):Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("title") ?: "C'est l'heure d'une activité"
        val message = inputData.getString("message") ?: "Cliquez pour pouvoir y accéder"

        createNotif(context, title, message)
        return Result.success()
    }
}