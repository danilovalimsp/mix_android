package com.example.mixandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {
    private lateinit var flutterEngine: FlutterEngine
    private val CHANNEL = "mix-channel"
    lateinit var channelM : MethodChannel
    var route = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flutterEngine = FlutterEngine(this)
        FlutterEngineCache
            .getInstance()
            .put("my_engine_id", flutterEngine)

        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        channelM = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        channelM.setMethodCallHandler {
                call, result ->
            if(call.method == "getRoute"){
                result.success(route)
            } else if(call.method == "getVMURL"){
                result.success("vm url")
            }
        }

        val btn01: Button = findViewById(R.id.btn01)
        btn01.setOnClickListener(View.OnClickListener() {
            route = "/page01"
            openFlutterActivity()
        });

        val btn02: Button = findViewById(R.id.btn02)
        btn02.setOnClickListener(View.OnClickListener() {
            route = "/page02"
            openFlutterActivity()
        });
    }

    fun openFlutterActivity(){
        channelM.invokeMethod("setRoute", route)

        startActivity(FlutterActivity
            .withCachedEngine("my_engine_id")
            .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.transparent)
            .build(this))
    }
}