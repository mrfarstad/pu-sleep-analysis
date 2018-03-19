package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.Sleep;
import tdt4140.gr1816.app.api.types.User;

public class SleepResolver implements GraphQLResolver<Sleep> {

  private final UserRepository userRepository;

  public SleepResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(Sleep sleep) {
    return userRepository.findById(sleep.getUserId());
  }
}
