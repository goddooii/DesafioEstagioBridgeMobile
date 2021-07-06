import 'dart:convert';

import 'package:audino/api/endpoints.dart';
import 'package:audino/models.dart';
import 'package:http/http.dart' as http;

Future<FullAlbum> fetchAlbum(String id) async {
  print('fetchAlbum $id');
  final response = await http.get(Endpoints.album(id));
  final data = jsonDecode(response.body);
  return FullAlbum.fromJson(data as Map<String, dynamic>);
}
