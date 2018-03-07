package tdt4140.gr1816.app.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
  public void testDoctorGUI() {}
}
