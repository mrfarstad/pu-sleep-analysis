package tdt4140.gr1816.app.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1816.app.core.*;

public class DoctorGUITest extends ApplicationTest {

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

  @Test
  public void testLoginDoctor() {
    String username = "legesen";
    String password = "test";
    User doctorSample = new User("ID", username, password, true, "male", 43);

    when(FxApp.userDataFetch.signIn(username, password)).thenReturn(doctorSample);
    when(FxApp.userDataFetch.getCurrentUser()).thenReturn(doctorSample);

    User testUser = FxApp.userDataFetch.signIn(username, password);
    assertTrue(testUser instanceof User);

    TextField usernameField = lookup("#usernameField").query();
    clickOn(usernameField);
    write(username);
    PasswordField passwordField = lookup("#passwordField").query();
    clickOn(passwordField);
    write(password);
    clickOn("#signinButton");

    testRequestButton();
    testShowDataButton();
    testShowMessageButton();
  }

  public void testRequestButton() {

    clickOn("#patientTab");
    TextField requestUserTextField = lookup("#requestUserTextField").query();
    Button requestButton = lookup("#requestButton").query();
    ListView patientListView = lookup("#patientListView").query();
    ObservableList<String> patientListViewItems = patientListView.getItems();
    
    clickOn(requestUserTextField);
    write("thisIsNoUser");
    clickOn(requestButton);
    Text requestFeedbackText = lookup("#requestFeedbackText").query();
    assertEquals("User not found", requestFeedbackText.getText());
  }

  public void testShowDataButton() {
    clickOn("#patientTab");
    TabPane tabPane = lookup("#tabPane").query();
    assertEquals(1, tabPane.getSelectionModel().getSelectedIndex());
    clickOn("#showDataButton");
    assertEquals(2, tabPane.getSelectionModel().getSelectedIndex());
  }

  public void testShowMessageButton() {
    clickOn("#patientTab");
    TabPane tabPane = lookup("#tabPane").query();
    assertEquals(1, tabPane.getSelectionModel().getSelectedIndex());
    clickOn("#showMessageButton");
    assertEquals(3, tabPane.getSelectionModel().getSelectedIndex());
  }
}
