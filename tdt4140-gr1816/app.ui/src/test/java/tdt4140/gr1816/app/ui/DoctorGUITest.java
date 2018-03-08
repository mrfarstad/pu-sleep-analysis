package tdt4140.gr1816.app.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

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
      System.setProperty("testfx.setup.timeout", "2500");
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("DoctorGUI.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Test
  public void testRequestButton() {

    clickOn("#patientTab");
    clickOn("#searchButton");

    Button requestButton = lookup("#requestButton").query();
    ListView searchListView = lookup("#searchListView").query();
    ObservableList<String> searchListViewItems = searchListView.getItems();
    ListView patientListView = lookup("#patientListView").query();
    ObservableList<String> patientListViewItems = patientListView.getItems();

    String data = searchListViewItems.get(0);

    assertTrue(searchListViewItems.contains(data));
    assertFalse(patientListViewItems.contains(data + " is pending"));
    searchListView.getSelectionModel().select(data);
    clickOn("#requestButton");
    assertFalse(searchListViewItems.contains(data));
    assertTrue(patientListViewItems.contains(data + " is pending"));
  }

  @Test
  public void testShowDataButton() {
    clickOn("#patientTab");
    TabPane tabPane = lookup("#tabPane").query();
    assertEquals(1, tabPane.getSelectionModel().getSelectedIndex());
    clickOn("#showDataButton");
    assertEquals(2, tabPane.getSelectionModel().getSelectedIndex());
  }

  @Test
  public void testShowMessageButton() {
    clickOn("#patientTab");
    TabPane tabPane = lookup("#tabPane").query();
    assertEquals(1, tabPane.getSelectionModel().getSelectedIndex());
    clickOn("#showMessageButton");
    assertEquals(3, tabPane.getSelectionModel().getSelectedIndex());
  }
}
