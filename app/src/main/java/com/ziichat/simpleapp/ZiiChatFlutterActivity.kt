package com.ziichat.simpleapp

import android.content.Context
import com.ziichat.simpleapp.utils.FlutterEngineUtils
import com.ziichat.simpleapp.utils.FlutterEngineUtils.ENGINE_MAIN
import com.ziichat.simpleapp.utils.MethodChannelUtils
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.plugin.common.EventChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class ZiiChatFlutterActivity : FlutterActivity() {

    private var eventSink: EventChannel.EventSink? = null

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return run {
            val engineGroup = FlutterEngineUtils.getEngineGroup(context)
            val dartEntrypoint = DartEntrypoint(
                FlutterInjector.instance().flutterLoader().findAppBundlePath(),
                ENGINE_MAIN
            )
            engineGroup.createAndRunEngine(context, dartEntrypoint).also {
                GeneratedPluginRegistrant.registerWith(it)
                MethodChannelUtils.setMethodChannel(it, eventSink)
                FlutterEngineUtils.storeEngine(ENGINE_MAIN, it)

            }
        }
    }

}
