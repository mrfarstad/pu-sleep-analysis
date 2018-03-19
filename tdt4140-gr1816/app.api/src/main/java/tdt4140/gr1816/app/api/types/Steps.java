package tdt4140.gr1816.app.api.types;

public class Steps {
  private String id;
  private String userId;
  private String date;
  private int steps;

  public Steps(String userId, String date, int steps) {
    this(null, userId, date, steps);
  }

  public Steps(String id, String userId, String date, int steps) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.steps = steps;
  }

  public String getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public String getDate() {
    return date;
  }

  public int getSteps() {
    return steps;
  }
}
