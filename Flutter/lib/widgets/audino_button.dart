import 'package:audino/utils/colors.dart';
import 'package:flutter/material.dart';

class AudinoButton extends StatelessWidget {
  final VoidCallback onPressed;
  final Widget child;

  const AudinoButton({
    Key key,
    @required this.onPressed,
    @required this.child,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return RaisedButton(
      disabledColor: AudinoColors.black80,
      shape: StadiumBorder(),
      onPressed: onPressed,
      child: child,
    );
  }
}
