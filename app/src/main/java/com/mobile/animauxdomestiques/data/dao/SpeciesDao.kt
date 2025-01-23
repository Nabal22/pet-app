package com.mobile.animauxdomestiques.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Update
import com.mobile.animauxdomestiques.data.entities.Specie
import kotlinx.coroutines.flow.Flow

@Dao
interface SpeciesDao {

    @Insert(onConflict= IGNORE)
    suspend fun insertSpecie(specie: Specie):Long

    @Query("SELECT * FROM Specie")
    fun getAllSpeciesFlow():Flow<List<Specie>>

    @Query("SELECT * FROM Specie WHERE Specie.specieId = :specieId")
    fun getSpecieById(specieId: Int):Flow<Specie>

    @Update
    suspend fun updateSpecie(specie: Specie):Int

    @Delete
    suspend fun deleteSpecie(specie: Specie):Int

    @Query("DELETE FROM Specie")
    suspend fun deleteAllSpecies()
}