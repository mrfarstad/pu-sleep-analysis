package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.SleepData;

public class SleepDataRepository {

  private final MongoCollection<Document> sleepsDoc;

  public SleepDataRepository(MongoCollection<Document> sleepsDoc) {
    this.sleepsDoc = sleepsDoc;
  }

  public SleepData findById(String id) {
    Document doc = sleepsDoc.find(eq("_id", new ObjectId(id))).first();
    return sleepData(doc);
  }

  public List<SleepData> getAllSleepData(String userId) {
    List<SleepData> allSleepData = new ArrayList<>();
    for (Document doc : sleepsDoc.find(eq("user", userId))) {
      allSleepData.add(sleepData(doc));
    }
    return allSleepData;
  }

  public SleepData saveSleepData(SleepData sleepData) {

    Document doc = new Document();
    doc.append("user", sleepData.getUserId());
    doc.append("date", sleepData.getDate());
    doc.append("duration", sleepData.getDuration());
    doc.append("efficiency", sleepData.getEfficiency());
    Bson filter =
        Filters.and(
            Filters.eq("date", sleepData.getDate()), Filters.eq("user", sleepData.getUserId()));
    Document old = sleepsDoc.find(filter).first();
    if (old != null) {
      sleepsDoc.updateOne(
          eq("_id", new ObjectId(old.get("_id").toString())), new Document("$set", doc));
      doc.append("_id", old.get("_id").toString());
    } else {
      sleepsDoc.insertOne(doc);
    }
    return new SleepData(
        doc.get("_id").toString(),
        sleepData.getUserId(),
        sleepData.getDate(),
        sleepData.getDuration(),
        sleepData.getEfficiency());
  }

  public boolean deleteSleepData(SleepData sleepData) {
    Document doc = sleepsDoc.find(eq("_id", new ObjectId(sleepData.getId()))).first();
    DeleteResult result = sleepsDoc.deleteOne(doc);
    return result.wasAcknowledged();
  }

  private SleepData sleepData(Document doc) {
    if (doc == null) {
      return null;
    }
    return new SleepData(
        doc.get("_id").toString(),
        doc.getString("user"),
        doc.getString("date"),
        doc.getInteger("duration"),
        doc.getInteger("efficiency"));
  }
}
