package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.VoteRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.auth.AuthData;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.User;
import tdt4140.gr1816.app.api.types.Vote;

public class Mutation implements GraphQLRootResolver {

  private final LinkRepository linkRepository;
  private final UserRepository userRepository;
  private final VoteRepository voteRepository;

  public Mutation(
      LinkRepository linkRepository, UserRepository userRepository, VoteRepository voteRepository) {
    this.linkRepository = linkRepository;
    this.userRepository = userRepository;
    this.voteRepository = voteRepository;
  }

  // The way to inject the context is via DataFetchingEnvironment
  public Link createLink(String url, String description, DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    Link newLink = new Link(url, description, context.getUser().getId());
    linkRepository.saveLink(newLink);
    return newLink;
  }

  public User createUser(AuthData auth, boolean isDoctor, String gender, int age) {
    User newUser = new User(auth.getUsername(), auth.getPassword(), isDoctor, gender, age);
    return userRepository.saveUser(newUser);
  }

  public Vote createVote(String linkId, String userId) {
    ZonedDateTime now = Instant.now().atZone(ZoneOffset.ofHours(1));
    return voteRepository.saveVote(new Vote(now, userId, linkId));
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
}
