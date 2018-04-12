package tdt4140.gr1816.app.api.types;

public class AverageData {
  private int restHr;
  private int sleepDuration;
  private int sleepEfficiency;
  private int steps;
  private String ageGroup;

  public int getRestHr() {
    return restHr;
  }

  public int getSleepDuration() {
    return sleepDuration;
  }

  public int getSleepEfficiency() {
    return sleepEfficiency;
  }

  public int getSteps() {
    return steps;
  }

  public String getAgeGroup() {
    return ageGroup;
  }

  public AverageData(
      int restHr, int sleepDuration, int sleepEfficiency, int steps, String ageGroup) {
    this.restHr = restHr;
    this.sleepDuration = sleepDuration;
    this.sleepEfficiency = sleepEfficiency;
    this.steps = steps;
    this.ageGroup = ageGroup;
  }
}
