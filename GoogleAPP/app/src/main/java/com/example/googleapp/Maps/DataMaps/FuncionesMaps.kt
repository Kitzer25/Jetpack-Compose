package com.example.googleapp.Maps.DataMaps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.googleapp.Maps.CustomMapMarker
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState


//Marcador customizable
@Composable
fun addCustomMarker(){
    CustomMapMarker(
        imageUrl = "https://api.hinlen.com/media/artist/2025/02/15/287bb6f4a04243ee9af34161f6e6949c.webp",
        fullName = "Mauricio Alexander",
        location = ArequipaLocation,
        onClick = { }
    )
}


//Múltiples marcadores
@Composable
fun MarkerMultiple(){
    locations.forEach { location ->
        Marker(
            state = rememberMarkerState(position = location),
            title = "Ubicación",
            snippet = "Puntos de encuentro"
        )
    }
}


//Marcador con Ícono
@Composable
fun IconMarker(){
    MarkerComposable(
        state = rememberMarkerState(position = ArequipaLocation),
        title = "Centro Histórico"
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}



//Marcador Inicial
@Composable
fun initMarker(){
    Marker(
        state = rememberMarkerState(position = ArequipaLocation),

        icon = BitmapDescriptorFactory.defaultMarker(),

        title = "Arequipa, Perú",
        snippet = "Población: 1.606.000 (2024)"

    )
}

