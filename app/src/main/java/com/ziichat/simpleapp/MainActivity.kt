package com.ziichat.simpleapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ziichat.simpleapp.presentation.navigation.AppNavigation
import com.ziichat.simpleapp.presentation.theme.SimpleAppTheme
import com.ziichat.simpleapp.utils.FlutterEngineUtils
import io.flutter.embedding.android.ExclusiveAppComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val exclusiveActivity = object : ExclusiveAppComponent<Activity> {
            override fun detachFromFlutterEngine() {
            }

            override fun getAppComponent(): Activity = this@MainActivity
        }
        FlutterEngineUtils.setActivity(exclusiveActivity, this.lifecycle)

        enableEdgeToEdge()
        setContent {
            SimpleAppTheme {
                AppNavigation()
            }
        }
    }
}

