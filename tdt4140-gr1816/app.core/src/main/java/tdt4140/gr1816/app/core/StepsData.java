package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StepsData {
  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private User user;

  @JsonProperty("date")
  private String date;

  @JsonProperty("steps")
  private int steps;

  public StepsData(
      @JsonProperty("id") String id,
      @JsonProperty("user") User user,
      @JsonProperty("date") String date,
      @JsonProperty("duration") int steps) {
    this.id = id;
    this.user = user;
    this.date = date;
    this.steps = steps;
  }

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public User getUser() {
    return user;
  }

  @JsonGetter
  public String getDate() {
    return date;
  }

  @JsonGetter
  public int getSteps() {
    return steps;
  }
}
