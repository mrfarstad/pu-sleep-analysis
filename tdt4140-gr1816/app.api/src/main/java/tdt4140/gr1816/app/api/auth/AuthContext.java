package tdt4140.gr1816.app.api.auth;

import graphql.servlet.GraphQLContext;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tdt4140.gr1816.app.api.types.User;

public class AuthContext extends GraphQLContext {

  private final User user;

  public AuthContext(
      User user, Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    super(request, response);
    this.user = user;
  }

  public User getUser() {
    return user;
  }
}
