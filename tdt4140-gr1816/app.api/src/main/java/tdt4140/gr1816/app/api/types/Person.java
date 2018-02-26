package tdt4140.gr1816.app.api.types;

public class Person {

	private int personID;
	private 	String username;
	private String password;
	private boolean isDoctor;
	private String sex;
	private int age;
	
	public Person(int id, String username, String password, boolean isDoctor, String sex, int age) {
		this.personID = id;
		this.username = username;
		this.password = password;
		this.isDoctor = isDoctor;
		this.sex = sex;
		this.age = age;
	}

	public int getPersonID() {
		return personID;
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

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}
	
	
	
	
}
