package tdt4140.gr1816.app.api.types;

public class User {

  private String id;
  private String username;
  private String password;
  private boolean isDoctor;
  private String gender;
  private int age;

  private boolean isGatheringData = true;

  public User(
      String username,
      String password,
      boolean isDoctor,
      String gender,
      int age,
      boolean isGatheringData) {
    this(null, username, password, isDoctor, gender, age, isGatheringData);
  }

  public User(
      String id,
      String username,
      String password,
      boolean isDoctor,
      String gender,
      int age,
      boolean isGatheringData) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.isDoctor = isDoctor;
    this.gender = gender;
    this.age = age;
    this.isGatheringData = isGatheringData;
  }

  public boolean isGatheringData() {
    return isGatheringData;
  }

  public String getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public boolean isDoctor() {
    return isDoctor;
  }

  public String getGender() {
    return gender;
  }

  public int getAge() {
    return age;
  }

  public boolean isInSameGroup(User user) {
    return user.getAge() == age;
  }
}
