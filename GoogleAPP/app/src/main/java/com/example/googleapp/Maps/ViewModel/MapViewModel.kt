package com.example.googleapp.Maps.ViewModel

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

class MapViewModel: ViewModel() {

    private val _currentLocation = mutableStateOf<LatLng?>(null)
    val currentLoc: State<LatLng?> = _currentLocation

    //Acceso y Paquete
    val AccessLoc = android.Manifest.permission.ACCESS_FINE_LOCATION
    val AccessPack = PackageManager.PERMISSION_GRANTED

    fun fetchcurrentLoc(
        context: Context,
        fusedLocClient: FusedLocationProviderClient){

        if(ContextCompat.checkSelfPermission(
            context,
            AccessLoc
        ) == AccessPack){
            try {
                fusedLocClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val currentLat = LatLng(it.latitude, it.longitude)
                        _currentLocation.value = currentLat
                    }
                }
            }catch (e: SecurityException){
                Timber.e("Permisos de de acceso a la ubicación denegados: ${e.localizedMessage}")
            }
        }else{
            Timber.e("Persisos de acceso a la ubicación concedidos")
        }
    }

}