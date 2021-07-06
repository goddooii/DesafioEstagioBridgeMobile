import 'dart:convert';

import 'package:audino/api/endpoints.dart';
import 'package:audino/models.dart';
import 'package:http/http.dart' as http;

Future<Track> fetchTrack(String id) async {
  print('fetchTrack $id');
  final response = await http.get(Endpoints.track(id));
  final data = jsonDecode(response.body);
  return Track.fromJson(data as Map<String, dynamic>);
}

Future<List<Track>> fetchTop() async {
  print('fetchTop');
  final response = await http.get(Endpoints.top());
  final data = jsonDecode(response.body);
  return (data as List<dynamic>).map((d) => Track.fromJson(d as Map<String, dynamic>)).toList();
}

Future<List<Track>> fetchFavorites() async {
  print('fetchFavorites');
  final response = await http.get(Endpoints.favorites());
  return _parseResponseTrackList(response);
}

Future<List<Track>> addFavorite(String id) async {
  print('addFavorite $id');
  final response = await http.post(Endpoints.addFavorite(id));
  return _parseResponseTrackList(response);
}

Future<List<Track>> removeFavorite(String id) async {
  print('removeFavorite $id');
  final response = await http.post(Endpoints.removeFavorite(id));
  return _parseResponseTrackList(response);
}

Future<List<Track>> clearFavorites() async {
  print('clearFavorites');
  final response = await http.post(Endpoints.clearFavorites());
  return _parseResponseTrackList(response);
}

List<Track> _parseResponseTrackList(http.Response response) {
  final data = jsonDecode(response.body);
  return (data as List<dynamic>).map((d) => Track.fromJson(d as Map<String, dynamic>)).toList();
}
