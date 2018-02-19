package tdt4140.gr1816.app.core;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

public class Query implements GraphQLQueryResolver {

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
}