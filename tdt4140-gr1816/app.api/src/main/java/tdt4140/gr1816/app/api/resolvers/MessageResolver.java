package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.Message;
import tdt4140.gr1816.app.api.types.User;

public class MessageResolver implements GraphQLResolver<Message> {

  private final UserRepository userRepository;

  public MessageResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User from(Message message) {
    return userRepository.findById(message.getFromId());
  }

  public User to(Message message) {
    return userRepository.findById(message.getToId());
  }
}
