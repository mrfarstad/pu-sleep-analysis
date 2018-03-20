package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.SleepData;
import tdt4140.gr1816.app.api.types.User;

public class SleepDataResolver implements GraphQLResolver<SleepData> {

  private final UserRepository userRepository;

  public SleepDataResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(SleepData sleepData) {
    return userRepository.findById(sleepData.getUserId());
  }
}
