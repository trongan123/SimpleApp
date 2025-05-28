package com.ziichat.simpleapp.utils

import android.annotation.SuppressLint
import android.content.Context
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.JSONMessageCodec
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class FlutterManagerSingleton private constructor(private val context: Context) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    companion object {
        const val CHANNEL_SERVICES = "com.ziichat/services"
        const val CHANNEL_STREAM_OUTS = "com.ziichat/stream/out"
        const val CHANNEL_STREAM_IN = "com.ziichat/stream/in"


        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: FlutterManagerSingleton? = null

        val instance: FlutterManagerSingleton get() = INSTANCE!!

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = FlutterManagerSingleton(context.applicationContext)
                    }
                }
            }
        }
    }

    private val _streamOut = MutableSharedFlow<Any?>()
    val streamOut: SharedFlow<Any?> = _streamOut.asSharedFlow()

    init {
        FlutterEngineUtils.preloadServiceFlutterEngine(context)
    }

    fun start() {
        var servicesEngine = FlutterEngineUtils.preloadServiceFlutterEngine(context)

        val streamOutChannel = BasicMessageChannel<Any>(
            servicesEngine.dartExecutor.binaryMessenger,
            CHANNEL_STREAM_OUTS,
            JSONMessageCodec.INSTANCE
        )

        streamOutChannel.setMessageHandler { message, reply ->
            coroutineScope.launch {
                _streamOut.emit(message)
            }
            reply.reply(null)
        }
    }


    fun makeServicesChannel(): MethodChannel {
        var servicesEngine = FlutterEngineUtils.preloadServiceFlutterEngine(context)

        return MethodChannel(
            servicesEngine.dartExecutor.binaryMessenger,
            CHANNEL_SERVICES
        )
    }

    fun makeStreamInChannel(): BasicMessageChannel<Any> {
        var servicesEngine = FlutterEngineUtils.preloadServiceFlutterEngine(context)

        return BasicMessageChannel(
            servicesEngine.dartExecutor.binaryMessenger,
            CHANNEL_STREAM_IN,
            JSONMessageCodec.INSTANCE
        )
    }
}