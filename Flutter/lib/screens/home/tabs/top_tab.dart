import 'package:audino/api/tracks.dart';
import 'package:audino/models.dart';
import 'package:audino/screens/home/tab_title.dart';
import 'package:audino/widgets/track_item.dart';
import 'package:flutter/material.dart';

class TopTab extends StatefulWidget {
  @override
  _TopTabState createState() => _TopTabState();
}

class _TopTabState extends State<TopTab> {
  List<Track> top;

  @override
  void initState() {
    super.initState();
    _load();
  }

  void _load() async {
    final _top = await fetchTop();

    setState(() {
      top = _top;
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: <Widget>[
        TabTitle(title: 'Lançamentos'),
        if (top != null)
          for (final track in top)
            TrackItem(
              track: track,
              tracks: top,
            ),
      ],
    );
  }
}
