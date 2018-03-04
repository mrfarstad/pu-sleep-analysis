package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;

import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.User;

public class LinkResolver implements GraphQLResolver<Link> {
    
    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User postedBy(Link link) {
        if (link.getUserId() == null) {
            return null;
        }
        return userRepository.findById(link.getUserId());
    }
}
