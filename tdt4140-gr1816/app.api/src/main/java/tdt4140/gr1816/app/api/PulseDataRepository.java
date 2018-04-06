package tdt4140.gr1816.app.api;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.conversions.Bson;
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

  public List<PulseData> getAllPulseData(String userId) {
    List<PulseData> allPulseData = new ArrayList<>();
    for (Document doc : pulseDataDoc.find(eq("user", userId))) {
      allPulseData.add(pulseData(doc));
    }
    return allPulseData;
  }

  /*
   * This method assumes that the dates are of the correct form 'yyyy-mm-dd'
   */
  public List<PulseData> getPulseDataBetweenDates(String userId, String startDate, String endDate) {
    return getAllPulseData(userId)
        .stream()
        .filter(
            data -> {
              DateTimeFormatter dmf = DateTimeFormatter.ISO_LOCAL_DATE;
              LocalDate from = LocalDate.parse(startDate, dmf);
              LocalDate to = LocalDate.parse(endDate, dmf);
              LocalDate date = LocalDate.parse(data.getDate(), dmf);
              return date.compareTo(from) > 0 && date.compareTo(to) < 0;
            })
        .collect(Collectors.toList());
  }

  public PulseData savePulseData(PulseData pulseData) {
    Document doc = new Document();
    doc.append("user", pulseData.getUserId());
    doc.append("date", pulseData.getDate());
    doc.append("restHr", pulseData.getRestHr());

    Bson filter =
        Filters.and(
            Filters.eq("date", pulseData.getDate()), Filters.eq("user", pulseData.getUserId()));
    Document old = pulseDataDoc.find(filter).first();
    if (old != null) {
      pulseDataDoc.updateOne(
          eq("_id", new ObjectId(old.get("_id").toString())), new Document("$set", doc));
      doc.append("_id", old.get("_id").toString());
    } else {
      pulseDataDoc.insertOne(doc);
    }

    return new PulseData(
        doc.get("_id").toString(),
        pulseData.getUserId(),
        pulseData.getDate(),
        pulseData.getRestHr());
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
        doc.getInteger("restHr"));
  }
}
