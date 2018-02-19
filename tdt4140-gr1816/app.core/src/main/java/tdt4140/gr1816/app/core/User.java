package tdt4140.gr1816.app.core;

public class User {

	private final String name;
	private final boolean isDoctor;
	private final String gender;
	private final int age;

	public User(String name, boolean isDoctor, String gender, int age) {
		this.name = name;
		this.isDoctor = isDoctor;
		this.gender = gender;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public boolean getIsDoctor() {

		return isDoctor;
	}

	public String getGender() {
		return gender;
	}

	public int getAge() {
		if (age % 2 == 0)
			return -1;
		return age;
	}

}
