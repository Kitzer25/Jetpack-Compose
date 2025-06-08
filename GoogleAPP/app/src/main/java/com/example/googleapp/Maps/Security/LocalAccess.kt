package com.example.googleapp.Maps.Security

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.googleapp.Maps.DataMaps.MallPlaza
import com.example.googleapp.Maps.ViewModel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.ktx.model.cameraPosition
import timber.log.Timber
import androidx.core.content.ContextCompat


@Composable
public fun SecurityAcces(mapVM : MapViewModel) {
    val context = LocalContext.current //Obtiene el contexto actual

    //Obtiene los datos de ubicación actual del View-Model
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val AccessFine = android.Manifest.permission.ACCESS_FINE_LOCATION
    val PassLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            mapVM.fetchcurrentLoc(context, fusedLocationClient)
        } else {
            Timber.e("Permisos de ubicación denegados por el usuario.")
        }
    }

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                AccessFine
                ) -> {
                    mapVM.fetchcurrentLoc(context, fusedLocationClient)
                }
            else -> {
                PassLauncher.launch(AccessFine)
            }
        }
    }
}