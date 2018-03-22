package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

  @JsonProperty("id")
  private String id;

  @JsonProperty("username")
  private String username;

  @JsonProperty("password")
  private String password;

  @JsonProperty("isDoctor")
  private boolean isDoctor;

  @JsonProperty("gender")
  private String gender;

  @JsonProperty("age")
  private int age;

  @JsonProperty("isGatheringData")
  private boolean isGatheringData;

  @JsonCreator
  public User(
      @JsonProperty("id") String id,
      @JsonProperty("username") String username,
      @JsonProperty("password") String password,
      @JsonProperty("isDoctor") boolean isDoctor,
      @JsonProperty("gender") String gender,
      @JsonProperty("age") int age,
      @JsonProperty("isGatheringData") boolean isGatheringData) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.isDoctor = isDoctor;
    this.gender = gender;
    this.age = age;
    this.isGatheringData = isGatheringData;
  }

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public String getUsername() {
    return username;
  }

  @JsonGetter
  public String getPassword() {
    return password;
  }

  @JsonGetter
  public boolean isDoctor() {
    return isDoctor;
  }

  @JsonGetter
  public String getGender() {
    return gender;
  }

  @JsonGetter
  public int getAge() {
    return age;
  }

  @JsonGetter
  public boolean getIsGatheringData() {
    return this.isGatheringData;
  }

  public void setIsGatheringData(boolean isGatheringData) {
    this.isGatheringData = isGatheringData;
  }

  public String toString() {
    return getUsername();
  }
}
