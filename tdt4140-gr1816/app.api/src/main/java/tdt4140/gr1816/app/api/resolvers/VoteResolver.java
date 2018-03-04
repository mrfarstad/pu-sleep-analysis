package tdt4140.gr1816.app.api.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;

import tdt4140.gr1816.app.api.LinkRepository;
import tdt4140.gr1816.app.api.UserRepository;
import tdt4140.gr1816.app.api.types.Link;
import tdt4140.gr1816.app.api.types.User;
import tdt4140.gr1816.app.api.types.Vote;

public class VoteResolver implements GraphQLResolver<Vote> {
    
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public User user(Vote vote) {
        return userRepository.findById(vote.getUserId());
    }
    
    public Link link(Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }
}
