import 'package:audino/api/artists.dart';
import 'package:audino/models.dart';
import 'package:audino/screens/routes.dart';
import 'package:audino/widgets/album_carousel.dart';
import 'package:audino/widgets/audino_button.dart';
import 'package:audino/widgets/audino_scaffold.dart';
import 'package:audino/widgets/image_gradient_background.dart';
import 'package:audino/widgets/track_item.dart';
import 'package:flutter/material.dart';

class ArtistScreen extends StatefulWidget {
  final Artist artist;

  const ArtistScreen({Key key, this.artist}) : super(key: key);

  @override
  _ArtistScreenState createState() => _ArtistScreenState();
}

class _ArtistScreenState extends State<ArtistScreen> {
  bool loading = true;
  FullArtist artist;

  @override
  void initState() {
    super.initState();
    load();
  }

  void load() async {
    final _artist = await fetchArtist(widget.artist.id);
    setState(() {
      loading = false;
      artist = _artist;
    });
  }

  void onPressed(BuildContext context, List<Track> tracks) {
    Navigator.push(context, Routes.play(tracks.first, tracks));
  }

  @override
  Widget build(BuildContext context) {
    return AudinoScaffold(
      body: buildBody(),
    );
  }

  Widget buildBody() {
    if (loading) {
      return Center(child: CircularProgressIndicator());
    }

    final enabledTracks =
        artist.topTracks.where((track) => track.previewUrl != null).toList();

    return ListView(
      children: <Widget>[
        ImageGradientBackground(
          backgroundUrl: artist.image,
          foreground: Column(
            children: <Widget>[
              Row(
                children: <Widget>[
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        Text(
                          artist.name,
                          overflow: TextOverflow.fade,
                          style: const TextStyle(
                            fontSize: 32,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        Text(
                          '1.000 seguidores',
                          softWrap: false,
                          overflow: TextOverflow.fade,
                          style: const TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.w500,
                            color: const Color(0xFF7B7B7B),
                          ),
                        ),
                      ],
                    ),
                  ),
                  SizedBox(width: 8),
                  AudinoButton(
                    child: Text('TOCAR'),
                    onPressed: enabledTracks.isNotEmpty
                        ? () => onPressed(context, enabledTracks)
                        : null,
                  )
                ],
              ),
            ],
          ),
        ),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.only(left: 24, top: 8),
              child: Text(
                'Mais ouvidas',
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 24,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            for (final track in artist.topTracks)
              TrackItem(
                track: track,
                tracks: artist.topTracks,
              ),
            if (artist.albums.isNotEmpty) ...[
              Padding(
                padding: EdgeInsets.only(left: 24, top: 40, bottom: 24),
                child: Text(
                  'Discografia',
                  style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                ),
              ),
              AlbumCarousel(
                albums: artist.albums,
                artist: artist,
              ),
            ],
            SizedBox(height: artist.albums.isNotEmpty ? 96 : 16),
          ],
        ),
      ],
    );
  }
}
