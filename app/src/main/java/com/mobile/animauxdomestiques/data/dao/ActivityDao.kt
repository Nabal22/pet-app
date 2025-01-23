package com.mobile.animauxdomestiques.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.data.entities.Specie
import com.mobile.animauxdomestiques.data.entities.activity.Activity
import com.mobile.animauxdomestiques.data.entities.activity.ActivityConfiguration
import com.mobile.animauxdomestiques.data.entities.activity.GlobalActivity
import com.mobile.animauxdomestiques.data.entities.activity.SpecieActivity
import com.mobile.animauxdomestiques.model.ActivityConfigurationModel
import com.mobile.animauxdomestiques.model.ActivityConfigurationWithAnimal
import com.mobile.animauxdomestiques.model.enums.ReminderType
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("""
        SELECT s.*
        FROM Specie s
        INNER JOIN SpecieActivity sa ON s.specieId = sa.specieId
        WHERE sa.activityId = :activityId
    """)
    suspend fun getSpeciesByActivityId(activityId: Int): List<Specie>

    @Query("""
        SELECT a.*
        FROM Animal a
        INNER JOIN ActivityConfiguration ac ON ac.animalId = a.animalId
        WHERE ac.activityId = :activityId
    """)
    suspend fun getAnimalsByActivityId(activityId: Int): List<Animal>

    @Query("SELECT EXISTS(SELECT 1 FROM GlobalActivity WHERE activityId = :activityId)")
    suspend fun isGlobalActivity(activityId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM SpecieActivity WHERE activityId = :activityId)")
    suspend fun isSpecieActivity(activityId: Int): Boolean

    @Query("SELECT * FROM Activity INNER JOIN GlobalActivity ON Activity.activityId = GlobalActivity.activityId")
    fun getGlobalActivitiesAsList(): List<Activity>

    @Query("""
    SELECT a.activityId, a.description, a.name FROM Activity a, GlobalActivity ga
    WHERE a.activityId = ga.activityId 
    UNION
    SELECT a.activityId, a.description, a.name FROM Activity a,SpecieActivity sa
    WHERE a.activityId = sa.activityId AND sa.specieId = :specieId
    """)
    fun getAllActivitiesForSpecieAsList(specieId: Int): List<Activity>

    @Query("SELECT * FROM Activity")
    fun getAllActivitiesFlow(): Flow<List<Activity>>

    @Transaction
    @Query("SELECT * FROM ActivityConfiguration WHERE activityId = :idActivity")
    fun getAllActivitiesConfigurationFlowByActivity(idActivity: Int?): Flow<List<ActivityConfigurationWithAnimal>>

    @Transaction
    @Query("SELECT * FROM ActivityConfiguration WHERE activityId = :idActivity")
    fun getAllActivitiesConfigurationByActivity(idActivity: Int?): List<ActivityConfiguration>

    @Query("""
    SELECT ac.activityConfigurationId,  a.activityId, a.name, a.description, ac.time, ac.reminderType, ac.isConfigured FROM Activity a, ActivityConfiguration ac
    WHERE ac.activityId = a.activityId AND ac.animalId = :animalId
    """)
    fun getAllActivitiesForAnimal(animalId: Int): Flow<List<ActivityConfigurationModel>>

    @Query("""
    SELECT ac.activityConfigurationId, a.activityId, a.name, a.description, ac.time, ac.reminderType, ac.isConfigured FROM Activity a, ActivityConfiguration ac
    WHERE ac.activityId = a.activityId
    """)
    fun getAllActivitiesAsActivityConfigurationModel(): Flow<List<ActivityConfigurationModel>>

    @Query("""
        UPDATE ActivityConfiguration SET time = :time, reminderType = :reminderType, isConfigured = true
        WHERE activityConfigurationId = :activityConfigurationId
    """)
    fun updateActivityConfiguration(time : Long, reminderType: ReminderType, activityConfigurationId : Int)

    @Insert(onConflict = IGNORE)
    fun insertActivityConfiguration(activityConfiguration: ActivityConfiguration): Long

    @Insert(onConflict = IGNORE)
    suspend fun insertActivityConfigurationList(activityConfigurationList: List<ActivityConfiguration>):List<Long>

    @Insert(onConflict= IGNORE)
    suspend fun insertActivity(activity: Activity):Long

    @Insert(onConflict= IGNORE)
    suspend fun insertSpecieActivity(specieActivity: SpecieActivity):Long

    @Insert(onConflict= IGNORE)
    suspend fun insertGlobalActivity(globalActivity: GlobalActivity):Long

    @Update
    suspend fun updateActivity(activity: Activity): Int

    @Delete
    suspend fun deleteActivity(activity: Activity):Int

    @Query("DELETE FROM ActivityConfiguration WHERE activityId = :activityId")
    suspend fun deleteActivityConfigurationByActivityId(activityId: Int)

    @Query("DELETE FROM GlobalActivity WHERE activityId = :activityId")
    suspend fun deleteGlobalActivityByActivityId(activityId: Int)

    @Query("DELETE FROM SpecieActivity WHERE activityId = :activityId")
    suspend fun deleteSpecieActivityByActivityId(activityId: Int)

    @Query("DELETE FROM SpecieActivity WHERE activityId = :activityId AND specieId = :specieId")
    suspend fun deleteSpecieActivity(activityId: Int, specieId: Int)

    @Query("DELETE FROM Activity")
    suspend fun deleteAllActivities()

}