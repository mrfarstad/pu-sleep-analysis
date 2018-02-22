package tdt4140.gr1816.app.core;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphQLEndpoint() {
		super(buildSchema());
	}

	private static GraphQLSchema buildSchema() {
		LinkRepository linkRepository = new LinkRepository();
		UserRepository userRepository = new UserRepository();
		return SchemaParser
				.newParser()
				.file("/schema.graphqls")
				.resolvers(new Query(linkRepository, userRepository), new Mutation(linkRepository, userRepository))
				.build()
				.makeExecutableSchema();
	}
}
