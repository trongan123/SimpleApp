package com.ziichat.simpleapp.presentation.ui

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavBackStackEntry
import com.ziichat.simpleapp.presentation.ui.view.ToolBarView
import com.ziichat.simpleapp.utils.FlutterEngineUtils
import com.ziichat.simpleapp.utils.MethodChannelUtils
import com.ziichat.simpleapp.utils.NavigationUtils
import io.flutter.embedding.android.FlutterSurfaceView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel

object FlutterEngineScreen {
    const val ROUTE = "FlutterEngineScreen"
    const val ROUTE_WITH_PARAMS = "$ROUTE?numPage={numPage}"

    fun getRouteWithParam(numPage: Int?): String {
        return "${ROUTE}?numPage=$numPage"
    }

    private var eventSink: EventChannel.EventSink? = null

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(backStackEntry: NavBackStackEntry) {
        var numPage = backStackEntry.arguments?.getInt("numPage")
        var enginePoint = "page_$numPage"
        val context: Context = LocalView.current.context
        val flutterEngine = remember { mutableStateOf<FlutterEngine?>(null) }

        LaunchedEffect(Unit) {
            flutterEngine.value = FlutterEngineUtils.registerProvideFlutterEngine(
                context, "main", enginePoint, eventSink
            )
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                ToolBarView("Page $numPage") {
                    NavigationUtils.navigate(getRouteWithParam(numPage?.plus(1)))
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (flutterEngine.value == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    AndroidView(
                        factory = {
                            val flutterView = FlutterView(it, FlutterSurfaceView(it))
                            flutterView.attachToFlutterEngine(flutterEngine.value!!)
                            flutterEngine.value!!.lifecycleChannel.appIsResumed()

                            flutterView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

}