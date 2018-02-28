package tdt4140.gr1816.app.api.types;

public class User {

	private String id;
	private String username;
	private String password;
	private boolean isDoctor;
	private String gender;
	private int age;
	
	public User(String string, String username, String password, boolean isDoctor, String gender, int age) {
		this.id = string;
		this.username = username;
		this.password = password;
		this.isDoctor = isDoctor;
		this.gender = gender;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isDoctor() {
		return isDoctor;
	}

	public String getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}
	
}
