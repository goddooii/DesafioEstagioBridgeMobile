import 'package:flutter/material.dart';

class TabTitle extends StatelessWidget {
  final String title;
  final Widget action;

  const TabTitle({Key key, this.title, this.action}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(
        top: 24,
        left: 24,
        right: 24,
        bottom: 8,
      ),
      child: ConstrainedBox(
        constraints: BoxConstraints(minHeight: 48),
        child: Row(
          children: <Widget>[
            Expanded(
              child: Text(
                title,
                softWrap: false,
                overflow: TextOverflow.fade,
                style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 32,
                ),
              ),
            ),
            if (action != null) action,
          ],
        ),
      ),
    );
  }
}
