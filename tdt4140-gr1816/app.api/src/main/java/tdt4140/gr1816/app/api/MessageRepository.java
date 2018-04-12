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

  public List<Message> getAllMessagesToUser(String userId) {
    List<Message> messagesToUser = new ArrayList<>();
    for (Document doc : messages.find(eq("toId", userId))) {
      messagesToUser.add(message(doc));
    }
    return messagesToUser;
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
