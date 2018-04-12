package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.Message;

public class MessageRepository {

  private final MongoCollection<Document> messages;

  public MessageRepository(MongoCollection<Document> messages) {
    this.messages = messages;
  }

  public Message findById(String id) {
    Document doc = messages.find(eq("_id", new ObjectId(id))).first();
    return message(doc);
  }

  public List<Message> getAllMessages(String userId, String field) {
    List<Message> messagesForField = new ArrayList<>();
    for (Document doc : messages.find(eq(field, userId))) {
      messagesForField.add(message(doc));
    }
    return messagesForField;
  }

  public List<Message> getAllMessagesToUser(String userId) {
    return getAllMessages(userId, "toId");
  }

  public List<Message> getAllMessagesByUser(String userId) {
    return getAllMessages(userId, "fromId");
  }

  public Message createMessage(Message message) {
    Document doc = new Document();

    doc.append("fromId", message.getFromId());
    doc.append("toId", message.getToId());
    doc.append("subject", message.getSubject());
    doc.append("message", message.getMessage());
    doc.append("date", message.getDate());

    messages.insertOne(doc);
    return message(doc);
  }

  private Message message(Document doc) {
    return new Message(
        doc.get("_id").toString(),
        doc.getString("fromId"),
        doc.getString("toId"),
        doc.getString("subject"),
        doc.getString("message"),
        doc.getString("date"));
  }
}
