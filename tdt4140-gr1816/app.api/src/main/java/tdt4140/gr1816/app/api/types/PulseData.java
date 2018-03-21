package tdt4140.gr1816.app.api.types;

public class PulseData {
  private String id;
  private String userId;
  private String date;
  private int restHr;

  public PulseData(String userId, String date, int restHr) {
    this(null, userId, date, restHr);
  }

  public PulseData(String id, String userId, String date, int restHr) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.restHr = restHr;
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

  public int getRestHr() {
    return restHr;
  }
}
