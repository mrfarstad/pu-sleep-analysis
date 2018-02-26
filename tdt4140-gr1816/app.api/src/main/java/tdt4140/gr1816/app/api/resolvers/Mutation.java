package tdt4140.gr1816.app.api.resolvers;

import tdt4140.gr1816.app.api.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.stereotype.Component;
import tdt4140.gr1816.app.api.types.Link;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class Mutation implements GraphQLMutationResolver {
    
    private static final LinkRepository linkRepository;

    static {
        MongoDatabase mongo = new MongoClient().getDatabase("gruppe16");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
    }
    
    public Link createLink(String url, String description) {
        Link newLink = new Link(url, description);
        linkRepository.saveLink(newLink);
        return newLink;
    }
}
