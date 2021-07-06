import 'package:audino/widgets/audino_scaffold.dart';
import 'package:flutter/material.dart';

class AlbumScreen extends StatefulWidget {
  final String albumId;
  final String artistName;

  const AlbumScreen({Key key, this.albumId, this.artistName}) : super(key: key);

  @override
  _AlbumScreenState createState() => _AlbumScreenState();
}

class _AlbumScreenState extends State<AlbumScreen> {
  @override
  Widget build(BuildContext context) {
    return AudinoScaffold(
      body: Center(child: Text('Tela de álbum')),
    );
  }
}
