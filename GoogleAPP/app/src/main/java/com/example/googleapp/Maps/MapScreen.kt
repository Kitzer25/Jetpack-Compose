package com.example.googleapp.Maps

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.googleapp.Maps.DataMaps.ArequipaLocation
import com.example.googleapp.Maps.DataMaps.MallPlaza
import com.example.googleapp.Maps.DataMaps.MarkerMultiple
import com.example.googleapp.Maps.DataMaps.Poligonales
import com.example.googleapp.Maps.DataMaps.Polilineas
import com.example.googleapp.Maps.DataMaps.mallAventurapolilyne
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
@Preview
fun MapScreen() {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 12f)
    }

    var uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Añadir GoogleMap al layout
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            //Implementación de propiedades del Mapa
            properties = properties,
            uiSettings = uiSettings
        ) {

            //Poligonos - Polilineas Funciones
            //Poligonales()

            //Polilineas()


            //Efecto de cámara
            LaunchedEffect(Unit) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory
                        .newLatLngZoom(MallPlaza, 12f),
                    durationMs = 6000
                )
            }

            /*Marcadores Funciones*/
            //addCustomMarker()

            //MarkerMultiple()

            //IconMarker()

            //initMarker()


        }
    }
}