package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.Steps;
import tdt4140.gr1816.app.api.types.User;

public class StepsResolver implements GraphQLResolver<Steps> {

  private final UserRepository userRepository;

  public StepsResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(Steps steps) {
    return userRepository.findById(steps.getUserId());
  }
}
