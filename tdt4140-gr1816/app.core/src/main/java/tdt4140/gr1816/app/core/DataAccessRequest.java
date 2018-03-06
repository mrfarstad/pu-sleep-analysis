package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DataAccessRequest {
  @JsonProperty("id")
  private final String id;

  @JsonProperty("dataOwnerId")
  private final String dataOwnerId;

  @JsonProperty("requestedById")
  private final String requestedById;

  @JsonProperty("status")
  private final String status;

  @JsonCreator
  public DataAccessRequest(
      @JsonProperty("id") String id,
      @JsonProperty("dataOwnerId") String dataOwnerId,
      @JsonProperty("requestedById") String requestedById,
      @JsonProperty("status") String status) {
    this.id = id;
    this.dataOwnerId = dataOwnerId;
    this.requestedById = requestedById;
    this.status = status;
  }

  public DataAccessRequest(String dataOwnerId, String requestedById, String status) {
    this(null, dataOwnerId, requestedById, status);
  }

  public String getId() {
    return id;
  }

  public String getDataOwnerId() {
    return dataOwnerId;
  }

  public String getRequestedById() {
    return requestedById;
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

  public enum DataAccessRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
  }
}
