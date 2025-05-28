package com.ziichat.simpleapp

import android.app.Application
import com.ziichat.simpleapp.utils.FlutterEngineUtils
import com.ziichat.simpleapp.utils.FlutterManagerSingleton
import io.flutter.FlutterInjector

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FlutterManagerSingleton.initialize(this)
        FlutterEngineUtils.initialize(this)

        val loader = FlutterInjector.instance().flutterLoader()
        loader.startInitialization(this)
        loader.ensureInitializationComplete(this, null)

        FlutterEngineUtils.preloadServiceFlutterEngine(this)
        FlutterEngineUtils.preloadMainFlutterEngine(this)
    }

}