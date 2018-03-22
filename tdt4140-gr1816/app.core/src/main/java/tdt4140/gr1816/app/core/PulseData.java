package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class PulseData {
  @JsonProperty("id")
  private String id;

  @JsonProperty("user")
  private User user;

  @JsonProperty("date")
  private LocalDate date;

  @JsonProperty("restHr")
  private int restHr;

  public PulseData(
      @JsonProperty("id") String id,
      @JsonProperty("user") User user,
      @JsonProperty("date") String date,
      @JsonProperty("restHr") int restHr) {
    this.id = id;
    this.user = user;
    this.date = LocalDate.parse(date);
    this.restHr = restHr;
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
  public int getRestHr() {
    return restHr;
  }
}
