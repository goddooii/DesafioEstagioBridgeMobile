import 'package:audino/api/search.dart';
import 'package:audino/models.dart';
import 'package:audino/screens/home/tab_title.dart';
import 'package:audino/utils/colors.dart';
import 'package:audino/utils/debouncer.dart';
import 'package:audino/widgets/track_item.dart';
import 'package:flutter/material.dart';

class SearchTab extends StatefulWidget {
  @override
  _SearchTabState createState() => _SearchTabState();
}

class _SearchTabState extends State<SearchTab> {
  Debouncer<String> _searchDebouncer;
  List<Track> _results;
  bool _loading = false;
  String lastTerm = '';

  ScrollController _scrollController;
  TextEditingController _textController;

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    _scrollController.addListener(_onScrollChanged);

    _textController = TextEditingController();
    _textController.addListener(_onSearchTermChanged);

    _searchDebouncer = Debouncer<String>(
      Duration(milliseconds: 500),
      _performSearch,
    );
  }

  @override
  void dispose() {
    _scrollController.dispose();
    _textController.dispose();
    _searchDebouncer.dispose();
    super.dispose();
  }

  void _onScrollChanged() {
    if (_scrollController.offset > 150) {
      FocusScope.of(context).unfocus();
    }
  }

  void _onSearchTermChanged() {
    final term = _textController.value.text;
    setState(() => _loading = false);
    _searchDebouncer.call(term);
  }

  void _performSearch(String term) async {
    if (term == lastTerm) return;

    lastTerm = term;

    setState(() => _loading = true);

    try {
      _results = await fetchSearch(term);
    } catch (e) {
      print(e);
    } finally {
      setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
      controller: _scrollController,
      children: <Widget>[
        TabTitle(title: 'Pesquisar'),
        SizedBox(height: 8),
        SearchInput(controller: _textController),
        SizedBox(height: 24),
        if (_loading)
          Center(
            child: CircularProgressIndicator(),
          )
        else if (_results == null || _results.isEmpty)
          Center(
            child: Column(
              children: <Widget>[
                SizedBox(height: 130),
                FractionallySizedBox(
                  widthFactor: 0.7,
                  child: Image.asset('assets/images/empty-search.png'),
                ),
                SizedBox(height: 24),
                Text(
                    _results == null
                        ? 'Pesquise musicas e artistas! \nBasta clicar no campo de busca.'
                        : 'Nenhum resultado encontrado.',
                    textAlign: TextAlign.center),
              ],
            ),
          )
        else
          for (final track in _results)
            TrackItem(track: track, tracks: _results),
      ],
    );
  }
}

class SearchInput extends StatelessWidget {
  final TextEditingController controller;

  const SearchInput({Key key, this.controller}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 24),
      child: Container(
        decoration: ShapeDecoration(
          color: Colors.white,
          shape: StadiumBorder(
            side: BorderSide(color: Colors.white),
          ),
        ),
        child: Padding(
          padding: const EdgeInsets.symmetric(vertical: 4, horizontal: 4),
          child: TextField(
            controller: controller,
            decoration: InputDecoration(
              hintText: 'Procure músicas…',
              hintStyle: TextStyle(
                fontSize: 16,
                color: AudinoColors.disabledColor,
              ),
              border: InputBorder.none,
              prefixIcon: Icon(
                Icons.search,
                color: AudinoColors.mediumGrey,
              ),
            ),
            style: TextStyle(fontSize: 16, color: Colors.black),
          ),
        ),
      ),
    );
  }
}
