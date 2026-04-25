package com.example.trailmark.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailmark.data.TrailMarkDatabase
import com.example.trailmark.data.TrailMarker
import com.example.trailmark.data.TrailMarkerRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TrailMarkerRepository

    /** All saved trail markers, observed from Room as a StateFlow. */
    val markers: StateFlow<List<TrailMarker>>

    /** Current device location (null until first fix). */
    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation.asStateFlow()

    /** Latest status / toast message for the UI. */
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { loc ->
                _currentLocation.value = LatLng(loc.latitude, loc.longitude)
            }
        }
    }

    init {
        val db = TrailMarkDatabase.getDatabase(application)
        repository = TrailMarkerRepository(db.trailMarkerDao())
        markers = repository.allMarkers.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )
    }

    // -------------------------------------------------------------------------
    // Location
    // -------------------------------------------------------------------------

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000L)
            .setMinUpdateIntervalMillis(2_000L)
            .build()
        fusedLocationClient.requestLocationUpdates(request, locationCallback, null)
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun fetchLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            loc?.let { _currentLocation.value = LatLng(it.latitude, it.longitude) }
        }
    }

    // -------------------------------------------------------------------------
    // Markers
    // -------------------------------------------------------------------------

    fun addMarkerAtCurrentLocation(title: String = "Trail Mark") {
        val loc = _currentLocation.value ?: run {
            _statusMessage.value = "Location not available yet"
            return
        }
        viewModelScope.launch {
            repository.addMarker(
                TrailMarker(
                    title = title,
                    latitude = loc.latitude,
                    longitude = loc.longitude
                )
            )
            _statusMessage.value = "Marker added at current location"
        }
    }

    fun deleteMarker(marker: TrailMarker) {
        viewModelScope.launch {
            repository.deleteMarker(marker)
            _statusMessage.value = "Marker deleted"
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}
