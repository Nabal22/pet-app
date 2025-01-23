package com.mobile.animauxdomestiques.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Update
import com.mobile.animauxdomestiques.data.entities.Animal
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalsDao {

    @Insert(onConflict= IGNORE)
    suspend fun insertAnimal(animal: Animal):Long

    @Update
    suspend fun updateAnimal(animal: Animal):Int

    @Query("SELECT * FROM Animal")
    fun getAllAnimalsFlow():Flow<List<Animal>>

    @Query("SELECT * FROM Animal")
    fun getAllAnimalsList():List<Animal>

    @Query("SELECT * FROM Animal WHERE specieId = :specieId")
    fun getAllAnimalsListBySpecieId(specieId : Int):List<Animal>

    @Query("SELECT * FROM Animal WHERE specieId = :specieId")
    fun getAllAnimalsFlowBySpecieId(specieId : Int?):Flow<List<Animal>>

    @Delete
    suspend fun deleteAnimal(animal: Animal):Int

    @Query("DELETE FROM Animal")
    suspend fun deleteAllAnimals()
}