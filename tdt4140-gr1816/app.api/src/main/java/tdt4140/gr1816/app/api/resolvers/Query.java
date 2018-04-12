package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.MessageRepository;
import tdt4140.gr1816.app.api.PulseDataRepository;
import tdt4140.gr1816.app.api.SleepDataRepository;
import tdt4140.gr1816.app.api.StepsDataRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.AverageData;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.DataAccessRequestStatus;
import tdt4140.gr1816.app.api.types.Message;
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
  private final MessageRepository messageRepository;

  public Query(
      UserRepository userRepository,
      SleepDataRepository sleepDataRepository,
      StepsDataRepository stepsDataRepository,
      PulseDataRepository pulseDataRepository,
      DataAccessRequestRepository dataAccessRequestRepository,
      MessageRepository messageRepository) {
    this.userRepository = userRepository;
    this.sleepDataRepository = sleepDataRepository;
    this.stepsDataRepository = stepsDataRepository;
    this.pulseDataRepository = pulseDataRepository;
    this.dataAccessRequestRepository = dataAccessRequestRepository;
    this.messageRepository = messageRepository;
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

  public List<SleepData> sleepDataBetweenDates(
      String userId, String startDate, String endDate, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return sleepDataRepository.getSleepDataBetweenDates(userId, startDate, endDate);
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

  public List<StepsData> stepsDataBetweenDates(
      String userId, String startDate, String endDate, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return stepsDataRepository.getStepsDataBetweenDates(userId, startDate, endDate);
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

  public List<PulseData> pulseDataBetweenDates(
      String userId, String startDate, String endDate, DataFetchingEnvironment env) {
    if (!hasUserAccess(env, userId)) {
      throw new GraphQLException("Cannot access user data");
    }
    return pulseDataRepository.getPulseDataBetweenDates(userId, startDate, endDate);
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

  public List<Message> messagesForField(
      Function<String, List<Message>> func, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return new ArrayList<Message>();
    }
    return func.apply(user.getId());
  }

  public List<Message> messagesForMe(DataFetchingEnvironment env) {
    return messagesForField(messageRepository::getAllMessagesToUser, env);
  }

  public List<Message> messagesByMe(DataFetchingEnvironment env) {
    return messagesForField(messageRepository::getAllMessagesByUser, env);
  }

  public AverageData getMyAverageData(String fromDate, String toDate, DataFetchingEnvironment env) {

    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return null;
    }

    return getAverageDataForUsers(fromDate, toDate, Arrays.asList(user));
  }

  public AverageData getAverageData(String fromDate, String toDate, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      return null;
    }
    List<User> users =
        userRepository
            .getAllUsers()
            .stream()
            .filter(localUser -> user.isInSameGroup(localUser))
            .collect(Collectors.toList());
    return getAverageDataForUsers(fromDate, toDate, users);
  }

  public AverageData getAverageDataForUsers(String fromDate, String toDate, List<User> users) {
    SleepData data = sleepDataRepository.getAverageForGroup(users);
    int steps = stepsDataRepository.getAverageForGroup(users);
    int restHr = pulseDataRepository.getAverageForGroup(users);

    return new AverageData(restHr, data.getDuration(), data.getEfficiency(), steps, "");
  }
}
