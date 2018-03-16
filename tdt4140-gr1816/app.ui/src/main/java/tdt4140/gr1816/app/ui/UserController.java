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
import tdt4140.gr1816.app.core.*;

public class UserController implements Initializable {

  @FXML private Button dataButton;

  @FXML private Button acceptDoctorButton;

  @FXML private Button removeDoctorButton;

  @FXML private Button deleteDataButton;

  @FXML private Text nameText;

  @FXML private Text ageText;

  @FXML private Text genderText;

  @FXML private ListView<DataAccessRequest> doctorsListView;

  @FXML private ListView<String> dataListView;

  @FXML private ListView<DataAccessRequest> doctorRequestListView;

  private User user;
  private UserDataFetch userDataFetch;

  private boolean dataGatheringOn;
  ObservableList<String> dataListViewItems;
  ObservableList<DataAccessRequest> doctorsListViewItems;
  ObservableList<DataAccessRequest> doctorRequestListViewItems;

  public void handleDataButton() {
    if (dataGatheringOn) {
      dataButton.setText("Turn on");
      dataGatheringOn = false;
      turnOffDataGathering();
    } else {
      dataButton.setText("Turn off");
      dataGatheringOn = true;
      turnOnDataGathering();
    }
  }

  public void handleDeleteDataButton() {
    dataListViewItems.remove(dataListView.getSelectionModel().getSelectedItem());
  }

  public void handleRemoveDoctorButton() {
    doctorsListViewItems.remove(doctorsListView.getSelectionModel().getSelectedItem());
  }

  public void handleAcceptDoctorButton() {
    DataAccessRequest selected = doctorRequestListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      FxApp.userDataFetch.answerDataAccessRequest(selected, "ACCEPTED");
      doctorRequestListViewItems.remove(selected);
      updateDoctorsListViewItems();
    }
  }

  public void handleRejectDoctorButton() {
    DataAccessRequest selected = doctorRequestListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      FxApp.userDataFetch.answerDataAccessRequest(selected, "REJECTED");
      doctorRequestListViewItems.remove(selected);
      updateDoctorsListViewItems();
    }
  }

  public void turnOffDataGathering() {
    // CODE TO TURN OFF DATA GATHERING
  }

  public void turnOnDataGathering() {
    // CODE TO TURN ON DATA GATHERING
  }

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {

    this.userDataFetch = FxApp.userDataFetch;
    this.user = userDataFetch.getCurrentUser();

    setProfileValues();

    setInitialDataButtonValue();

    setDataListViewItems();

    updateDoctorsListViewItems();

    updateDoctorRequestListViewItems();
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
    dataGatheringOn = true;

    if (dataGatheringOn) {
      dataButton.setText("Turn off");
    } else {
      dataButton.setText("Turn on");
    }
  }

  public void setDataListViewItems() {
    dataListViewItems = dataListView.getItems();
    dataListViewItems.add("Data 1");
    dataListViewItems.add("Data 2");
    dataListViewItems.add("Data 3");
  }

  public void updateDoctorsListViewItems() {
    doctorsListViewItems = doctorsListView.getItems();
    doctorsListViewItems.clear();
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    requests
        .stream()
        .filter(request -> request.getStatusAsString().equals("ACCEPTED"))
        .forEach(request -> doctorsListViewItems.add(request));
  }

  public void updateDoctorRequestListViewItems() {
    doctorRequestListViewItems = doctorRequestListView.getItems();
    doctorRequestListViewItems.clear();
    List<DataAccessRequest> requests = this.userDataFetch.getAccessRequestsToUser();
    requests
        .stream()
        .filter(request -> request.getStatusAsString().equals("PENDING"))
        .forEach(request -> doctorRequestListViewItems.add(request));
  }
}
