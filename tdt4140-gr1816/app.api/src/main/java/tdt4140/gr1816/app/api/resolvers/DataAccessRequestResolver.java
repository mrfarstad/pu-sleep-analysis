package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.DataAccessRequest;
import tdt4140.gr1816.app.api.types.User;

public class DataAccessRequestResolver implements GraphQLResolver<DataAccessRequest> {

  private final UserRepository userRepository;

  public DataAccessRequestResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User dataOwner(DataAccessRequest dataAccess) {
    return userRepository.findById(dataAccess.getDataOwnerId());
  }

  public User requestedBy(DataAccessRequest dataAccess) {
    return userRepository.findById(dataAccess.getRequestedById());
  }
}
