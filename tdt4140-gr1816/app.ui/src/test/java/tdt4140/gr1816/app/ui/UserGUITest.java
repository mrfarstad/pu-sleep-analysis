package tdt4140.gr1816.app.ui;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import tdt4140.gr1816.app.core.*;

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
  public void signIn() {
    String username = "testUser";
    String password = "test";
    User userSample = new User("userID", username, password, false, "male", 22);

    when(FxApp.userDataFetch.signIn(username, password)).thenReturn(userSample);
    when(FxApp.userDataFetch.getCurrentUser()).thenReturn(userSample);

    User testUser = FxApp.userDataFetch.signIn(username, password);
    assertTrue(testUser instanceof User);

    TextField usernameField = lookup("#usernameField").query();
    clickOn(usernameField);
    write(username);
    PasswordField passwordField = lookup("#passwordField").query();
    clickOn(passwordField);
    write(password);
    clickOn("#signinButton");

    testDataButton();
    testAcceptAndRemoveDoctor();
  }

  public void testDataButton() {
    clickOn("#profileTab");
    Button dataButton = lookup("#dataButton").query();

    assertEquals("Turn off", dataButton.getText());
    clickOn(dataButton);
    assertEquals("Turn on", dataButton.getText());
    clickOn(dataButton);
    assertEquals("Turn off", dataButton.getText());
  }

  public void testAcceptAndRemoveDoctor() {
    clickOn("#doctorTab");

    ListView doctorRequestList = lookup("#doctorRequestListView").query();
    ObservableList<DataAccessRequest> doctorRequestItems = doctorRequestList.getItems();

    User doctorSample = new User("doctorID", "Doctor", "test", true, "female", 34);
    DataAccessRequest request =
        new DataAccessRequest(
            "requestID", FxApp.userDataFetch.getCurrentUser(), doctorSample, "PENDING");

    doctorRequestItems.add(request);

    assertTrue(doctorRequestItems.contains(request));
    doctorRequestList.getSelectionModel().select(request);
    assertEquals(request.getStatusAsString(), "PENDING");
    clickOn("#acceptDoctorButton");
    assertFalse(doctorRequestItems.contains(request));

    clickOn("#profileTab");
    DataAccessRequest accRequest =
        new DataAccessRequest(
            "requestID", FxApp.userDataFetch.getCurrentUser(), doctorSample, "ACCEPTED");
    ListView doctorList = lookup("#doctorsListView").query();
    ObservableList<DataAccessRequest> doctorItems = doctorList.getItems();
    doctorItems.add(accRequest);

    assertTrue(doctorItems.contains(accRequest));
    doctorList.getSelectionModel().select(accRequest);
    assertEquals(accRequest.getStatusAsString(), "ACCEPTED");

    clickOn("#removeDoctorButton");
    assertFalse(doctorItems.contains(accRequest));
  }

  // TODO
  public void testDeleteDataButton() {
    clickOn("#sleepTab");
    Button deleteButton = lookup("#deleteDataButton").query();
    ListView dataList = lookup("#dataListView").query();
    ObservableList<String> dataItems = dataList.getItems();
  }
}
