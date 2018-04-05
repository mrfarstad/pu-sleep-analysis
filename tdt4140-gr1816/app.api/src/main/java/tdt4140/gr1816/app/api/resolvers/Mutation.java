package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import tdt4140.gr1816.app.api.DataAccessRequestRepository;
import tdt4140.gr1816.app.api.MessageRepository;
import tdt4140.gr1816.app.api.PulseDataRepository;
import tdt4140.gr1816.app.api.SleepDataRepository;
import tdt4140.gr1816.app.api.StepsDataRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.auth.AuthData;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.DataAccessRequestStatus;
import tdt4140.gr1816.app.api.types.Message;
import tdt4140.gr1816.app.api.types.PulseData;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.SleepData;
import tdt4140.gr1816.app.api.types.StepsData;
import tdt4140.gr1816.app.api.types.User;

public class Mutation implements GraphQLRootResolver {

  private final UserRepository userRepository;
  private final SleepDataRepository sleepDataRepository;
  private final StepsDataRepository stepsDataRepository;
  private final DataAccessRequestRepository dataAccessRequestRepository;
  private final PulseDataRepository pulseDataRepository;
  private final MessageRepository messageRepository;

  public Mutation(
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

  public User createUser(AuthData auth, boolean isDoctor, String gender, int age) {
    User newUser = new User(auth.getUsername(), auth.getPassword(), isDoctor, gender, age, true);
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

  public SleepData createSleepData(
      String date, int duration, int efficiency, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Please log in");
    }
    if (!user.isGatheringData()) {
      throw new GraphQLException("This is not legal. DataGathering is disabled");
    }
    SleepData newSleepData = new SleepData(user.getId(), date, duration, efficiency);
    return sleepDataRepository.saveSleepData(newSleepData);
  }

  public Boolean deleteSleepData(String sleepId) {
    SleepData sleepData = sleepDataRepository.findById(sleepId);
    if (sleepData == null) {
      throw new GraphQLException("Invalid sleep id");
    }
    return sleepDataRepository.deleteSleepData(sleepData);
  }

  public StepsData createStepsData(String date, int steps, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Please log in");
    }
    if (!user.isGatheringData()) {
      throw new GraphQLException("This is not legal. DataGathering is disabled");
    }
    StepsData newStepsData = new StepsData(user.getId(), date, steps);
    return stepsDataRepository.saveStepsData(newStepsData);
  }

  public Boolean deleteStepsData(String stepsId) {
    StepsData stepsData = stepsDataRepository.findById(stepsId);
    if (stepsData == null) {
      throw new GraphQLException("Invalid steps id");
    }
    return stepsDataRepository.deleteStepsData(stepsData);
  }

  public PulseData createPulseData(String date, int restHr, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Please log in");
    }
    if (!user.isGatheringData()) {
      throw new GraphQLException("This is not legal. DataGathering is disabled");
    }
    PulseData newPulseData = new PulseData(user.getId(), date, restHr);
    return pulseDataRepository.savePulseData(newPulseData);
  }

  public Boolean deletePulseData(String pulseDataId) {
    PulseData pulseData = pulseDataRepository.findById(pulseDataId);
    if (pulseData == null) {
      throw new GraphQLException("Invalid pulse data id");
    }
    return pulseDataRepository.deletePulseData(pulseData);
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

  public boolean setIsGatheringData(Boolean status, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Invalid user");
    }
    return userRepository.setIsGatheringData(user, status);
  }

  public Message createMessage(
      String fromId, String toId, String subject, String message, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    User user = context.getUser();
    if (user == null) {
      throw new GraphQLException("Please log in");
    }
    Message msg = new Message(fromId, toId, subject, message);
    return messageRepository.createMessage(msg);
  }
}
