package com.example.googleapp.Maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable


@Composable
@Preview
fun MapScreen() {
    val ArequipaLocation = LatLng(-16.4040102, -71.559611) // Arequipa, Perú
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    val locations = listOf(
        LatLng(-16.433415, -71.5442652),
        LatLng(-16.4205151, -71.495209),
        LatLng(-16.3524187, -71.5675994)
    )
    /*
    locations.forEach { location ->
        Marker(
            state =rememberMarkerState(position = location),
            title = "Ubicacion",
            snippet = "Puntos de encuentro"
        )
    }*/

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            //Marcador Customizable
            CustomMapMarker(
                imageUrl = "https://api.hinlen.com/media/artist/2025/02/15/287bb6f4a04243ee9af34161f6e6949c.webp",
                fullName = "Mauricio Alexander",
                location = ArequipaLocation,
                onClick = { }
            )


            // Añadir marcador
            /*Marker(
                state = rememberMarkerState(position = ArequipaLocation),

                icon = BitmapDescriptorFactory.defaultMarker(),

                title = "Arequipa, Perú",
                snippet = "Población: 1.606.000 (2024)"

            )


            //Marcador con Ícono
            MarkerComposable (
                state = rememberMarkerState(position = ArequipaLocation),
                title = "Centro Histórico"
            ){
                Box(
                    modifier = Modifier
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.matchParentSize()
                    )

                }
            }*/
        }
    }
}
