package tdt4140.gr1816.app.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

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
      System.setProperty("testfx.setup.timeout", "2500");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("CreateNewUserGUI.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void testSignupPasswordField() {
    PasswordField signupPasswordField = lookup("#signupPasswordField").query();
    clickOn(signupPasswordField);
    write("passord123");
    assertEquals("passord123", signupPasswordField.getText());
  }

  @Test
  public void testSignupUsernameField() {
    TextField signupUsernameField = lookup("#signupUsernameField").query();
    clickOn(signupUsernameField);
    write("mathiaswahl");
    assertEquals("mathiaswahl", signupUsernameField.getText());
  }

  @Test
  public void testFemaleSelected() {
    RadioButton female = lookup("#femaleRadioButton").query();
    clickOn(female);
    assertTrue(female.isSelected());
  }

  @Test
  public void testMaleSelected() {
    RadioButton female = lookup("#femaleRadioButton").query();
    RadioButton male = lookup("#maleRadioButton").query();
    clickOn(female);
    clickOn(male);
    assertTrue(male.isSelected());
  }
}
