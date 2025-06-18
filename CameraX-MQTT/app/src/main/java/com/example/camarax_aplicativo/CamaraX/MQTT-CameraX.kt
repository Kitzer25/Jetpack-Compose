package com.example.camarax_aplicativo.CamaraX

import android.Manifest
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.camarax_aplicativo.MQTT.MqttClientManager
import java.io.File


@Composable
fun Camerax_MQTT() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var requestPermission by remember { mutableStateOf(true) }

    /* Captura Imagen desde Fuera */
    val imageCaptureRef = remember { mutableStateOf<ImageCapture?>(null) }

    fun capturarImagen_Enviar() {
        val ctx = context
        val imageCapture = imageCaptureRef.value ?: return

        val photoFile = File(
            ctx.externalMediaDirs.first(),
            "captura-${System.currentTimeMillis()}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(ctx),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val imagen = photoFile.readBytes()
                    val imagenBase64 = android.util.Base64.encodeToString(
                        imagen,
                        android.util.Base64.DEFAULT)
                    MqttClientManager.publish("Android/Fotografia", imagenBase64)
                    Toast.makeText(ctx, "Imagen Enviada", Toast.LENGTH_SHORT).show()
                }


                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(ctx, "Error en la captura: ${exc.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    /* Conexión MQTT con suscripción */
    LaunchedEffect(Unit) {
        MqttClientManager.connect(
            serverUri = "tcp://161.132.48.224:1883",
            username = "esp32",
            password = "tecsup123",
            onConnected = {
                MqttClientManager.subscribe("ESP32/Captura")
            }
        ) { mensaje ->
            if (mensaje == "fotografiar") {
                capturarImagen_Enviar()
            }
        }
    }


    /* Permiso de cámara */
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
            }
        }
    )

    LaunchedEffect(launcher) {
        if (requestPermission) {
            launcher.launch(Manifest.permission.CAMERA)
            requestPermission = false
        }
    }

    /* Vista de cámara */
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageCapture = ImageCapture.Builder().build()
                imageCaptureRef.value = imageCapture // ✅ Aquí se guarda la referencia

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, cameraSelector, preview, imageCapture
                )

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}


