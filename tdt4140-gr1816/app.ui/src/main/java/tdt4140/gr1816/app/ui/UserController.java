package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UserController implements Initializable {

	@FXML
	private Button dataButton;
	
	@FXML
	private ListView<String> doctorsListView;
	
	@FXML
	private TextField doctorTextField;
	
	@FXML
	private Button doctorAddButton;
	
	@FXML
	private Button doctorDeleteButton;
	
	private boolean dataGatheringOn;
	
	final ObservableList<String> doctorsList = FXCollections.observableArrayList("Add Items here");
	
	this.doctorsList.add("Doktor Proktor");


	public void handleDataButton() {
		if (dataGatheringOn) {
			dataButton.setText("Turn on");
			dataGatheringOn = false;
		}
		else {
			dataButton.setText("Turn off");
			dataGatheringOn = true;
		}	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//SET INITIAL VALUE OF DATA BUTTON
		this.dataGatheringOn = true;
		
		if (dataGatheringOn) {
			dataButton.setText("Turn off");
		}
		else {
			dataButton.setText("Turn on");
		}		
		
		//SET LISTVIEW ITEMS
		doctorsListView.setItems(doctorsList);
		
		
	}
}
