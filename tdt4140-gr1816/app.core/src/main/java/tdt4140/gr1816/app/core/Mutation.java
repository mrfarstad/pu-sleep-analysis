package tdt4140.gr1816.app.core;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

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
    
    public User createUser(String name, boolean isDoctor, String gender, int age) {
    		User newUser = new User(name, isDoctor, gender, age);
    		userRepository.saveUser(newUser);
    		return newUser;
    }
}
