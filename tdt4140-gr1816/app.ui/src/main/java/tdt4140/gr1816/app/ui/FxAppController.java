package tdt4140.gr1816.app.ui;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FxAppController {
	
	@FXML
	private TextField usernameField;
	private PasswordField passwordField;
	
	public void handleLogin() {
		// if usernameField.isDoctor()
		// finner ut om bruker er lege
		// if setning som åpner riktig vindu
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
