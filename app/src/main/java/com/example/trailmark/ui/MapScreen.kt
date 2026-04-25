package com.example.trailmark.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trailmark.data.TrailMarker
import com.example.trailmark.viewmodel.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    val markers by viewModel.markers.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showMarkersList by remember { mutableStateOf(false) }

    // Default camera position (Beijing, China — a placeholder before location is available)
    val defaultPosition = LatLng(39.9042, 116.4074)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultPosition, 12f)
    }

    // React to permission changes
    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            viewModel.fetchLastKnownLocation()
            viewModel.startLocationUpdates()
        }
    }

    // Clean up location updates when composable leaves composition
    DisposableEffect(Unit) {
        onDispose { viewModel.stopLocationUpdates() }
    }

    // Move camera to current location when it becomes available
    LaunchedEffect(currentLocation) {
        currentLocation?.let { loc ->
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(loc, 15f)
                )
            )
        }
    }

    // Show snackbar on status messages
    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.clearStatusMessage()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("TrailMark") },
                actions = {
                    IconButton(onClick = { showMarkersList = true }) {
                        Icon(Icons.Default.List, contentDescription = "Marker List")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = {
                            if (locationPermissionsState.allPermissionsGranted) {
                                viewModel.fetchLastKnownLocation()
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newCameraPosition(
                                            CameraPosition.fromLatLngZoom(
                                                currentLocation ?: defaultPosition, 15f
                                            )
                                        )
                                    )
                                }
                            } else {
                                locationPermissionsState.launchMultiplePermissionRequest()
                            }
                        }
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "My Location")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            if (locationPermissionsState.allPermissionsGranted) {
                                viewModel.addMarkerAtCurrentLocation()
                            } else {
                                locationPermissionsState.launchMultiplePermissionRequest()
                            }
                        }
                    ) {
                        Icon(Icons.Default.AddLocation, contentDescription = "Add Marker")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!locationPermissionsState.allPermissionsGranted) {
                // Permission rationale UI
                PermissionRationaleContent(
                    onRequestPermission = {
                        locationPermissionsState.launchMultiplePermissionRequest()
                    }
                )
            } else {
                // Google Map
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = false)
                ) {
                    // Draw saved markers
                    markers.forEach { marker ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(marker.latitude, marker.longitude)
                            ),
                            title = marker.title,
                            snippet = formatTimestamp(marker.timestamp)
                        )
                    }
                }
            }
        }
    }

    // Bottom sheet: list of saved markers
    if (showMarkersList) {
        ModalBottomSheet(
            onDismissRequest = { showMarkersList = false },
            sheetState = sheetState
        ) {
            MarkersListSheet(
                markers = markers,
                onDeleteMarker = { viewModel.deleteMarker(it) },
                onDismiss = { showMarkersList = false }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Permission rationale card
// ---------------------------------------------------------------------------

@Composable
private fun PermissionRationaleContent(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Location Permission Required",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "TrailMark needs your location to display the map and record trail markers.",
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onRequestPermission) {
                    Text("Grant Permission")
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Markers list bottom sheet content
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MarkersListSheet(
    markers: List<TrailMarker>,
    onDeleteMarker: (TrailMarker) -> Unit,
    onDismiss: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Text(
            text = "Trail Markers (${markers.size})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (markers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No markers yet. Tap the + button on the map to add one.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(markers, key = { it.id }) { marker ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = marker.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        supportingContent = {
                            Text(
                                text = "${formatTimestamp(marker.timestamp)}  " +
                                        "%.5f, %.5f".format(marker.latitude, marker.longitude),
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { onDeleteMarker(marker) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Marker",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Helper
// ---------------------------------------------------------------------------

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
