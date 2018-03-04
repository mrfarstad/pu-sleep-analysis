package tdt4140.gr1816.app.api;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import tdt4140.gr1816.app.api.resolvers.Mutation;
import tdt4140.gr1816.app.api.resolvers.Query;


@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;
    private static final UserRepository userRepository;

    static {
        //Change to `new MongoClient("mongodb://<host>:<port>/hackernews")`
        //if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("gruppe16");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
    }
    
    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(new Query(linkRepository, userRepository), new Mutation(linkRepository, userRepository))
                .build()
                .makeExecutableSchema();
    }
}
