package com.example.googleapp.Maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.googleapp.Maps.DataMaps.ArequipaLocation
import com.example.googleapp.Maps.DataMaps.IconMarker
import com.example.googleapp.Maps.DataMaps.MallPlaza
import com.example.googleapp.Maps.DataMaps.MarkerMultiple
import com.example.googleapp.Maps.DataMaps.addCustomMarker
import com.example.googleapp.Maps.DataMaps.initMarker
import com.example.googleapp.Maps.DataMaps.locations
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.Marker


@Composable
@Preview
fun MapScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            //Efecto de cámara
            LaunchedEffect(Unit) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory
                        .newLatLngZoom(MallPlaza, 12f),
                    durationMs = 6000
                )
            }

            /*FUNCIONES*/
            //addCustomMarker()

            MarkerMultiple()

            //IconMarker()

            //initMarker()

        }
    }
}
