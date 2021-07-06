import 'package:audino/utils/colors.dart';
import 'package:flutter/material.dart';

class AudinoScaffold extends StatelessWidget {
  final Widget body;
  final Widget bottomNavigationBar;

  const AudinoScaffold({Key key, this.body, this.bottomNavigationBar})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    final parentRoute = ModalRoute.of(context);
    final canPop = parentRoute?.canPop ?? false;

    return Scaffold(
      backgroundColor: AudinoColors.black,
      body: SafeArea(
        child: Stack(
          children: <Widget>[
            body,
            if (canPop)
              _BackButton(
                onPress: () {
                  Navigator.pop(context);
                },
              ),
          ],
        ),
      ),
      bottomNavigationBar: bottomNavigationBar,
    );
  }
}

class _BackButton extends StatelessWidget {
  final VoidCallback onPress;

  const _BackButton({Key key, this.onPress}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: Alignment.topLeft,
      child: Padding(
        padding: const EdgeInsets.only(top: 20, left: 20),
        child: Material(
          color: const Color(0x70FFFFFF),
          borderRadius: BorderRadius.circular(24),
          child: InkWell(
            borderRadius: BorderRadius.circular(24),
            onTap: onPress,
            child: Image.asset(
              'assets/icons/back_arrow.png',
              width: 48,
              height: 48,
            ),
          ),
        ),
      ),
    );
  }
}
