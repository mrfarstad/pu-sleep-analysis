package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;
import tdt4140.gr1816.app.api.types.Steps;

public class StepsRepository {

  private final MongoCollection<Document> stepsDoc;

  public StepsRepository(MongoCollection<Document> stepsDoc) {
    this.stepsDoc = stepsDoc;
  }

  public Steps findById(String id) {
    Document doc = stepsDoc.find(eq("_id", new ObjectId(id))).first();
    return steps(doc);
  }

  public List<Steps> getAllSteps() {
    List<Steps> allSteps = new ArrayList<>();
    for (Document doc : stepsDoc.find()) {
      allSteps.add(steps(doc));
    }
    return allSteps;
  }

  public Steps saveSteps(Steps steps) {
    Document doc = new Document();
    doc.append("user", steps.getUserId());
    doc.append("date", steps.getDate());
    doc.append("steps", steps.getSteps());
    stepsDoc.insertOne(doc);
    return new Steps(
        doc.get("_id").toString(), steps.getUserId(), steps.getDate(), steps.getSteps());
  }

  public boolean deleteSteps(Steps steps) {
    Document doc = stepsDoc.find(eq("_id", new ObjectId(steps.getId()))).first();
    DeleteResult result = stepsDoc.deleteOne(doc);
    return result.wasAcknowledged();
  }

  private Steps steps(Document doc) {
    if (doc == null) {
      return null;
    }
    return new Steps(
        doc.get("_id").toString(),
        doc.getString("user"),
        doc.getString("date"),
        doc.getInteger("steps"));
  }
}
