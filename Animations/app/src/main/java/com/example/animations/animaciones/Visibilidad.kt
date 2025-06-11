package com.example.animations.animaciones

import android.R
import android.R.attr.onClick
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animations.animaciones.Estados

//@Preview(showBackground = true)
@Composable
fun VisibilityAnimate() {
    var esVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { esVisible = !esVisible }) {
            Text(text = if (esVisible) "Ocultar Caja" else "Mostrar Caja")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = esVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            Box(
                modifier = Modifier
                    .size(width = 256.dp, height = 64.dp)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Caja visible",
                    color = Color.White
                )
            }
        }
    }
}

//@Preview
@Composable
fun ColorStateChange(){
    var colorchange by remember { mutableStateOf(true) }

    val boxcolor by animateColorAsState(
        targetValue = if(!colorchange) Color(0xFFD91D1D) else Color(0xFF143862)
    )

    Column(Modifier
        .background(Color(0xFFA98E2F))
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Button(
            onClick = { colorchange = !colorchange },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E35A8),
                contentColor = Color(0xffffffff)
            )
            ) {
            Text( if(colorchange) "Azul" else "Rojo")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Box(Modifier
            .size(200.dp)
            .background(boxcolor),
            //.clickable {color = !color}
            contentAlignment = Alignment.Center
        ){}
    }
}

//@Preview
@Composable
fun ChangeAttributes(){
    var movimiento by remember { mutableStateOf(false) }

    val offsetX by animateDpAsState(
        targetValue = if (movimiento) 140.dp else 10.dp,
        animationSpec = tween(600)
    )
    val offsetY by animateDpAsState(
        targetValue = if (movimiento) 300.dp else 10.dp,
        animationSpec = tween(600)
    )

    val boxsize by animateDpAsState(
        targetValue = if(movimiento) 250.dp else 120.dp,
        animationSpec = tween(600)
    )

    Column(Modifier
        .fillMaxSize()
        .background(Color(0xFFB4B4B4))) {

        Box(Modifier
            .offset(x = offsetX, y = offsetY)
            .size(boxsize)
            .background(Color(0xFF2436B2))
            .clickable{
                movimiento = !movimiento
            }) {  }
    }


}


@Preview
@Composable
fun EstadoAnimation() {
    var estado by remember { mutableStateOf(Estados.Cargando) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB6B6B6)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { estado = Estados.Cargando },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF479D4A),
                    contentColor = Color(0xFFFFFFFF)
                )) {
                Text("Cargando")
            }
            Button(onClick = { estado = Estados.Contenido },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC23636),
                    contentColor = Color(0xFFFFFFFF))
            ) {
                Text("Contenido")
            }
            Button(onClick = { estado = Estados.Error },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000),
                    contentColor = Color(0xFFFFFFFF))
                ) {
                Text("Error")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Contenido animado
        AnimatedContent(
            targetState = estado,
            transitionSpec = {
                fadeIn(animationSpec = tween(700)) togetherWith
                        fadeOut(animationSpec = tween(500))
            },
            label = "AnimacionEstado"
        ) { currentState ->
            when (currentState) {
                Estados.Cargando -> {
                    Text("Cargando...", fontSize = 24.sp, color = Color.Gray)
                }
                Estados.Contenido -> {
                    Text("Contenido mostrado ✅", fontSize = 24.sp, color = Color(0xFF00796B))
                }
                Estados.Error -> {
                    Text("Ocurrió un error ❌", fontSize = 24.sp, color = Color.Red)
                }
            }
        }
    }
}