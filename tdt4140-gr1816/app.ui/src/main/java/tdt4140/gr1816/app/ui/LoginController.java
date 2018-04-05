package tdt4140.gr1816.app.ui;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import tdt4140.gr1816.app.core.*;

public class LoginController {

  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Button signinButton;
  @FXML private Button createNewUserButton;
  @FXML private Button forgotPasswordButton;
  @FXML private Text forgotPasswordText;

  public void handleSigninButton() throws IOException {
    String file, username, password;
    username = usernameField.getText();
    password = passwordField.getText();
    User loginUser = Login.userDataFetch.signIn(username, password);
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
    Window stage1 = usernameField.getScene().getWindow();
    stage1.hide();
  }

  public void handleForgotPasswordButton() {
    if (usernameField.getText() == "") {
      forgotPasswordText.setText("Please write your username");
    } else if (Login.userDataFetch.getUserByUsername(usernameField.getText()) == null) {
      forgotPasswordText.setText("No user found");
    } else {
      // Login.userDataFetch.forgotPassword(usernameField.getText());
      forgotPasswordText.setText("Password sent");
    }
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Platform.runLater(() -> forgotPasswordText.setText(""));
  }

  public void handleCreateNewUserButton() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CreateNewUserGUI.fxml"));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage createStage = new Stage();
    createStage.setScene(new Scene(root1));
    createStage.show();
    Window loginStage = usernameField.getScene().getWindow();
    loginStage.hide();
  }

  public TextField getUser() {
    return usernameField;
  }

  public PasswordField getPassword() {
    return passwordField;
  }
}
