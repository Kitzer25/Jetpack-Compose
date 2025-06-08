package com.example.googleapp.Maps


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.googleapp.Maps.Security.SecurityAcces
import com.example.googleapp.Maps.ViewModel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(mapVM: MapViewModel) {
    val cameraPosition = rememberCameraPositionState() //Inicializa la cámara

    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.HYBRID)) }

    /*Acceso a la ubicación - Función*/
    val currentLocation by mapVM.currentLoc
    SecurityAcces(mapVM)

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition,
            //Implementación de propiedades del Mapa
            properties = properties,
            uiSettings = uiSettings
        ) {

            currentLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Mi Ubicación",
                    snippet = "ubicación actual"
                )

                cameraPosition.position = CameraPosition.fromLatLngZoom(it, 10f)
            }

            /*Marcadores Funciones*/
            //addCustomMarker()
            //MarkerMultiple()
            //IconMarker()
            //initMarker()


            /*Poligonos - Polilineas Funciones*/
            //Poligonales()
            //Polilineas()
        }
    }
}