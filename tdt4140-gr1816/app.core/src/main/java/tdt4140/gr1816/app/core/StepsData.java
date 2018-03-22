package tdt4140.gr1816.app.core;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StepsData {
  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private User user;

  @JsonProperty("date")
  private LocalDate date;

  @JsonProperty("steps")
  private int steps;

  public StepsData(
      @JsonProperty("id") String id,
      @JsonProperty("user") User user,
      @JsonProperty("date") String date,
      @JsonProperty("duration") int steps) {
    this.id = id;
    this.user = user;
    this.date = LocalDate.parse(date);
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
  public LocalDate getDate() {
    return date;
  }

  @JsonGetter
  public int getSteps() {
    return steps;
  }
}
