package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.SleepRepository;
import tdt4140.gr1816.app.api.StepsRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.Sleep;
import tdt4140.gr1816.app.api.types.Steps;
import tdt4140.gr1816.app.api.types.User;

public class Query implements GraphQLRootResolver {

  private final UserRepository userRepository;
  private final SleepRepository sleepRepository;
  private final StepsRepository stepsRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;

  public Query(
      UserRepository userRepository,
      SleepRepository sleepRepository,
      StepsRepository stepsRepository,
      DataAccessRequestRepository dataAccessRequestRepository) {
    this.userRepository = userRepository;
    this.sleepRepository = sleepRepository;
    this.stepsRepository = stepsRepository;
    this.dataAccessRequestRepository = dataAccessRequestRepository;
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

  public List<Sleep> allSleeps(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<Sleep>();
    }
    return sleepRepository.getAllSleeps();
  }

  public List<Sleep> sleepsByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<Sleep>();
    }
    return allSleeps(env)
        .stream()
        .filter(sleep -> sleep.getUserId().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public List<Steps> allSteps(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<Steps>();
    }
    return stepsRepository.getAllSteps();
  }

  public List<Steps> stepsByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<Steps>();
    }
    return allSteps(env)
        .stream()
        .filter(steps -> steps.getUserId().equals(user.getId()))
        .collect(Collectors.toList());
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
