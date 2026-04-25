package com.example.trailmark.data

import kotlinx.coroutines.flow.Flow

class TrailMarkerRepository(private val dao: TrailMarkerDao) {

    val allMarkers: Flow<List<TrailMarker>> = dao.getAllMarkers()

    suspend fun addMarker(marker: TrailMarker): Long = dao.insert(marker)

    suspend fun deleteMarker(marker: TrailMarker) = dao.delete(marker)

    suspend fun deleteMarkerById(id: Long) = dao.deleteById(id)
}
