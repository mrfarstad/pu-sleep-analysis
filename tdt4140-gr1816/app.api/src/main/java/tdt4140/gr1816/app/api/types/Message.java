package tdt4140.gr1816.app.api.types;

public class Message {

  private final String id;
  private final String fromId;
  private final String toId;
  private final String subject;
  private final String message;
  private final String date;

  public Message(String fromId, String toId, String subject, String message, String date) {
    this(null, fromId, toId, subject, message, date);
  }

  public Message(
      String id, String fromId, String toId, String subject, String message, String date) {
    super();
    this.id = id;
    this.fromId = fromId;
    this.toId = toId;
    this.subject = subject;
    this.message = message;
    this.date = date;
  }

  public String getId() {
    return id;
  }

  public String getFromId() {
    return fromId;
  }

  public String getToId() {
    return toId;
  }

  public String getSubject() {
    return subject;
  }

  public String getMessage() {
    return message;
  }

  public String getDate() {
    return date;
  }
}
