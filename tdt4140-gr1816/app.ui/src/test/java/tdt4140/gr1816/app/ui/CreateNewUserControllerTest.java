package tdt4140.gr1816.app.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.BeforeClass;

public class CreateNewUserControllerTest extends Application {

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
  public void start(Stage primaryStage) throws Exception {
    // TODO Auto-generated method stub

  }
}
