package tdt4140.gr1816.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tdt4140.gr1816.app.core.*;

public class SleepAnalysis extends Application {

  protected static UserDataFetch userDataFetch;

  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    userDataFetch = new UserDataFetch(new DataGetter());
    launch(args);
  }
}
