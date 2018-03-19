package tdt4140.gr1816.app.api.types;

public class Sleep {
  private String id;
  private String date;
  private int duration;
  private int efficiency;
  private String userId;

  public Sleep(String userId, String date, int duration, int efficiency) {
    this(null, userId, date, duration, efficiency);
  }

  public Sleep(String id, String userId, String date, int duration, int efficiency) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.duration = duration;
    this.efficiency = efficiency;
  }

  public String getId() {
    return id;
  }

  public String getDate() {
    return date;
  }

  public int getDuration() {
    return duration;
  }

  public int getEfficiency() {
    return efficiency;
  }

  public String getUserId() {
    return userId;
  }
}
