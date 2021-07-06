import 'dart:async';

import 'package:audino/models.dart';
import 'package:audino/utils/collections.dart';
import 'package:audino/utils/duration.dart';
import 'package:audino/widgets/audino_scaffold.dart';
import 'package:audino/widgets/favorite_button.dart';
import 'package:audino/widgets/image_gradient_background.dart';
import 'package:audino/widgets/track_menu.dart';
import 'package:audioplayers/audioplayers.dart';
import 'package:flutter/material.dart';

enum RepeatMode {
  none,
  repeat,
  repeatOne,
}

class PlayScreen extends StatefulWidget {
  final Track initialTrack;
  final List<Track> tracks;

  const PlayScreen({Key key, this.initialTrack, this.tracks}) : super(key: key);

  @override
  _PlayScreenState createState() => _PlayScreenState();
}

class _PlayScreenState extends State<PlayScreen> {
  final audioPlayer = AudioPlayer();

  Duration position;
  Duration duration;

  RepeatMode repeat = RepeatMode.none;
  bool shuffle = false;

  StreamSubscription<Duration> subPosition;
  StreamSubscription<Duration> subDuration;
  StreamSubscription<AudioPlayerState> subState;

  int currentIndex;

  Track get currentTrack => tracks[currentIndex];

  List<Track> tracks;

  @override
  void initState() {
    super.initState();
    tracks = widget.tracks.toList();

    currentIndex = tracks.indexOf(widget.initialTrack);
    assert(currentIndex != null);

    if (currentTrack.previewUrl != null) {
      audioPlayer.play(currentTrack.previewUrl, isLocal: false);
    }

    subPosition = audioPlayer.onAudioPositionChanged.listen((_position) {
      if (mounted) setState(() => position = _position);
    });
    subDuration = audioPlayer.onDurationChanged.listen((_duration) {
      if (mounted) setState(() => duration = _duration);
    });
    subState = audioPlayer.onPlayerStateChanged.listen((state) {});
    audioPlayer.onPlayerCompletion.listen((event) {
      playNext();
    });
  }

  @override
  void dispose() {
    closePlayer();
    super.dispose();
  }

  void closePlayer() async {
    await subPosition.cancel();
    await subDuration.cancel();
    await subState.cancel();
    await audioPlayer.pause();
    await audioPlayer.stop();
    await Future.delayed(Duration(seconds: 1));
    await audioPlayer.dispose();
  }

  void onPlayPressed() async {
    switch (audioPlayer.state) {
      case AudioPlayerState.COMPLETED:
        playNext();
        await audioPlayer.seek(Duration(seconds: 0));
        break;
      case AudioPlayerState.STOPPED:
      case AudioPlayerState.PAUSED:
        await audioPlayer.resume();
        break;
      case AudioPlayerState.PLAYING:
        await audioPlayer.pause();
        break;
    }
  }

  void playNext() {
    final nextIndex = currentIndex + 1;
    if (nextIndex < tracks.length) {
      setState(() {
        currentIndex = nextIndex;
      });
      audioPlayer.play(currentTrack.previewUrl, isLocal: false);
    }
  }

  void playPrevious() {
    if (currentIndex > 0) {
      setState(() {
        currentIndex--;
      });
      audioPlayer.play(currentTrack.previewUrl, isLocal: false);
    }
  }

  void toggleShuffle() {}

  void toggleRepeat() {
    setState(() {
      repeat = cycleNext(repeat, RepeatMode.values);
    });
  }

  double get positionPercent {
    if (position == null || duration == null) {
      return 0;
    }
    return position.inMilliseconds / duration.inMilliseconds;
  }

  @override
  Widget build(BuildContext context) {
    final imageSize = MediaQuery.of(context).size.width;
    return AudinoScaffold(
      body: SingleChildScrollView(
        child: ImageGradientBackground(
          backgroundUrl: currentTrack.album.image,
          foreground: buildControllers(),
        ),
      ),
    );
  }

  Widget buildControllers() {
    return Column(
      children: <Widget>[
        Row(
          children: <Widget>[
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    currentTrack.name,
                    softWrap: false,
                    overflow: TextOverflow.fade,
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    currentTrack.artistsNames,
                    softWrap: false,
                    overflow: TextOverflow.fade,
                    style: TextStyle(fontSize: 16),
                  ),
                ],
              ),
            ),
            FavoriteButton(track: currentTrack),
            TrackMenu(track: currentTrack),
          ],
        ),
        SizedBox(height: 28),
        Row(
          children: <Widget>[
            Text(
              durationToTimestamp(position),
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w500,
              ),
            ),
            Spacer(),
            Text(
              durationToTimestamp(duration),
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ),
        Slider(
          value: positionPercent,
          onChanged: (value) {
            audioPlayer.seek(duration * value);
            audioPlayer.resume();
          },
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: <Widget>[
            IconButton(
              icon: Icon(Icons.shuffle),
              iconSize: 24,
              onPressed: toggleShuffle,
            ),
            IconButton(
              icon: Icon(Icons.skip_previous),
              iconSize: 48,
              onPressed: playPrevious,
            ),
            StreamBuilder<AudioPlayerState>(
              stream: audioPlayer.onPlayerStateChanged,
              builder: (context, snapshot) {
                return IconButton(
                  icon: Icon(
                    audioPlayer.state == AudioPlayerState.PLAYING
                        ? Icons.pause_circle_filled
                        : Icons.play_circle_filled,
                  ),
                  iconSize: 80,
                  onPressed: onPlayPressed,
                );
              },
            ),
            IconButton(
              icon: Icon(Icons.skip_next),
              iconSize: 48,
              onPressed: playNext,
            ),
            IconButton(
              icon: Icon(Icons.repeat, color: Colors.transparent),
              iconSize: 24,
              onPressed: toggleRepeat,
            ),
          ],
        ),
      ],
    );
  }
}
