package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import graphql.ExecutionResult;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class DataAccessRequestTest extends ApiBaseCase {

  @Test
  public void testDataAccessRequestsFromUserPerspective() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);

    // Preps database by creating a request
    ExecutionResult tempresult =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \"" + user.getId() + "\") {id status} }",
            doctorContext);

    String query =
        "{\n"
            + "  dataAccessRequestsForMe {\n"
            + "    id\n"
            + "    dataOwner {\n"
            + "      id\n"
            + "    }\n"
            + "    requestedBy {\n"
            + "      id\n"
            + "    }\n"
            + "    status\n"
            + "  }\n"
            + "}";

    ExecutionResult res = executeQuery(query, userContext);

    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> dataAccessRequestsForMe = (ArrayList<Map>) result.get("dataAccessRequestsForMe");

    assertEquals(dataAccessRequestsForMe.size(), 1);
    assertEquals(dataAccessRequestsForMe.get(0).get("status"), "PENDING");

    String requestId = (String) dataAccessRequestsForMe.get(0).get("id");

    ExecutionResult deleteRes =
        executeQuery(
            "mutation {deleteDataAccessRequest(dataAccessRequestId: \"" + requestId + "\")}",
            userContext);

    Map<String, Object> deleteResult = deleteRes.getData();

    assertTrue((Boolean) deleteResult.get("deleteDataAccessRequest"));
  }

  @Test
  public void testDataAccessRequestsFromDoctorsPerspective() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);
    // Preps database by creating a request
    ExecutionResult tempresult =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \"" + user.getId() + "\") {id status} }",
            doctorContext);

    String query =
        "{\n"
            + "  myDataAccessRequests {\n"
            + "    id\n"
            + "    dataOwner {\n"
            + "      id\n"
            + "    }\n"
            + "    requestedBy {\n"
            + "      id\n"
            + "    }\n"
            + "    status\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, doctorContext);

    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> dataAccessRequestsFromDoc = (ArrayList<Map>) result.get("myDataAccessRequests");
    assertEquals(dataAccessRequestsFromDoc.size(), 1);
    assertEquals(dataAccessRequestsFromDoc.get(0).get("status"), "PENDING");
  }

  @Test
  public void testAnswerDataAccessRequest() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);
    // Preps database by creating a request
    ExecutionResult tempres =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \""
                + user.getId()
                + "\") {id dataOwner{id} requestedBy{id} status} }",
            doctorContext);

    Map<String, Object> tempresult = tempres.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> request = (Map<String, Object>) tempresult.get("requestDataAccess");

    String ans = "ACCEPTED";
    String answerQuery =
        "mutation {\n"
            + "  answerDataAccessRequest(dataAccessRequestId: \""
            + request.get("id")
            + "\", status: "
            + ans
            + ") {\n"
            + "    status\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(answerQuery, userContext);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> answeredRequest =
        (Map<String, Object>) result.get("answerDataAccessRequest");

    assertNotNull(answeredRequest);
    assertEquals(answeredRequest.get("status"), "ACCEPTED");

    ans = "REJECTED";
    answerQuery =
        "mutation {\n"
            + "  answerDataAccessRequest(dataAccessRequestId: \""
            + request.get("id")
            + "\", status: "
            + ans
            + ") {\n"
            + "    status\n"
            + "  }\n"
            + "}";
    res = executeQuery(answerQuery, userContext);
    result = res.getData();
    @SuppressWarnings("unchecked")
    Map<String, Object> newAnsweredRequest =
        (Map<String, Object>) result.get("answerDataAccessRequest");
    assertEquals(newAnsweredRequest.get("status"), "REJECTED");
  }
}
