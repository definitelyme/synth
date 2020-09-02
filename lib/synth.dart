import 'dart:async';

import 'package:flutter/services.dart';

class Synth {
  static const MethodChannel _channel = const MethodChannel('synth');

  /// Returns the running Platform Version
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<int> onKeyDown(int key) async {
    final int numNotesOn = await _channel.invokeMethod('keyDown', [key]);
    return numNotesOn;
  }

  static Future<int> onKeyUp(int key) async {
    final int numNotesOn = await _channel.invokeMethod('keyUp', [key]);
    return numNotesOn;
  }
}
