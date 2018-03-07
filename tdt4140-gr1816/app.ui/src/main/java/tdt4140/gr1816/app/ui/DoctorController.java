package tdt4140.gr1816.app.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DoctorController {

	@FXML
	private Button requestButton;
	
	@FXML
	private TextField requestTextField;
	
	public void handleRequestButton() {
		System.out.println("Data requested");
		
		System.out.println(requestTextField.getText());
		requestTextField.setText("HALLO");
		
	}
}