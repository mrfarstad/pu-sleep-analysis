package tdt4140.gr1816.app.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import tdt4140.gr1816.app.core.*;

public class CreateNewUserController {

  @FXML private TextField signupUsernameField;
  @FXML private PasswordField signupPasswordField;
  @FXML private TextField ageField;
  @FXML private RadioButton femaleRadioButton;
  @FXML private RadioButton maleRadioButton;
  @FXML private RadioButton otherGenderRadioButton;
  @FXML private RadioButton isDoctorRadioButton;
  @FXML private Button createUserButton;

  // Uses username, password, isDoctor, age and gender to create a new user using createUser in
  // userDataFetch in FxApp
  public void handleCreateUser() throws IOException {
    String username = signupUsernameField.getText();
    String password = signupPasswordField.getText();
    boolean isDoctor = isDoctorRadioButton.isSelected();
    RadioButton gender = (RadioButton) femaleRadioButton.getToggleGroup().getSelectedToggle();
    int age = Integer.parseInt(ageField.getText());

    Login.userDataFetch.createUser(username, password, isDoctor, gender.getText(), age);

    // Open up login-screen and close this window
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginGUI.fxml"));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage createStage = new Stage();
    createStage.setScene(new Scene(root1));
    createStage.show();
    Window newUserStage = signupUsernameField.getScene().getWindow();
    newUserStage.hide();
  }
}
