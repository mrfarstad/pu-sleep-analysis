package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.auth.AuthData;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.DataAccessRequestStatus;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.User;

public class Mutation implements GraphQLRootResolver {

  private final UserRepository userRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;

  public Mutation(
      UserRepository userRepository, DataAccessRequestRepository dataAccessRequestRepository) {
    this.userRepository = userRepository;
    this.dataAccessRequestRepository = dataAccessRequestRepository;
  }

  public User createUser(AuthData auth, boolean isDoctor, String gender, int age) {
    User newUser = new User(auth.getUsername(), auth.getPassword(), isDoctor, gender, age);
    return userRepository.saveUser(newUser);
  }

  public boolean deleteUser(AuthData auth) {
    User user = userRepository.findByUsername(auth.getUsername());
    if (user == null) {
      throw new GraphQLException("Nonexistent username");
    }

    if (user.getPassword().equals(auth.getPassword())) {
      boolean acknowledged = userRepository.deleteUser(user);
      return acknowledged;
    }
    throw new GraphQLException("Invalid credentials");
  }

  public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
    User user = userRepository.findByUsername(auth.getUsername());
    if (user == null) {
      throw new GraphQLException("Invalid user");
    }

    if (user.getPassword().equals(auth.getPassword())) {
      return new SigninPayload(user.getId(), user);
    }
    throw new GraphQLException("Invalid credentials");
  }

  public DataAccessRequest requestDataAccess(String dataOwnerId, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Invalid user");
    }
    if (!user.isDoctor()) {
      throw new GraphQLException("User must be a doctor....");
    }
    return dataAccessRequestRepository.createDataRequest(dataOwnerId, user);
  }

  public DataAccessRequest answerDataAccessRequest(
      String dataAccessRequestId, DataAccessRequestStatus status, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    return dataAccessRequestRepository.answerDataAccessRequest(dataAccessRequestId, status, user);
  }
}
