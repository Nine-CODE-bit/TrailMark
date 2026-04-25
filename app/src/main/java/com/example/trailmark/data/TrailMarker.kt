package com.example.trailmark.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a saved trail marker (check-in point) on the map.
 *
 * In a full implementation this would map to the CheckInLog table from the report.
 * For the baseline build we keep it simple with just coordinates and a title.
 */
@Entity(tableName = "trail_markers")
data class TrailMarker(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)
