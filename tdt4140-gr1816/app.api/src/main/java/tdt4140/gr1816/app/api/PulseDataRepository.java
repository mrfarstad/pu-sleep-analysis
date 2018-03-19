package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.PulseData;

public class PulseDataRepository {

  private final MongoCollection<Document> pulseDataDoc;

  public PulseDataRepository(MongoCollection<Document> pulseDataDoc) {
    this.pulseDataDoc = pulseDataDoc;
  }

  public PulseData findById(String id) {
    Document doc = pulseDataDoc.find(eq("_id", new ObjectId(id))).first();
    return pulseData(doc);
  }

  public List<PulseData> getAllPulseData() {
    List<PulseData> allPulseData = new ArrayList<>();
    for (Document doc : pulseDataDoc.find()) {
      allPulseData.add(pulseData(doc));
    }
    return allPulseData;
  }

  public PulseData savePulseData(PulseData pulseData) {
    Document doc = new Document();
    doc.append("user", pulseData.getUserId());
    doc.append("date", pulseData.getDate());
    doc.append("maxHr", pulseData.getMaxHr());
    doc.append("minHr", pulseData.getMinHr());
    pulseDataDoc.insertOne(doc);
    return new PulseData(
        doc.get("_id").toString(),
        pulseData.getUserId(),
        pulseData.getDate(),
        pulseData.getMaxHr(),
        pulseData.getMinHr());
  }

  public boolean deletePulseData(PulseData pulseData) {
    Document doc = pulseDataDoc.find(eq("_id", new ObjectId(pulseData.getId()))).first();
    DeleteResult result = pulseDataDoc.deleteOne(doc);
    return result.wasAcknowledged();
  }

  private PulseData pulseData(Document doc) {
    if (doc == null) {
      return null;
    }
    return new PulseData(
        doc.get("_id").toString(),
        doc.getString("user"),
        doc.getString("date"),
        doc.getInteger("maxHr"),
        doc.getInteger("minHr"));
  }
}
