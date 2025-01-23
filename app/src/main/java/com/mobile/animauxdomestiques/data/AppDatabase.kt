package com.mobile.animauxdomestiques.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobile.animauxdomestiques.data.dao.ActivityDao
import com.mobile.animauxdomestiques.data.dao.AnimalsDao
import com.mobile.animauxdomestiques.data.dao.SpeciesDao
import com.mobile.animauxdomestiques.data.entities.Animal
import com.mobile.animauxdomestiques.data.entities.Specie
import com.mobile.animauxdomestiques.data.entities.activity.Activity
import com.mobile.animauxdomestiques.data.entities.activity.ActivityConfiguration
import com.mobile.animauxdomestiques.data.entities.activity.GlobalActivity
import com.mobile.animauxdomestiques.data.entities.activity.SpecieActivity


@Database(entities = [Specie::class, Animal::class, Activity::class, ActivityConfiguration::class, GlobalActivity::class, SpecieActivity::class], version = 14)
abstract class AppDatabase : RoomDatabase() {
    abstract fun speciesDao() : SpeciesDao
    abstract fun animalsDao() : AnimalsDao
    abstract fun activityDao() : ActivityDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(c: Context):AppDatabase{
            if (instance != null) return instance!!
            val appDatabase = Room.databaseBuilder(
                c.applicationContext,
                AppDatabase::class.java,
                "AppDatabase")
                .fallbackToDestructiveMigration()
                .build()
            instance = appDatabase
            return instance!!
        }
    }

}