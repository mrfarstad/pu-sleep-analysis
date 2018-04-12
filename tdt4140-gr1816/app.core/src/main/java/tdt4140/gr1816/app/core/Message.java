package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
  @JsonProperty("id")
  private final String id;

  @JsonProperty("from")
  private final User from;

  @JsonProperty("to")
  private final User to;

  @JsonProperty("subject")
  private final String subject;

  @JsonProperty("message")
  private final String message;

  @JsonProperty("date")
  private final LocalDateTime date;

  @JsonCreator
  public Message(
      @JsonProperty("id") String id,
      @JsonProperty("from") User from,
      @JsonProperty("to") User to,
      @JsonProperty("subject") String subject,
      @JsonProperty("message") String message,
      @JsonProperty("date") String date) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.subject = subject;
    this.message = message;
    this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
  }

  public Message(String fromId, String toId, String subject, String message, String date) {
    this(
        null,
        UserDataFetch.userDataFetch.getUserById(fromId),
        UserDataFetch.userDataFetch.getUserById(toId),
        subject,
        message,
        date);
  }

  @JsonGetter
  public String getId() {
    return id;
  }

  @JsonGetter
  public User getFrom() {
    return from;
  }

  @JsonGetter
  public User getTo() {
    return to;
  }

  @JsonGetter
  public String getSubject() {
    return subject;
  }

  @JsonGetter
  public String getMessage() {
    return message;
  }

  @JsonGetter
  public LocalDateTime getDate() {
    return date;
  }

  @Override
  public String toString() {
    return "From: "
        + this.from.getUsername()
        + "\nTo: "
        + this.to.getUsername()
        + "\nSubject: "
        + this.subject
        + "\n-------------------";
  }
}
