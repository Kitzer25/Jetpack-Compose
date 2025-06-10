package com.example.animations.animaciones

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.animateColor
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun VisibilityAnimate(){
    var esVisible by remember { mutableStateOf(true) }
    val densidad = LocalDensity.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { esVisible = !esVisible }) {
            Text(text = if (esVisible) "Hide Box" else "Show Box") }
    }

    AnimatedVisibility(
        visible = esVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ){
            Box(
                Modifier
                    .align(Alignment.Center)
                    .animateEnterExit(
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    )
                    .sizeIn(minWidth = 256.dp, minHeight = 69.dp)
                    .background(Color.Blue)
            )
        }
    }
}