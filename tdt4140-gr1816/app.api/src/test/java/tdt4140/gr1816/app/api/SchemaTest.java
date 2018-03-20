package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertNull;

import graphql.ExecutionResult;
import graphql.GraphQL;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.types.User;

public class SchemaTest extends ApiBaseCase {

  @Test
  public void SchemaWillCompile() {
    getGraph();
  }

  @Test
  public void viewerIsNullUnAuthorized() {
    GraphQL graphQL = getGraph();
    ExecutionResult res = graphQL.execute("{viewer {id}}");
    Map<String, Object> result = res.getData();
    assertNull(result.get("viewer"));
  }

  @Test
  public void noUserShouldExist() {
    User u2 = GraphQLEndpoint.userRepository.findByUsername("test");
    assertNull(u2);
  }
}
