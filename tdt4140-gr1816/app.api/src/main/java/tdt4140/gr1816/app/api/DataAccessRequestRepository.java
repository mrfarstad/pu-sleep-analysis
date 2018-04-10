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
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.DataAccessRequestStatus;
import tdt4140.gr1816.app.api.types.User;

public class DataAccessRequestRepository {

  private final MongoCollection<Document> dataAccessRequests;

  public DataAccessRequestRepository(MongoCollection<Document> dataAccessRequests) {
    this.dataAccessRequests = dataAccessRequests;
  }

  public DataAccessRequest findById(String id) {
    Document doc = dataAccessRequests.find(eq("_id", new ObjectId(id))).first();
    return dataAccessRequest(doc);
  }

  public List<DataAccessRequest> getAllDataAccessRequests() {
    List<DataAccessRequest> allRequests = new ArrayList<>();
    for (Document doc : dataAccessRequests.find()) {
      allRequests.add(dataAccessRequest(doc));
    }
    return allRequests;
  }

  public DataAccessRequest createDataRequest(String dataOwnerId, User requestedByUser) {
    Bson filter =
        Filters.and(
            Filters.eq("dataOwnerId", dataOwnerId),
            Filters.eq("requestedById", requestedByUser.getId()));
    Document doc = dataAccessRequests.find(filter).first();
    if (doc != null) {
      return dataAccessRequest(doc);
    }

    return saveDataAccess(new DataAccessRequest(dataOwnerId, requestedByUser.getId(), "PENDING"));
  }

  private DataAccessRequest saveDataAccess(DataAccessRequest dataAccess) {
    Document doc = new Document();

    doc.append("dataOwnerId", dataAccess.getDataOwnerId());
    doc.append("requestedById", dataAccess.getRequestedById());
    doc.append("status", dataAccess.getStatusAsString());

    dataAccessRequests.insertOne(doc);
    return dataAccessRequest(doc);
  }

  private DataAccessRequest dataAccessRequest(Document doc) {
    return new DataAccessRequest(
        doc.get("_id").toString(),
        doc.getString("dataOwnerId"),
        doc.getString("requestedById"),
        doc.getString("status"));
  }

  public DataAccessRequest answerDataAccessRequest(
      String dataRequestId, DataAccessRequestStatus status, User authenticatedUser) {
    Document doc = dataAccessRequests.find(eq("_id", new ObjectId(dataRequestId))).first();

    if (!authenticatedUser.getId().equals(doc.get("dataOwnerId"))) {
      return null;
    }

    dataAccessRequests.updateOne(
        eq("_id", new ObjectId(dataRequestId)),
        new Document("$set", new Document("status", DataAccessRequest.statusToString((status)))));
    return findById(dataRequestId);
  }

  public boolean deleteDataAccessRequest(DataAccessRequest request) {
    Document doc = dataAccessRequests.find(eq("_id", new ObjectId(request.getId()))).first();
    DeleteResult result = dataAccessRequests.deleteOne(doc);
    return result.wasAcknowledged();
  }
}
