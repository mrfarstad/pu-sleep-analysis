package tdt4140.gr1816.app.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FxAppController {

  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Button signinButton;
  @FXML private Button createNewUserButton;

  public void handleSigninButton() throws IOException {
    String file;
    System.out.println(usernameField.getText());
    if (usernameField.getText().equals("user")) {
      file = "UserGUI.fxml";
    } else {
      file = "DoctorGUI.fxml";
    }
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(file));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));
    stage.show();
    Window stage1 = usernameField.getScene().getWindow();
    stage1.hide();
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
