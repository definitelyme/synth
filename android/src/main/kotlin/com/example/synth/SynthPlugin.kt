package com.example.synth

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import com.example.synth.Synth

/** SynthPlugin */
class SynthPlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private final var channelName = "synth"
    private lateinit var channel: MethodChannel
    private lateinit var synth: Synth

    fun setup(plugin: SynthPlugin, flutterPluginBinding: FlutterPlugin.FlutterPluginBinding): Unit {
        plugin.channel = MethodChannel(flutterPluginBinding.binaryMessenger, channelName)
        plugin.channel.setMethodCallHandler(plugin)
        plugin.synth = Synth()
        plugin.synth.start()
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        setup(this, flutterPluginBinding);
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android version ${android.os.Build.VERSION.RELEASE}")
            "keyDown" -> {
                try {
                    val arguments = call.arguments as ArrayList<*>
                    val numKeysDown = synth.keyDown(arguments.get(0) as Int)
                    result.success(numKeysDown)
                } catch (ex: Exception) {
                    result.error("1", ex.message, ex.getStackTrace())
                }
            }
            "keyUp" -> {
                try {
                    val arguments = call.arguments as ArrayList<*>
                    val numKeysUp = synth.keyUp(arguments.get(0) as Int)
                    result.success(numKeysUp)
                } catch (ex: Exception) {
                    result.error("1", ex.message, ex.getStackTrace())
                }
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
