package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.User;

public class Query implements GraphQLRootResolver {

  private final LinkRepository linkRepository;
  private final UserRepository userRepository;

  public Query(LinkRepository linkRepository, UserRepository userRepository) {
    this.linkRepository = linkRepository;
    this.userRepository = userRepository;
  }

  public List<Link> allLinks() {
    return linkRepository.getAllLinks();
  }

  public List<User> allUsers() {
    return userRepository.getAllUsers();
  }

  public User viewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    return context.getUser();
  }
}
