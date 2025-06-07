package com.example.googleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.googleapp.Maps.MapScreen
import com.example.googleapp.ui.theme.GoogleAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoogleAPPTheme {
                MapScreen()
            }

        }
    }
}
