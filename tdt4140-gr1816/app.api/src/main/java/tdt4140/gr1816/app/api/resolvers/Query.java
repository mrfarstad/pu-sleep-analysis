package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.PulseDataRepository;
import tdt4140.gr1816.app.api.SleepDataRepository;
import tdt4140.gr1816.app.api.StepsDataRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.PulseData;
import tdt4140.gr1816.app.api.types.SleepData;
import tdt4140.gr1816.app.api.types.StepsData;
import tdt4140.gr1816.app.api.types.User;

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

  public List<SleepData> allSleepData(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<SleepData>();
    }
    return sleepDataRepository.getAllSleepData();
  }

  public List<SleepData> sleepDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<SleepData>();
    }
    return allSleepData(env)
        .stream()
        .filter(sleep -> sleep.getUserId().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public List<StepsData> allStepsData(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<StepsData>();
    }
    return stepsDataRepository.getAllStepsData();
  }

  public List<StepsData> stepsDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<StepsData>();
    }
    return allStepsData(env)
        .stream()
        .filter(steps -> steps.getUserId().equals(user.getId()))
        .collect(Collectors.toList());
  }

  public List<PulseData> allPulseData(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<PulseData>();
    }
    return pulseDataRepository.getAllPulseData();
  }

  public List<PulseData> pulseDataByViewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<PulseData>();
    }
    return allPulseData(env)
        .stream()
        .filter(pulseData -> pulseData.getUserId().equals(user.getId()))
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
