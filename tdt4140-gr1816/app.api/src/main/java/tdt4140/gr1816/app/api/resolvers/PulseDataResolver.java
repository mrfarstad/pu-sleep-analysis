package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.PulseData;
import tdt4140.gr1816.app.api.types.User;

public class PulseDataResolver implements GraphQLResolver<PulseData> {

  private final UserRepository userRepository;

  public PulseDataResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(PulseData pulseData) {
    return userRepository.findById(pulseData.getUserId());
  }
}
