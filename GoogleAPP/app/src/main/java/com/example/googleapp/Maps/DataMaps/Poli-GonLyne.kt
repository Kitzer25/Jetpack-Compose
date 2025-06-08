package com.example.googleapp.Maps.DataMaps

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline

@Composable
public fun Poligonales(){
    Polygon(
        points = parqueLambramaniPolygon,
        fillColor = Color(0xff2c6494),
        strokeColor = Color.Red,
        strokeWidth = 30f,
        clickable = true,
        onClick = {
            println("Polilineas")
        }
    )

    Polygon(
        points = mallAventuraPolygon,
        fillColor = Color(0xff2c6494),
        strokeColor = Color.Red,
        strokeWidth = 30f,
        clickable = true,
        onClick = {
            println("Polilineas")
        }
    )

    Polygon(
        points = plazaDeArmasPolygon,
        fillColor = Color(0xff2c6494),
        strokeColor = Color.Red,
        strokeWidth = 30f,
        clickable = true,
        onClick = {
            println("Polilineas")
        }
    )
}

@Composable
public fun Polilineas(){
    Polyline(
        points = mallAventurapolilyne,
        color = Color(0xff25669d).copy(alpha = 0.7f),
        width = 12f,
        clickable = true,
        onClick = {
            Log.d("Ruta", "Polilinea Seleccionada")
        }
    )

    Polyline(
        points = plazadeArmaspolilyne,
        color = Color(0xffcc2851).copy(alpha = 0.7f),
        width = 12f,
        clickable = true,
        onClick = {
            Log.d("Ruta", "Polilinea Seleccionada")
        }
    )
}