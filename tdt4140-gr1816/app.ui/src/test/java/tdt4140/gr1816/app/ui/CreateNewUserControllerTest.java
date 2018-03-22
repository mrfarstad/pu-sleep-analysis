package tdt4140.gr1816.app.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1816.app.core.User;
import tdt4140.gr1816.app.core.UserDataFetch;

public class CreateNewUserControllerTest extends ApplicationTest {

  @BeforeClass
  public static void headless() {
    if (Boolean.valueOf(System.getProperty("gitlab-ci", "false"))) {
      System.setProperty("prism.verbose", "true"); // optional
      System.setProperty("java.awt.headless", "true");
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("glass.platform", "Monocle");
      System.setProperty("monocle.platform", "Headless");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    FxApp.userDataFetch = mock(UserDataFetch.class);
    Parent root = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Ignore
  @Test
  public void testCreateNewUser() {
    clickOn("#createNewUserButton");

    String username = "mathiaswahl";
    String password = "passord123";
    String age = "22";

    User userSample = new User("userID", username, password, false, "male", Integer.parseInt(age));
    when(FxApp.userDataFetch.createUser(username, password, false, "Male", Integer.parseInt(age)))
        .thenReturn(userSample);

    TextField signupUsernameField = lookup("#signupUsernameField").query();
    PasswordField signupPasswordField = lookup("#signupPasswordField").query();
    TextField ageField = lookup("#ageField").query();
    RadioButton isDoctor = lookup("#isDoctorRadioButton").query();
    RadioButton genderButton = lookup("#femaleRadioButton").query();

    clickOn(signupUsernameField);
    write(username);
    clickOn(signupPasswordField);
    write(password);
    clickOn(ageField);
    write(age);
    clickOn("#femaleRadioButton");
    clickOn("#otherGenderRadioButton");
    clickOn("#maleRadioButton");
    clickOn(isDoctor);
    clickOn(isDoctor);

    RadioButton gender = (RadioButton) genderButton.getToggleGroup().getSelectedToggle();
    User newUser =
        FxApp.userDataFetch.createUser(
            signupUsernameField.getText(),
            signupPasswordField.getText(),
            isDoctor.isSelected(),
            gender.getText(),
            Integer.parseInt(ageField.getText()));

    assertTrue(newUser instanceof User);
    assertEquals(newUser, userSample);

    clickOn("#createUserButton");
  }
}
