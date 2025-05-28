package com.ziichat.simpleapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import io.flutter.FlutterInjector
import io.flutter.embedding.android.ExclusiveAppComponent
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.plugin.common.EventChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FlutterEngineUtils {

    var activity: ExclusiveAppComponent<Activity>? = null
    var lifecycle: Lifecycle? = null
    var ENGINE_MAIN = "main"
    var ENGINE_SERVICE = "runServices"

    private val engines = mutableMapOf<String, FlutterEngine>()
    private var engineGroup: FlutterEngineGroup? = null

    fun initialize(application: Application) {
        engineGroup = FlutterEngineGroup(application)
    }

    fun setActivity(activity: ExclusiveAppComponent<Activity>, lifecycle: Lifecycle) {
        this.activity = activity
        this.lifecycle = lifecycle
    }

    fun getEngineGroup(context: Context): FlutterEngineGroup {
        engineGroup?.let { return it }
        engineGroup = FlutterEngineGroup(context)
        return engineGroup!!
    }

    suspend fun registerProvideFlutterEngine(
        context: Context,
        entryPoint: String,
        route: String,
        eventSink: EventChannel.EventSink?
    ): FlutterEngine {
        getEngine(route)?.let { return it }
        var engineGroup = getEngineGroup(context)

        val dartEntrypoint = DartEntrypoint(
            FlutterInjector.instance().flutterLoader().findAppBundlePath(),
            entryPoint
        )

        val engine = withContext(Dispatchers.Main) {
            engineGroup.createAndRunEngine(
                context,
                dartEntrypoint
            ).also {
                if (activity != null && lifecycle != null) {
                    it.activityControlSurface.attachToActivity(activity!!, lifecycle!!)
                }
                GeneratedPluginRegistrant.registerWith(it)
                MethodChannelUtils.setMethodChannel(it, eventSink)
            }
        }

        engines[route] = engine
        return engine
    }


    fun destroyEngine(entryPoint: String) {
        engines[entryPoint]?.let { engine ->
            engine.destroy()
            engines.remove(entryPoint)
        }
    }

    fun preloadMainFlutterEngine(context: Context) {
        val engineGroup = getEngineGroup(context)
        val dartEntrypoint = DartEntrypoint(
            FlutterInjector.instance().flutterLoader().findAppBundlePath(),
            ENGINE_MAIN
        )

        val engine = engineGroup.createAndRunEngine(context, dartEntrypoint).also {
            GeneratedPluginRegistrant.registerWith(it)
        }
        storeEngine(ENGINE_MAIN, engine)
    }

    fun storeEngine(entryPoint: String, engine: FlutterEngine) {
        engines[entryPoint] = engine
    }

    fun getEngine(entryPoint: String): FlutterEngine? {
        return engines[entryPoint]
    }

    fun preloadServiceFlutterEngine(context: Context): FlutterEngine {
        getEngine(ENGINE_SERVICE)?.let { return it }
        val engineGroup = getEngineGroup(context)
        val dartEntrypoint = DartEntrypoint(
            FlutterInjector.instance().flutterLoader().findAppBundlePath(),
            ENGINE_SERVICE
        )

        val engine = engineGroup.createAndRunEngine(context, dartEntrypoint)
        storeEngine(ENGINE_SERVICE, engine)
        return engine
    }

}