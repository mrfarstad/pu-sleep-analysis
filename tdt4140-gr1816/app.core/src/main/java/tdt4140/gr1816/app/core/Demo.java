package tdt4140.gr1816.app.core;

public class Demo {
  public static void main(String[] args) {
    UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());
    System.out.println(userDataFetch.getAllUsers());
    System.out.println(userDataFetch.createUser("test", "test", true, "male", 22));
    System.out.println(userDataFetch.createUser("doctor", "test", true, "male", 22));
    System.out.println(userDataFetch.getAllUsers());
    User test = userDataFetch.signIn("test", "test");
    userDataFetch.signIn("doctor", "test");
    boolean succ = userDataFetch.requestDataAccess(test);
    System.out.println(succ);
    System.out.println(userDataFetch.getCurrentUser());
    System.out.println(userDataFetch.getAccessRequestsToUser());
    System.out.println(userDataFetch.getAccessRequestsByDoctor());
    userDataFetch.deleteUser("test", "test");
    userDataFetch.deleteUser("doctor", "test");
  }
}
