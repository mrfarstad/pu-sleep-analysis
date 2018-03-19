package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.StepsData;

public class StepsDataRepository {

  private final MongoCollection<Document> stepsDoc;

  public StepsDataRepository(MongoCollection<Document> stepsDoc) {
    this.stepsDoc = stepsDoc;
  }

  public StepsData findById(String id) {
    Document doc = stepsDoc.find(eq("_id", new ObjectId(id))).first();
    return stepsData(doc);
  }

  public List<StepsData> getAllStepsData() {
    List<StepsData> allStepsData = new ArrayList<>();
    for (Document doc : stepsDoc.find()) {
      allStepsData.add(stepsData(doc));
    }
    return allStepsData;
  }

  public StepsData saveStepsData(StepsData stepsData) {
    Document doc = new Document();
    doc.append("user", stepsData.getUserId());
    doc.append("date", stepsData.getDate());
    doc.append("steps", stepsData.getSteps());
    stepsDoc.insertOne(doc);
    return new StepsData(
        doc.get("_id").toString(),
        stepsData.getUserId(),
        stepsData.getDate(),
        stepsData.getSteps());
  }

  public boolean deleteStepsData(StepsData stepsData) {
    Document doc = stepsDoc.find(eq("_id", new ObjectId(stepsData.getId()))).first();
    DeleteResult result = stepsDoc.deleteOne(doc);
    return result.wasAcknowledged();
  }

  private StepsData stepsData(Document doc) {
    if (doc == null) {
      return null;
    }
    return new StepsData(
        doc.get("_id").toString(),
        doc.getString("user"),
        doc.getString("date"),
        doc.getInteger("steps"));
  }
}
