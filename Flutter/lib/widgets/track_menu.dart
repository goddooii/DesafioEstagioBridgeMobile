import 'package:audino/models.dart';
import 'package:audino/screens/routes.dart';
import 'package:audino/utils/colors.dart';
import 'package:flutter/material.dart';

const _kAlbumSentinel = 'album';

class TrackMenu extends StatelessWidget {
  final Track track;

  const TrackMenu({Key key, this.track}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return PopupMenuButton(
      onSelected: (id) {
        if (id == _kAlbumSentinel) {
          Navigator.push(context, Routes.album(albumId: track.album.id, artistName: track.artists.first.id));
        } else {
          final artist = track.artists.firstWhere((a) => a.id == id);
          Navigator.push(context, Routes.artist(artist: artist));
        }
      },
      color: Colors.white,
      icon: Icon(Icons.more_vert),
      itemBuilder: (BuildContext context) {
        return [
          PopupMenuItem(
            value: _kAlbumSentinel,
            child: Text(
              'Ver álbum',
              style: const TextStyle(color: AudinoColors.darkGrey),
            ),
          ),
          for (final artist in track.artists)
            PopupMenuItem(
              value: artist.id,
              child: Text(
                'Ver ${artist.name}',
                style: const TextStyle(color: AudinoColors.darkGrey),
              ),
            ),
        ];
      },
    );
  }
}
