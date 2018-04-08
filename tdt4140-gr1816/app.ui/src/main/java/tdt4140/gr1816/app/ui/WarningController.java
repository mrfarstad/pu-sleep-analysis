package tdt4140.gr1816.app.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import tdt4140.gr1816.app.core.User;
import tdt4140.gr1816.app.core.UserDataFetch;

public class WarningController implements Initializable {

  @FXML private Button yesButton;

  @FXML private Button noButton;

  @FXML private Button sceneHolder;

  private User user;
  private UserDataFetch userDataFetch;

  private void returnToLoginScreen(Button sceneHolder) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginGUI.fxml"));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));
    stage.show();
    Window stage1 = yesButton.getScene().getWindow();
    stage1.hide();
  }

  public void handleYesButton() throws Exception {
    // ISSUE?: The UI does not have access to the current users password. Fix: Alloctate password in
    // LogIn method,
    // or ask to confirm password here once more. Otherwise, everything justwerkz.

    System.out.println(userDataFetch.getCurrentUser().getPassword());
    // THIS PRINTS NULL

    userDataFetch.deleteUser(user.getUsername(), "test");
    returnToLoginScreen(yesButton);
  }

  public void handleNoButton() throws IOException {
    String file;
    User loginUser = Login.userDataFetch.getCurrentUser();
    if (loginUser.isDoctor()) {
      file = "DoctorGUI.fxml";
    } else if (!loginUser.isDoctor()) {
      file = "UserGUI.fxml";
    } else {
      throw new IllegalArgumentException();
    }
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));
    stage.show();
    Window stage1 = noButton.getScene().getWindow();
    stage1.hide();
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {

    this.userDataFetch = Login.userDataFetch;
    this.user = userDataFetch.getCurrentUser();
  }
}
