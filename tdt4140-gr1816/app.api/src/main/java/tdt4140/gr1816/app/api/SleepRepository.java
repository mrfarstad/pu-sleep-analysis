package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.Sleep;

public class SleepRepository {

  private final MongoCollection<Document> sleeps;

  public SleepRepository(MongoCollection<Document> sleeps) {
    this.sleeps = sleeps;
  }

  public Sleep findById(String id) {
    Document doc = sleeps.find(eq("_id", new ObjectId(id))).first();
    return sleep(doc);
  }

  public List<Sleep> getAllSleeps() {
    List<Sleep> allSleeps = new ArrayList<>();
    for (Document doc : sleeps.find()) {
      allSleeps.add(sleep(doc));
    }
    return allSleeps;
  }

  public Sleep saveSleep(Sleep sleep) {
    Document doc = new Document();
    doc.append("user", sleep.getUserId());
    doc.append("date", sleep.getDate());
    doc.append("duration", sleep.getDuration());
    doc.append("efficiency", sleep.getEfficiency());
    sleeps.insertOne(doc);
    return new Sleep(
        doc.get("_id").toString(),
        sleep.getUserId(),
        sleep.getDate(),
        sleep.getDuration(),
        sleep.getEfficiency());
  }

  public boolean deleteSleep(Sleep sleep) {
    Document doc = sleeps.find(eq("_id", new ObjectId(sleep.getId()))).first();
    DeleteResult result = sleeps.deleteOne(doc);
    return result.wasAcknowledged();
  }

  private Sleep sleep(Document doc) {
    if (doc == null) {
      return null;
    }
    return new Sleep(
        doc.get("_id").toString(),
        doc.getString("user"),
        doc.getString("date"),
        doc.getInteger("duration"),
        doc.getInteger("efficiency"));
  }
}
