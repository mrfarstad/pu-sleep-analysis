package tdt4140.gr1816.app.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.ListViewMatchers;

public class UserGUITest extends ApplicationTest {

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
    Parent root = FXMLLoader.load(getClass().getResource("UserGUI.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void testDataButton() {
    // Check if databutton is on by defult
    Button dataButton = lookup("#dataButton").query();
    assertEquals("Turn off", dataButton.getText());
    // Click button and check of text changes
    clickOn(dataButton);
    assertEquals("Turn on", dataButton.getText());
  }

  @Test
  public void testDoctorRemoval() {
    ListView doctorList = lookup("#doctorsListView").query();
    Button removeDoctorButton = lookup("#removeDoctorButton").query();

    // Check if list has "Doctor 5"
    assertThat(doctorList, ListViewMatchers.hasListCell("Doctor 5"));
    // Remove "Doctor 5"
    moveTo(doctorList);
    press(MouseButton.PRIMARY);
    release(MouseButton.PRIMARY);
    clickOn(removeDoctorButton);

    // Check "Doctor 5" is removed
    // assertThat(doctorList, ListViewMatchers.hasListCell("Doctor 5"));
  }

  @Test
  public void testTabs() {
    clickOn("#sleepTab");
    clickOn("#doctorTab");
    clickOn("#profileTab");
  }
}
