package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import tdt4140.gr1816.app.core.DataAccessRequest;
import tdt4140.gr1816.app.core.User;
import tdt4140.gr1816.app.core.UserDataFetch;

public class UserController implements Initializable {

	@FXML
	private Button dataButton;
	
	@FXML
	private Button acceptDoctorButton;
	
	@FXML
	private Button removeDoctorButton;
	
	@FXML
	private Text nameText;
	
	@FXML
	private Text ageText;
	
	@FXML
	private Text genderText;
	
	@FXML
	private ListView<String> doctorsListView;
	
	@FXML
	private ListView<String> doctorRequestListView;
	
	private User user;
	private UserDataFetch userDataFetch;
	
	
	private List<DataAccessRequest> accessRequestList;
	
	private boolean dataGatheringOn;
	ObservableList<String> doctorsListViewItems;
	ObservableList<String> doctorRequestListViewItems;

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
		doctorsListViewItems.remove(doctorsListView.getSelectionModel().getSelectedItem());
	}
	
	public void handleAcceptDoctorButton() {
		doctorsListViewItems.add(doctorRequestListView.getSelectionModel().getSelectedItem());
		doctorRequestListViewItems.remove(doctorRequestListView.getSelectionModel().getSelectedItem());
	}
	
	public void turnOffDataGathering() {
		//CODE TO TURN OFF DATA GATHERING
	}
	
	public void turnOnDataGathering() {
		//CODE TO TURN ON DATA GATHERING
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		UserDataFetch.signIn();
		user = UserDataFetch.getCurrentUser();
		accessRequestList = UserDataFetch.getAccessRequestsToUser();
		
		setProfileValues();
		
		setInitialDataButtonValue();
			
		setDoctorsListViewItems();
		
		setDoctorRequestListViewItems();
		
	}
	
	public void setProfileValues() {
		
		String name = user.getUsername();
		Integer age = user.getAge();
		String gender = user.getGender();
		
		nameText.setText(name);
		ageText.setText(age.toString());
		genderText.setText(gender);
				
	}
	
	public void setInitialDataButtonValue() {
		this.dataGatheringOn = true;
		
		if (dataGatheringOn) {
			dataButton.setText("Turn off");
		}
		else {
			dataButton.setText("Turn on");
		}	
	}
	
	public void setListViews() {
		List<DataAccessRequest> dataAccessRequest = UserDataFetch.getAccessRequestsToUser();
	}
	
	public void setDoctorsListViewItems() {
		doctorsListViewItems = doctorsListView.getItems();
		doctorsListViewItems.add("Doctor 1");
		doctorsListViewItems.add("Doctor 2");
		doctorsListViewItems.add("Doctor 3");
		doctorsListViewItems.add("Doctor 4");
	}
	
	public void setDoctorRequestListViewItems() {
		doctorRequestListViewItems = doctorRequestListView.getItems();
		doctorRequestListViewItems.add("Doctor 5");
		doctorRequestListViewItems.add("Doctor 6");
		doctorRequestListViewItems.add("Doctor 7");
	}
}
