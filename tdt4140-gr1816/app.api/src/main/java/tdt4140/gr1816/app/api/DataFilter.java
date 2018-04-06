package tdt4140.gr1816.app.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataFilter {

  private String fromDate;
  private String toDate;

  @JsonProperty("fromDate") // the name must match the schema
  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  @JsonProperty("toDate") // the name must match the schema
  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
  }
}
