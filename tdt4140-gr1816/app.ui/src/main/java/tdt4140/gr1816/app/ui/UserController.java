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
import tdt4140.gr1816.app.core.UserDataFetch;

public class UserController implements Initializable {

	@FXML
	private Button dataButton;
	
	@FXML
	private Button acceptDoctorButton;
	
	@FXML
	private Button removeDoctorButton;
	
	@FXML
	private ListView<String> doctorsListView;
	
	
	private boolean dataGatheringOn;
	ObservableList<String> listViewItems;

	public void handleDataButton() {
		if (dataGatheringOn) {
			dataButton.setText("Turn on");
			dataGatheringOn = false;
			turnOffDataGathering();
			List<User> users = UserDataFetch.getAllUsers();
		}
		else {
			dataButton.setText("Turn off");
			dataGatheringOn = true;
			turnOnDataGathering();
		}	
	}
	
	public void handleRemoveDoctorButton() {
		listViewItems.remove(doctorsListView.getSelectionModel().getSelectedItem());
	}
	
	public void handleAcceptDoctorButton() {
		acceptDoctorButton.setVisible(false);
	}
	
	public void turnOffDataGathering() {
		//CODE TO TURN OFF DATA GATHERING
	}
	
	public void turnOnDataGathering() {
		//CODE TO TURN ON DATA GATHERING
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
		listViewItems = doctorsListView.getItems();
		listViewItems.add("Doctor 1");
		listViewItems.add("Doctor 2");
		listViewItems.add("Doctor 3");
		listViewItems.add("Doctor 4");
		
		
	}
}
