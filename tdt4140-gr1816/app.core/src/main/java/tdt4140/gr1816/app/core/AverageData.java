package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AverageData {

  @JsonProperty("sleepDuration")
  private int sleepDuration;

  @JsonProperty("steps")
  private int steps;

  @JsonProperty("sleepEfficiency")
  private int sleepEfficiency;

  @JsonProperty("restHr")
  private int restHr;

  @JsonProperty("ageGroup")
  private String ageGroup;

  public AverageData(
      @JsonProperty("sleepDuration") int sleepDuration,
      @JsonProperty("steps") int steps,
      @JsonProperty("sleepEfficiency") int sleepEfficiency,
      @JsonProperty("restHr") int restHr,
      @JsonProperty("ageGroup") String ageGroup) {
    this.restHr = restHr;
    this.sleepDuration = sleepDuration;
    this.sleepEfficiency = sleepEfficiency;
    this.steps = steps;
    this.ageGroup = ageGroup;
  }

  @JsonGetter
  public int getRestHr() {
    return restHr;
  }

  @JsonGetter
  public int getSleepDuration() {
    return sleepDuration;
  }

  @JsonGetter
  public int getSleepEfficiency() {
    return sleepEfficiency;
  }

  @JsonGetter
  public int getSteps() {
    return steps;
  }

  @JsonGetter
  public String getAgeGroup() {
    return ageGroup;
  }
}
