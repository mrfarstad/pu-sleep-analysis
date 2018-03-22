package tdt4140.gr1816.app.core;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SleepData {
  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private User user;

  @JsonProperty("date")
  private LocalDate date;

  @JsonProperty("duration")
  private int duration;

  @JsonProperty("efficiency")
  private int efficiency;

  public SleepData(
      @JsonProperty("id") String id,
      @JsonProperty("user") User user,
      @JsonProperty("date") String date,
      @JsonProperty("duration") int duration,
      @JsonProperty("efficiency") int efficiency) {
    this.id = id;
    this.user = user;
    this.date = LocalDate.parse(date);
    this.duration = duration;
    this.efficiency = efficiency;
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
  public int getDuration() {
    return duration;
  }

  @JsonGetter
  public int getEfficiency() {
    return efficiency;
  }
}
