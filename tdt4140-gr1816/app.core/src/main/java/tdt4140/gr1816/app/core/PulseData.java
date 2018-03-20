package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PulseData {
  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private User user;

  @JsonProperty("date")
  private String date;

  @JsonProperty("maxHr")
  private int maxHr;

  @JsonProperty("minHr")
  private int minHr;

  public PulseData(
      @JsonProperty("id") String id,
      @JsonProperty("user") User user,
      @JsonProperty("date") String date,
      @JsonProperty("maxHr") int maxHr,
      @JsonProperty("minHr") int minHr) {
    this.id = id;
    this.user = user;
    this.date = date;
    this.maxHr = maxHr;
    this.minHr = minHr;
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
  public int getMaxHr() {
    return maxHr;
  }

  @JsonGetter
  public int getMinHr() {
    return minHr;
  }
}
