import 'package:audino/business/favorites.dart';
import 'package:audino/screens/home/home_screen.dart';
import 'package:audino/utils/colors.dart';
import 'package:audino/utils/slider_track_shape.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:provider/provider.dart';

void main() async {
  runApp(App());
}

class App extends StatefulWidget {
  @override
  _AppState createState() => _AppState();
}

class _AppState extends State<App> {
  @override
  void initState() {
    super.initState();
    FocusScope.of(context).unfocus();
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => Favorites(),
      lazy: false,
      child: MaterialApp(
        debugShowCheckedModeBanner: false,
        title: 'Audino',
        theme: ThemeData(
          primarySwatch: Colors.blue,
          accentColor: Colors.blue,
          backgroundColor: AudinoColors.black,
          brightness: Brightness.dark,
          fontFamily: 'Inter',
          sliderTheme: SliderThemeData(
            trackShape: NoPaddingTrackShape(),
            trackHeight: 4,
            thumbShape: RoundSliderThumbShape(enabledThumbRadius: 5),
          ),
        ),
        home: HomeScreen(),
      ),
    );
  }
}
