T cycleNext<T>(T element, List<T> list) {
  final nextIndex = (list.indexOf(element) + 1) % list.length;
  return list[nextIndex];
}
