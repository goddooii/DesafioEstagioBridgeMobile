import 'dart:convert';

import 'package:audino/api/endpoints.dart';
import 'package:audino/models.dart';
import 'package:http/http.dart' as http;

Future<List<Track>> fetchSearch(String term) async {
  print('fetchSearch $term');

  if (term == null || term.isEmpty) {
    return [];
  }

  final response = await http.get(Endpoints.search(term));
  final data = jsonDecode(response.body);
  return (data as List<dynamic>).map((d) => Track.fromJson(d as Map<String, dynamic>)).toList();
}
