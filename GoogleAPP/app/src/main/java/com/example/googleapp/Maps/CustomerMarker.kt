package com.example.googleapp.Maps

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.All
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.Coil.imageLoader
import coil.ImageLoader
import coil.request.SuccessResult
import com.android.volley.toolbox.ImageRequest
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState


@Composable
fun CustomMapMarker(
    imageUrl: String?,
    fullName: String,
    location: LatLng,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val markerState = remember { MarkerState(position = location) }
    val shape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 0.dp)

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Cargar imagen con Coil manualmente
    LaunchedEffect(imageUrl) {
        if (!imageUrl.isNullOrEmpty()) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()
            val result = (loader.execute(request) as? SuccessResult)?.drawable
            result?.let { drawable ->
                val bitmap = drawable.toBitmap()
                imageBitmap = bitmap.asImageBitmap()
            }
        }
    }

    MarkerComposable(
        keys = arrayOf(fullName, imageBitmap),
        state = markerState,
        title = fullName,
        anchor = Offset(0.5f, 1f),
        onClick = {
            onClick()
            true
        }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(shape)
                .background(Color.LightGray)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = fullName.take(1).uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
