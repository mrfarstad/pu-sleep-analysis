package tdt4140.gr1816.app.api.types;

import java.util.List;

public interface Character {
  String getId();

  String getName();

  List<Character> getFriends();

  List<Episode> getAppearsIn();
}
