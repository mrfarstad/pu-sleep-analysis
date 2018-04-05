package tdt4140.gr1816.app.api.types;

public class Message {

  private final String id;
  private final String fromId;
  private final String toId;
  private final String message;

  public Message(String fromId, String toId, String message) {
    this(null, fromId, toId, message);
  }

  public Message(String id, String fromId, String toId, String message) {
    super();
    this.id = id;
    this.fromId = fromId;
    this.toId = toId;
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

  public String getMessage() {
    return message;
  }
}
