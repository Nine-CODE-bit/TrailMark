package com.example.trailmark.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrailMarkerDao {

    @Query("SELECT * FROM trail_markers ORDER BY timestamp DESC")
    fun getAllMarkers(): Flow<List<TrailMarker>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marker: TrailMarker): Long

    @Delete
    suspend fun delete(marker: TrailMarker)

    @Query("DELETE FROM trail_markers WHERE id = :id")
    suspend fun deleteById(id: Long)
}
