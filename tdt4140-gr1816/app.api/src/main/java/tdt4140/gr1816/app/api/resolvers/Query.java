package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.User;

public class Query implements GraphQLRootResolver {

  private final LinkRepository linkRepository;
  private final UserRepository userRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;

  public Query(
      LinkRepository linkRepository,
      UserRepository userRepository,
      DataAccessRequestRepository dataAccessRequestRepository) {
    this.linkRepository = linkRepository;
    this.userRepository = userRepository;
    this.dataAccessRequestRepository = dataAccessRequestRepository;
  }

  public List<Link> allLinks() {
    return linkRepository.getAllLinks();
  }

  public User viewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    if (context == null) {
      return null;
    }
    return context.getUser();
  }

  public List<User> allUsers() {
    return userRepository.getAllUsers();
  }

  private List<DataAccessRequest> allDataAccessRequests(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<DataAccessRequest>();
    }
    return dataAccessRequestRepository.getAllDataAccessRequests();
  }

  public List<DataAccessRequest> myDataAccessRequests(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<DataAccessRequest>();
    }
    return allDataAccessRequests(env)
        .stream()
        .filter(request -> request.getRequestedById().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public List<DataAccessRequest> dataAccessRequestsForMe(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<DataAccessRequest>();
    }
    return allDataAccessRequests(env)
        .stream()
        .filter(request -> request.getDataOwnerId().equals(user.getId()))
        .collect(Collectors.toList());
  }
}
