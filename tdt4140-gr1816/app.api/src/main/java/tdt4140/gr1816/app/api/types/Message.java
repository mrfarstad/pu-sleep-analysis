package tdt4140.gr1816.app.api.types;

public class Message {

  private final String id;
  private final String fromId;
  private final String toId;
  private final String subject;
  private final String message;

  public Message(String fromId, String toId, String subject, String message) {
    this(null, fromId, toId, subject, message);
  }

  public Message(String id, String fromId, String toId, String subject, String message) {
    super();
    this.id = id;
    this.fromId = fromId;
    this.toId = toId;
    this.subject = subject;
    this.message = message;
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
}
