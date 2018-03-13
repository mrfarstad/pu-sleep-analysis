package tdt4140.gr1816.app.ui;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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

  // Uses username, password, isDoctor, age and gender
  public void handleCreateUser() { 
    UserDataFetch udf = new UserDataFetch(new DataGetter());
    String username = signupUsernameField.getText();
    String password = signupPasswordField.getText();
    boolean isDoctor = isDoctorRadioButton.isPressed();
    String gender;
    if (femaleRadioButton.isPressed()) {
    	gender = "Female";
    } else if (maleRadioButton.isPressed()) {
    	gender = "Male";
    } else {
    	gender = "Other";
    }
    int age = Integer.parseInt(ageField.getText());
    
    
    udf.createUser(username, password, isDoctor, gender , age);
  }
}
