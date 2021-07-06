class Track {
  final String id;
  final String name;
  final String previewUrl;
  final int durationSeconds;

  final Album album;
  final List<Artist> artists;

  String get artistsNames => artists.map((a) => a.name).join(', ');

  Track({
    this.id,
    this.name,
    this.previewUrl,
    this.durationSeconds,
    this.album,
    this.artists,
  });

  factory Track.fromJson(Map<String, dynamic> data) {
    return Track(
      id: data['id'] as String,
      name: data['name'] as String,
      previewUrl: data['previewUrl'] as String,
      durationSeconds: data['durationSeconds'] as int,
      artists: (data['artists'] as List<dynamic>)
          .map((a) => Artist.fromJson(a as Map<String, dynamic>))
          .toList(),
      album: Album.fromJson(data['album'] as Map<String, dynamic>),
    );
  }

  @override
  bool operator ==(other) {
    return identical(this, other) ||
        (runtimeType == other.runtimeType && id == other.id);
  }

  @override
  int get hashCode => id.hashCode;

  @override
  String toString() {
    return name;
  }
}

class Artist {
  final String id;
  final String name;

  Artist({
    this.id,
    this.name,
  });

  factory Artist.fromJson(Map<String, dynamic> data) {
    return Artist(
      id: data['id'] as String,
      name: data['name'] as String,
    );
  }
}

class FullArtist extends Artist {
  final String image;
  final List<FullAlbum> albums;
  final List<Track> topTracks;

  FullArtist({
    String id,
    String name,
    this.image,
    this.albums,
    this.topTracks,
  }) : super(id: id, name: name);

  factory FullArtist.fromJson(Map<String, dynamic> data) {
    return FullArtist(
      id: data['id'] as String,
      name: data['name'] as String,
      image: data['image'] as String,
      albums: (data['albums'] as List)
          .map((d) => FullAlbum.fromJson(d as Map<String, dynamic>))
          .toList(),
      topTracks: (data['top_tracks'] as List)
          .map((d) => Track.fromJson(d as Map<String, dynamic>))
          .toList(),
    );
  }
}

class Album {
  final String id;
  final String name;
  final String image;

  Album({this.id, this.name, this.image});

  factory Album.fromJson(Map<String, dynamic> data) {
    return Album(
      id: data['id'] as String,
      name: data['name'] as String,
      image: data['image'] as String,
    );
  }
}

class FullAlbum extends Album {
  final List<Track> tracks;

  FullAlbum({
    String id,
    String name,
    String image,
    this.tracks,
  }) : super(
          id: id,
          name: name,
          image: image,
        );

  factory FullAlbum.fromJson(Map<String, dynamic> data) {
    return FullAlbum(
      id: data['id'] as String,
      name: data['name'] as String,
      image: data['image'] as String,
      tracks: (data['tracks'] as List)
          .map((d) => Track.fromJson(d as Map<String, dynamic>))
          .toList(),
    );
  }
}
