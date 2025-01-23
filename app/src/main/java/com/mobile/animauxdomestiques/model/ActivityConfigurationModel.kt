package com.mobile.animauxdomestiques.model

import com.mobile.animauxdomestiques.data.entities.activity.Activity
import com.mobile.animauxdomestiques.model.enums.ReminderType

data class ActivityConfigurationModel(
    val activityConfigurationId: Int,
    val activityId: Int,
    val name: String,
    val description: String?,
    var reminderType: ReminderType? = null,
    val time: Long? = null,
    val isConfigured: Boolean
) {

    fun toActivity() : Activity {
        return Activity(
            this.activityId,
            this.name,
            this.description
        )
    }

}