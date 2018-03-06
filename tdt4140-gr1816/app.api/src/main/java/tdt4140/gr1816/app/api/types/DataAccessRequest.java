package tdt4140.gr1816.app.api.types;

public class DataAccessRequest {

  private final String id;
  private final String dataOwnerId;
  private final String requestedById;
  private final String status;

  public DataAccessRequest(String id, String dataOwnerId, String requestedById, String status) {
    super();
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
}
