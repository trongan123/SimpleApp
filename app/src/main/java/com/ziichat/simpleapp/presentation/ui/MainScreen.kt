package com.ziichat.simpleapp.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import com.ziichat.simpleapp.ZiiChatFlutterActivity
import com.ziichat.simpleapp.utils.NavigationUtils

object MainScreen {
    const val ENGINE_POINT = "main"
    const val ROUTE = "MainScreen"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen() {
        val context: Context = LocalView.current.context
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        var intent = Intent(context, ZiiChatFlutterActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Text("Push FlutterActivity", color = Color.White)
                    }
                    Button(onClick = {
                        NavigationUtils.navigate(FlutterEngineScreen.getRouteWithParam(1))
                    }) {
                        Text("Push a page", color = Color.White)
                    }
                    Button(onClick = {
                        (1 until 11).forEach {
                            NavigationUtils.navigate(FlutterEngineScreen.getRouteWithParam(it))
                        }

                    }) {
                        Text("Push 10 page", color = Color.White)
                    }
                    Button(onClick = {
                        (1 until 101).forEach {
                            NavigationUtils.navigate(FlutterEngineScreen.getRouteWithParam(it))
                        }
                    }) {
                        Text("Push 100 page", color = Color.White)
                    }
                }
            }
        }
    }
}