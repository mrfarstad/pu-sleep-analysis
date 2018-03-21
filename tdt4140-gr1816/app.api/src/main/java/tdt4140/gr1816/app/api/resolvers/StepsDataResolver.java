package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.StepsData;
import tdt4140.gr1816.app.api.types.User;

public class StepsDataResolver implements GraphQLResolver<StepsData> {

  private final UserRepository userRepository;

  public StepsDataResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User user(StepsData stepsData) {
    return userRepository.findById(stepsData.getUserId());
  }
}
