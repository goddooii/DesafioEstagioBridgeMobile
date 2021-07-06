import 'dart:convert';

import 'package:audino/api/endpoints.dart';
import 'package:audino/models.dart';
import 'package:http/http.dart' as http;

Future<FullArtist> fetchArtist(String id) async {
  print('fetchArtist $id');
  final response = await http.get(Endpoints.artist(id));
  final data = jsonDecode(response.body);
  return FullArtist.fromJson(data as Map<String, dynamic>);
}
