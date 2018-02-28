package tdt4140.gr1816.app.api.resolvers;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import graphql.GraphQLException;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.AuthData;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.User;

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
    
    public User createUser(String id, String name, AuthData auth, boolean isDoctor, String gender, int age) {
        User newUser = new User(id, name, auth.getUsername(), auth.getPassword(), isDoctor, gender, age);
        return userRepository.saveUser(newUser);
    }
    
    public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
        User user = userRepository.findByUsername(auth.getUsername());
        if (user.getPassword().equals(auth.getPassword())) {
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }
}
