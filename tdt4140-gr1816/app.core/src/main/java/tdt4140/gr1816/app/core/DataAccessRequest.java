package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataAccessRequest {
  @JsonProperty("id")
  private final String id;

  @JsonProperty("dataOwner")
  private final User dataOwner;

  @JsonProperty("requestedBy")
  private final User requestedBy;

  @JsonProperty("status")
  private final String status;

  @JsonCreator
  public DataAccessRequest(
      @JsonProperty("id") String id,
      @JsonProperty("dataOwner") User dataOwner,
      @JsonProperty("requestedBy") User requestedBy,
      @JsonProperty("status") String status) {
    this.id = id;
    this.dataOwner = dataOwner;
    this.requestedBy = requestedBy;
    this.status = status;
  }

  public DataAccessRequest(String dataOwnerId, String requestedById, String status) {
    this(
        null,
        UserDataFetch.getUserByID(dataOwnerId),
        UserDataFetch.getUserByID(requestedById),
        status);
  }

  public String getId() {
    return id;
  }

  public User getDataOwner() {
    return dataOwner;
  }

  public User getRequestedBy() {
    return requestedBy;
  }

  public DataAccessRequestStatus getStatus() {
    return DataAccessRequest.statusFromString(status);
  }

  public String getStatusAsString() {
    return status;
  }

  public static String statusToString(DataAccessRequestStatus status) {
    switch (status) {
      case PENDING:
        return "PENDING";
      case ACCEPTED:
        return "ACCEPTED";
      case REJECTED:
      default:
        return "REJECTED";
    }
  }

  public static DataAccessRequestStatus statusFromString(String status) {
    switch (status) {
      case "PENDING":
        return DataAccessRequestStatus.PENDING;
      case "ACCEPTED":
        return DataAccessRequestStatus.ACCEPTED;
      case "REJECTED":
        return DataAccessRequestStatus.REJECTED;
      default:
        return DataAccessRequestStatus.PENDING;
    }
  }

  @Override
  public String toString() {
    String out = "DataOwner: \n";
    out += ("Username: " + this.dataOwner + "\n");
    out += ("Requested By: \n");
    out += ("Username: " + this.requestedBy + "\n");
    out += ("Status: " + statusToString(this.getStatus()) + "\n");
    return out;
  }

  public enum DataAccessRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
  }
}
