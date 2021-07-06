import 'package:audino/models.dart';
import 'package:audino/screens/album/album_screen.dart';
import 'package:audino/screens/artist/artist_screen.dart';
import 'package:audino/screens/play/play_screen.dart';
import 'package:flutter/material.dart';

class Routes {
  static MaterialPageRoute<void> play(Track initialTrack, List<Track> tracks) {
    final _tracks = tracks.where((t) => t.previewUrl != null).toList();

    return MaterialPageRoute(
      builder: (BuildContext context) =>
          PlayScreen(initialTrack: initialTrack, tracks: _tracks),
    );
  }

  static MaterialPageRoute<void> artist({@required Artist artist}) {
    return MaterialPageRoute(
      builder: (BuildContext context) => ArtistScreen(artist: artist),
    );
  }

  static MaterialPageRoute<void> album({@required String albumId, String artistName}) {
    return MaterialPageRoute(
      builder: (BuildContext context) => AlbumScreen(albumId: albumId, artistName: artistName),
    );
  }
}
