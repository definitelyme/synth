import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:synth/synth.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await SystemChrome.setPreferredOrientations([DeviceOrientation.landscapeLeft, DeviceOrientation.landscapeRight]);

  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await Synth.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void _onKeyDown(int key) async => await Synth.onKeyDown(key);

  void _onKeyUp(int key) async => await Synth.onKeyUp(key);

  Widget _makeKey({@required _KeyType keyType, @required int key}) {
    return AnimatedContainer(
      height: 200,
      width: 44,
      duration: const Duration(seconds: 2),
      curve: Curves.easeIn,
      child: Material(
        color: keyType == _KeyType.white ? Colors.white : Color.fromARGB(255, 60, 60, 80),
        child: InkWell(
          onTap: () => _onKeyUp(key),
          onTapDown: (details) => _onKeyDown(key),
          onTapCancel: () => _onKeyUp(key),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        backgroundColor: Color.fromARGB(255, 250, 30, 0),
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              Text('Running on: $_platformVersion\n'),
              Flexible(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    _makeKey(keyType: _KeyType.white, key: 60),
                    _makeKey(keyType: _KeyType.black, key: 61),
                    _makeKey(keyType: _KeyType.white, key: 62),
                    _makeKey(keyType: _KeyType.black, key: 63),
                    _makeKey(keyType: _KeyType.white, key: 64),
                    _makeKey(keyType: _KeyType.white, key: 65),
                    _makeKey(keyType: _KeyType.black, key: 66),
                    _makeKey(keyType: _KeyType.white, key: 67),
                    _makeKey(keyType: _KeyType.black, key: 68),
                    _makeKey(keyType: _KeyType.white, key: 69),
                    _makeKey(keyType: _KeyType.black, key: 70),
                    _makeKey(keyType: _KeyType.white, key: 71),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

enum _KeyType { black, white }
