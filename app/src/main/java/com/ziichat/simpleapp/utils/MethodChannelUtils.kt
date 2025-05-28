package com.ziichat.simpleapp.utils

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.JSONMessageCodec
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MethodChannelUtils : EventChannel.StreamHandler {

    const val CHANNEL_APP_SERVICES = "com.ziichat/app/services"
    const val CHANNEL_APP_STREAM_OUTS = "com.ziichat/app/stream/out"
    const val CHANNEL_APP_STREAM_IN = "com.ziichat/app/stream/in"

    private var eventSink: EventChannel.EventSink? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun setMethodChannel(engine: FlutterEngine, event: EventChannel.EventSink?) {
        this.eventSink = event
        var appServicesChannel = MethodChannel(
            engine.dartExecutor.binaryMessenger,
            CHANNEL_APP_SERVICES
        )

        var appStreamOutChannel = EventChannel(
            engine.dartExecutor.binaryMessenger,
            CHANNEL_APP_STREAM_OUTS
        )

        var appStreamInChannel = BasicMessageChannel(
            engine.dartExecutor.binaryMessenger,
            CHANNEL_APP_STREAM_IN,
            JSONMessageCodec.INSTANCE
        )

        appStreamOutChannel.setStreamHandler(this)

        servicesChannel.setMethodCallHandler { call, result ->
            appServicesChannel.invokeMethod(
                call.method,
                call.arguments,
                object : MethodChannel.Result {
                    override fun success(invokeResult: Any?) {
                        result.success(invokeResult)
                    }

                    override fun error(
                        errorCode: String,
                        errorMessage: String?,
                        errorDetails: Any?
                    ) {
                        result.error(errorCode, errorMessage, errorDetails)
                    }

                    override fun notImplemented() {
                        result.notImplemented()
                    }
                })
        }

        appServicesChannel.setMethodCallHandler { call, result ->
            servicesChannel.invokeMethod(
                call.method,
                call.arguments,
                object : MethodChannel.Result {
                    override fun success(invokeResult: Any?) {
                        result.success(invokeResult)
                    }

                    override fun error(
                        errorCode: String,
                        errorMessage: String?,
                        errorDetails: Any?
                    ) {
                        result.error(errorCode, errorMessage, errorDetails)
                    }

                    override fun notImplemented() {
                        result.notImplemented()
                    }
                })
        }
        appStreamInChannel.setMessageHandler { message, reply ->
            streamInChannel.send(message)
            reply.reply(null)
        }

        coroutineScope.launch {
            FlutterManagerSingleton.instance.streamOut.collect { message ->
                eventSink?.success(message)
            }
        }
        FlutterManagerSingleton.instance.start()

    }

    override fun onListen(
        arguments: Any?,
        events: EventChannel.EventSink?
    ) {
        this.eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        this.eventSink = null
    }


    private val streamInChannel: BasicMessageChannel<Any> by lazy {
        FlutterManagerSingleton.instance.makeStreamInChannel()
    }

    private val servicesChannel: MethodChannel by lazy {
        FlutterManagerSingleton.instance.makeServicesChannel()
    }

}