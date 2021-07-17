import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:async';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Foreground Example',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Foreground Example Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = const MethodChannel('example_service');
  bool _serviceIsRunning = false;

  Future<void> _toggleService() async {
    try {
      String methodName =
          !_serviceIsRunning ? 'startExampleService' : 'stopExampleService';
      final result = await platform.invokeMethod(methodName);
      setState(() {
        if ('$result' == 'Started!') {
          _serviceIsRunning = true;
        } else if ('$result' == 'Stopped!') {
          _serviceIsRunning = false;
        }
      });
    } on PlatformException catch (e) {
      print("Failed to invoke method: '${e.message}'.");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: ElevatedButton(
          child: Text(
            !_serviceIsRunning ? 'Start Service' : 'Stop Service',
          ),
          onPressed: _toggleService,
        ),
      ),
    );
  }
}
