import 'package:audino/business/favorites.dart';
import 'package:audino/models.dart';
import 'package:audino/utils/colors.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class FavoriteButton extends StatelessWidget {
  final Track track;
  final bool enabled;
  final Color activeColor;

  const FavoriteButton({
    Key key,
    this.track,
    this.enabled = true,
    this.activeColor = AudinoColors.blue,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final favorites = Provider.of<Favorites>(context);
    final liked = favorites.contains(track);

    return IconButton(
      onPressed: enabled
          ? () {
              if (favorites.editable(track)) {
                if (favorites.contains(track)) {
                  favorites.remove(track);
                } else {
                  favorites.add(track);
                }
              }
            }
          : null,
      icon: AnimatedSwitcher(
        duration: Duration(milliseconds: 200),
        child: _buildChild(context, liked, !favorites.editable(track)),
      ),
    );
  }

  Widget _buildChild(BuildContext context, bool liked, bool loading) {
    if (loading) {
      return SizedBox(
        width: 20,
        height: 20,
        child: CircularProgressIndicator(strokeWidth: 3),
      );
    }

    if (liked) {
      return Image.asset(
        'assets/icons/heart_filled.png',
        color: enabled ? activeColor : AudinoColors.disabledColor,
        width: 24,
        height: 24,
        key: Key('liked'),
      );
    }

    return Image.asset(
      'assets/icons/heart_outline.png',
      color: enabled ? Colors.white : AudinoColors.disabledColor,
      width: 24,
      height: 24,
      key: Key('not_liked'),
    );
  }
}
