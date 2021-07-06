import 'package:audino/business/favorites.dart';
import 'package:audino/screens/home/tab_title.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class FavoritesTab extends StatefulWidget {
  @override
  _FavoritesTabState createState() => _FavoritesTabState();
}

class _FavoritesTabState extends State<FavoritesTab> {
  @override
  Widget build(BuildContext context) {
    final favorites = Provider.of<Favorites>(context);

    return ListView(
      children: <Widget>[
        TabTitle(title: 'Músicas favoritas'),
        if (favorites.loading)
          Center(
            child: CircularProgressIndicator(),
          )
        else if (favorites.favorites.isEmpty)
          Center(
            child: Column(
              children: <Widget>[
                SizedBox(height: 200),
                FractionallySizedBox(
                  widthFactor: 0.7,
                  child: Image.asset('assets/images/empty-favs.png'),
                ),
                SizedBox(height: 24),
                Text(
                    'Suas músicas favoritas estarão listadas aqui! \nAperte no coração para favoritar uma música.',
                    textAlign: TextAlign.center),
              ],
            ),
          )
        else
          Padding(
            padding: EdgeInsets.all(32),
            child: Text('Lista de favoritos.'),
          ),
      ],
    );
  }
}
