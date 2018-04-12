package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import graphql.*;
import graphql.language.SourceLocation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
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

  @Test
  public void authContextThingWithUser() {
    User user = createUser();

    HttpServletRequest req = mock(HttpServletRequest.class);

    given(req.getHeader("Authorization")).willReturn("Bearer " + user.getId());
    GraphQLEndpoint e = new GraphQLEndpoint();
    AuthContext ctx = (AuthContext) e.createContext(Optional.ofNullable(req), null);

    assertEquals(ctx.getUser().getId(), user.getId());
  }

  @Test
  public void authContextThingWithoutValidUser() {
    createUser();

    HttpServletRequest req = mock(HttpServletRequest.class);

    given(req.getHeader("Authorization")).willReturn("Bearer 5ab25139c9b9f214ab761e29");
    GraphQLEndpoint e = new GraphQLEndpoint();
    AuthContext ctx = (AuthContext) e.createContext(Optional.ofNullable(req), null);

    assertEquals(ctx.getUser(), null);
  }

  @Test
  public void authContextThingWithoutUser() {
    createUser();

    HttpServletRequest req = mock(HttpServletRequest.class);

    given(req.getHeader("Authorization")).willReturn("");
    GraphQLEndpoint e = new GraphQLEndpoint();
    AuthContext ctx = (AuthContext) e.createContext(Optional.ofNullable(req), null);

    assertEquals(ctx.getUser(), null);
  }

  @Test
  public void exceptionFilteringShouldHandleErrors() {
    GraphQLEndpoint e = new GraphQLEndpoint();

    e.filterGraphQLErrors(
        Arrays.asList(
            new GraphQLError() {
              @Override
              public String getMessage() {
                return null;
              }

              @Override
              public List<SourceLocation> getLocations() {
                return null;
              }

              @Override
              public ErrorType getErrorType() {
                return ErrorType.InvalidSyntax;
              }
            },
            new GraphQLError() {
              @Override
              public String getMessage() {
                return null;
              }

              @Override
              public List<SourceLocation> getLocations() {
                return null;
              }

              @Override
              public ErrorType getErrorType() {
                return ErrorType.ValidationError;
              }
            },
            new GraphQLError() {
              @Override
              public String getMessage() {
                return null;
              }

              @Override
              public List<SourceLocation> getLocations() {
                return null;
              }

              @Override
              public ErrorType getErrorType() {
                return ErrorType.MutationNotSupported;
              }
            },
            new ExceptionWhileDataFetching(new Exception("Rip"))));
  }
}
