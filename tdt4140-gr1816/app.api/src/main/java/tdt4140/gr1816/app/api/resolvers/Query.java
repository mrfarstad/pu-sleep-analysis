package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import tdt4140.gr1816.app.api.CharacterRepository;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.Character;
import tdt4140.gr1816.app.api.types.Droid;
import tdt4140.gr1816.app.api.types.Episode;
import tdt4140.gr1816.app.api.types.Human;
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

  private CharacterRepository characterRepository;

  public Character hero(Episode episode) {
    return episode != null
        ? characterRepository.getHeroes().get(episode)
        : characterRepository.getCharacters().get("1000");
  }

  public Human human(String id, DataFetchingEnvironment env) {
    return (Human)
        characterRepository
            .getCharacters()
            .values()
            .stream()
            .filter(character -> character instanceof Human && character.getId().equals(id))
            .findFirst()
            .orElseGet(null);
  }

  public Droid droid(String id) {
    return (Droid)
        characterRepository
            .getCharacters()
            .values()
            .stream()
            .filter(character -> character instanceof Droid && character.getId().equals(id))
            .findFirst()
            .orElseGet(null);
  }

  public Character character(String id) {
    return characterRepository.getCharacters().get(id);
  }

  public User viewer(DataFetchingEnvironment env) {
    AuthContext context = env.getContext();
    return context.getUser();
  }
}
