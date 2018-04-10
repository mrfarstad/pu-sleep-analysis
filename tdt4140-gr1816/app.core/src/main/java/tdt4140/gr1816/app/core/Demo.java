package tdt4140.gr1816.app.core;

public class Demo {
  public static void main(String[] args) {
    UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());
    System.out.println("All users: " + userDataFetch.getAllUsers());
    System.out.println(
        "Create user: " + userDataFetch.createUser("test", "test", true, "male", 22).getUsername());
    System.out.println(
        "Create user: "
            + userDataFetch.createUser("doctor", "test", true, "male", 22).getUsername());
    System.out.println("All users: " + userDataFetch.getAllUsers());
    User test = userDataFetch.signIn("test", "test");
    System.out.println(
        "Logged in: " + userDataFetch.getCurrentUser() + ". Creating sleepData entries.");
    SleepData data = userDataFetch.createSleepData("2018-07-15", 480, 10);
    SleepData data2 = userDataFetch.createSleepData("2018-07-16", 490, 11);
    System.out.println(
        "Entries, duration: "
            + data.getDuration()
            + " minutes, and "
            + data2.getDuration()
            + " minutes.");
    /*User doctor = userDataFetch.signIn("doctor", "test");
    DataAccessRequest request = userDataFetch.requestDataAccess(test);
    System.out.println(request.getDataOwner());
    System.out.println(request.getId());
    userDataFetch.signIn("test", "test");
    boolean success = userDataFetch.answerDataAccessRequest(request, "ACCEPTED");
    System.out.println(success);
    System.out.println(userDataFetch.getCurrentUser());
    System.out.println(userDataFetch.getAccessRequestsToUser());
    System.out.println(userDataFetch.getAccessRequestsByDoctor());
    userDataFetch.signIn("test", "test");*/
    System.out.println("Deleting users (And data):");
    System.out.println(userDataFetch.deleteUser("test", "test", false));
    userDataFetch.signIn("doctor", "test");
    System.out.println(userDataFetch.deleteUser("doctor", "test", true));
    System.out.println("All users: " + userDataFetch.getAllUsers());
  }
}
