package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tdt4140.gr1816.app.api.*;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.*;

public class Query implements GraphQLRootResolver {

  private final UserRepository userRepository;
  private final SleepDataRepository sleepDataRepository;
  private final StepsDataRepository stepsDataRepository;
  private final PulseDataRepository pulseDataRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;

  public Query(
      UserRepository userRepository,
      SleepDataRepository sleepDataRepository,
      StepsDataRepository stepsDataRepository,
      PulseDataRepository pulseDataRepository,
      DataAccessRequestRepository dataAccessRequestRepository) {
    this.userRepository = userRepository;
    this.sleepDataRepository = sleepDataRepository;
    this.stepsDataRepository = stepsDataRepository;
    this.pulseDataRepository = pulseDataRepository;
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

  public boolean hasUserAccess(DataFetchingEnvironment env, final String requestedUserId) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return false;
    }
    DataAccessRequest request =
        this.myDataAccessRequests(env)
            .stream()
            .filter(
                req ->
                    req.getDataOwnerId().equals(requestedUserId)
                        && req.getStatus() == DataAccessRequestStatus.ACCEPTED)
            .findFirst()
            .orElse(null);
    return request != null || user.getId().equals(requestedUserId);
  }

  public List<SleepData> allSleepData(String userId, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return sleepDataRepository.getAllSleepData(userId);
  }

  public List<SleepData> sleepDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Cannot access user data");
    }
    return allSleepData(user.getId(), env);
  }

  public List<StepsData> allStepsData(String userId, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return stepsDataRepository.getAllStepsData(userId);
  }

  public List<StepsData> stepsDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Cannot access user data");
    }
    return allStepsData(user.getId(), env);
  }

  public List<PulseData> allPulseData(String userId, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return pulseDataRepository.getAllPulseData(userId);
  }

  public List<PulseData> pulseDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Cannot access user data");
    }
    return allPulseData(user.getId(), env);
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
