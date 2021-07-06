import 'package:audino/screens/home/tabs/favorites_tab.dart';
import 'package:audino/screens/home/tabs/search_tab.dart';
import 'package:audino/screens/home/tabs/top_tab.dart';
import 'package:audino/utils/colors.dart';
import 'package:audino/widgets/audino_scaffold.dart';
import 'package:flutter/material.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int tabIndex = 0;

  @override
  Widget build(BuildContext context) {
    return AudinoScaffold(
      body: buildBody(),
      bottomNavigationBar: BottomNavigationBar(
        backgroundColor: AudinoColors.black,
        selectedItemColor: Colors.white,
        currentIndex: tabIndex,
        onTap: (index) {
          setState(() => tabIndex = index);
        },
        items: [
          buildNavBarItem(Icons.home, 'Home'),
          buildNavBarItem(Icons.beach_access, 'Busca'),
          buildNavBarItem(Icons.favorite, 'Favoritos'),
        ],
      ),
    );
  }

  Widget buildBody() {
    return IndexedStack(
      index: tabIndex,
      children: [
        TopTab(),
        SearchTab(),
        FavoritesTab(),
      ],
    );
  }

  BottomNavigationBarItem buildNavBarItem(IconData iconData, String label) {
    return BottomNavigationBarItem(
      icon: Icon(iconData),
      title: Text(label),
    );
  }
}
