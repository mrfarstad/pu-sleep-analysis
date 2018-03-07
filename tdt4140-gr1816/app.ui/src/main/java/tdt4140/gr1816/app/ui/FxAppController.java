package tdt4140.gr1816.app.ui;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class FxAppController {
	
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button signinButton;
	@FXML
	private Button createNewButton;
	
	
	public void handleSigninButton() {
		// if (doctor) FxApp.showDocterScene() 
		// else  FxApp.showPatientScene()
	}
	public void handleCreateNewButton() throws IOException {
		FxApp.showCreateNewUserScene();
	}
	public TextField getUser() {
		return usernameField;
	}
	
	public PasswordField getPassword() {
		return passwordField;
	}
}
