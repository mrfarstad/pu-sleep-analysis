package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UserCollection {

  @JsonProperty("allUsers")
  List<User> users;

  @JsonCreator
  public UserCollection(@JsonProperty("allUsers") List<User> users) {
    this.users = users;
  }
}
