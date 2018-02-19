package tdt4140.gr1816.app.core;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

	private final List<User> users;

	public UserRepository() {
		users = new ArrayList<>();
		// add some links to start off with
		users.add(new User("Odin Ugedal", false, "male", 21));
		users.add(new User("Ole kristian Vingdal", false, "male", 21));
		users.add(new User("Martin Farstad", false, "male", 21));
		users.add(new User("Odin Ugedal2", false, "male", 21));
		users.add(new User("Odin Ugedal3", false, "male", 24));
		users.add(new User("Odin Ugedal5", false, "male", 26));
		users.add(new User("Odin Ugedal4", false, "male", 22));
		users.add(new User("Odin Ugedal6", false, "male", 20));
		users.add(new User("Odin Ugedal7", false, "male", 18));
		users.add(new User("Odin Ugedal8", false, "male", 21));
	}

	public List<User> getAllUsers() {
		return users;
	}

	public void saveUser(User user) {
		users.add(user);
	}
}
