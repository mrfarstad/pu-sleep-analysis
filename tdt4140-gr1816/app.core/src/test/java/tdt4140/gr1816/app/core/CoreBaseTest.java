package tdt4140.gr1816.app.core;

import static org.mockito.Mockito.mock;

public class CoreBaseTest {

  protected DataGetter test = mock(DataGetter.class);

  protected UserDataFetch userDataFetch = new UserDataFetch(test);
  protected String resourceResponsePath = "src/test/resources/tdt4140/gr1816/app/core/";
  protected String resourceQueryPath = "src/main/resources/tdt4140/gr1816/app/core/";

  public User createUser() {
    return new User("5ab24b72c13edf233e48b42d", "user", "user", false, "female", 22, true);
  }

  public User createDoctor() {
    return new User("5ab24b72c13edf233e48b42e", "doctor", "doctor", true, "female", 22, true);
  }
  
}
