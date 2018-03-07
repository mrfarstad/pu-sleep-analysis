package tdt4140.gr1816.app.api;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.resolvers.DataAccessRequestResolver;
import tdt4140.gr1816.app.api.resolvers.Mutation;
import tdt4140.gr1816.app.api.resolvers.Query;
import tdt4140.gr1816.app.api.resolvers.SigninResolver;
import tdt4140.gr1816.app.api.types.User;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

  public static final UserRepository userRepository;
  private static final DataAccessRequestRepository dataAccessRequestRepository;

  public static MongoDatabase mongo;

  static {
    String dbname = System.getenv("DB_NAME");
    System.out.println(dbname);
    mongo = new MongoClient().getDatabase(dbname == null ? "gruppe16" : dbname);
    userRepository = new UserRepository(mongo.getCollection("users"));
    dataAccessRequestRepository =
        new DataAccessRequestRepository(mongo.getCollection("dataAccessRequests"));
  }

  public GraphQLEndpoint() {
    super(buildSchema());
  }

  public static GraphQLSchema buildSchema() {
    return SchemaParser.newParser()
        .file("schema.graphqls")
        .resolvers(
            new Query(userRepository, dataAccessRequestRepository),
            new Mutation(userRepository, dataAccessRequestRepository),
            new SigninResolver(),
            new DataAccessRequestResolver(userRepository))
        .build()
        .makeExecutableSchema();
  }

  // Check the token of the User and return the users id
  @Override
  protected GraphQLContext createContext(
      Optional<HttpServletRequest> request, Optional<HttpServletResponse> response) {
    User user =
        request
            .map(req -> req.getHeader("Authorization"))
            .filter(id -> !id.isEmpty())
            .map(id -> id.replace("Bearer ", ""))
            .map(userRepository::findById)
            .orElse(null);
    return new AuthContext(user, request, response);
  }

  @Override
  protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
    return errors
        .stream()
        .filter(e -> e instanceof ExceptionWhileDataFetching || super.isClientError(e))
        .map(
            e ->
                e instanceof ExceptionWhileDataFetching
                    ? new SanitizedError((ExceptionWhileDataFetching) e)
                    : e)
        .collect(Collectors.toList());
  }
}
