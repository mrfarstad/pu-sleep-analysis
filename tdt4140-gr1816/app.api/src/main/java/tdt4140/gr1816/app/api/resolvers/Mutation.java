package tdt4140.gr1816.app.api.resolvers;

import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Component;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.User;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class Mutation implements GraphQLMutationResolver {
    
    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;

    static {
        MongoDatabase mongo = new MongoClient().getDatabase("gruppe16");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
    }
    
    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }
    
    public User createUser(String id, String username, String password, boolean isDoctor, String gender, int age) {
        User newUser = new User(id, username, password, isDoctor, gender, age);
        userRepository.saveUser(newUser);
        return newUser;
    }
}
