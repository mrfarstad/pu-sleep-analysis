package tdt4140.gr1816.app.api.types;

public class PulseData {
  private String id;
  private String userId;
  private String date;
  private int maxHr;

  public PulseData(String userId, String date, int maxHr, int minHr) {
    this(null, userId, date, maxHr, minHr);
  }

  public PulseData(String id, String userId, String date, int maxHr, int minHr) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.maxHr = maxHr;
    this.minHr = minHr;
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

  public int getMaxHr() {
    return maxHr;
  }

  public int getMinHr() {
    return minHr;
  }

  private int minHr;
}
