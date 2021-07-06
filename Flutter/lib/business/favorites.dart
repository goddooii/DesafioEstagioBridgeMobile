import 'package:audino/api/tracks.dart';
import 'package:audino/models.dart';
import 'package:flutter/widgets.dart';

const _debug = false;

class Favorites extends ChangeNotifier {
  List<Track> _favorites;
  List<Track> _added = [];
  List<Track> _removed = [];
  bool _loading = false;

  bool get loading => _loading;

  List<Track> get favorites {
    return _favorites.where((t) => !_removed.contains(t)).toList();
  }

  Favorites() {
    _load();
  }

  void add(Track track) async {
    _added.add(track);
    notifyListeners();
    _load(await addFavorite(track.id));
    _log();
  }

  void remove(Track track) async {
    _removed.add(track);
    notifyListeners();
    _load(await removeFavorite(track.id));
    _log();
  }

  void clear() async {
    _removed.addAll(_favorites);
    notifyListeners();
    _load(await clearFavorites());
  }

  bool contains(Track track) {
    return _favorites != null &&
        (_favorites.contains(track) || _added.contains(track)) &&
        !_removed.contains(track);
  }

  bool editable(Track track) {
    return !_added.contains(track) && !_removed.contains(track);
  }

  bool clearable() {
    return _added.isEmpty && _removed.isEmpty;
  }

  void _load([List<Track> tracks]) async {
    if (tracks == null) {
      if (_favorites == null) {
        _loading = true;
        notifyListeners();
      }

      _favorites = await fetchFavorites();
      _loading = false;
    } else {
      _favorites = tracks;
    }

    _added.removeWhere((t) => _favorites.contains(t));
    _removed.removeWhere((t) => !_favorites.contains(t));
    notifyListeners();
    _log();
  }

  void _log() {
    if (_debug) {
      print('$runtimeType(${{
        'favorites': _favorites,
        'added': _added,
        'removed': _removed,
      }})');
    }
  }
}
