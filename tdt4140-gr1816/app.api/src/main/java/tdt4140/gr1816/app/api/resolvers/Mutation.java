package tdt4140.gr1816.app.api.resolvers;


import com.coxautodev.graphql.tools.GraphQLRootResolver;

import graphql.GraphQLException;
import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.AuthData;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.SigninPayload;
import tdt4140.gr1816.app.api.types.User;

public class Mutation implements GraphQLRootResolver {
    
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }
    
    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }

    
    public User createUser(AuthData auth, boolean isDoctor, String gender, int age) {
        User newUser = new User(auth.getUsername(), auth.getPassword(), isDoctor, gender, age);
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
