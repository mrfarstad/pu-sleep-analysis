package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import tdt4140.gr1816.app.core.*;

public class DoctorController implements Initializable {

  @FXML private Button requestButton;

  @FXML private Button showDataButton;

  @FXML private Button showMessageButton;

  @FXML private Text nameText;

  @FXML private Text genderText;

  @FXML private Text ageText;

  @FXML private Text requestFeedbackText;

  @FXML private Tab dataTab;

  @FXML private TabPane tabPane;

  @FXML private Tab messageTab;

  @FXML private Tab profileTab;

  @FXML private Tab patientTab;

  @FXML private TextField requestUserTextField;

  @FXML private ListView<DataAccessRequest> patientListView;

  ObservableList<DataAccessRequest> patientListViewItems;

  private UserDataFetch userDataFetch;
  private User user;

  public void handleRequestButton() {
    String username = requestUserTextField.getText();
    User newPatient = FxApp.userDataFetch.getUserByUsername(username);
    if (newPatient == null) {
      requestFeedbackText.setText("User not found");
    } else if (FxApp.userDataFetch.requestDataAccess(newPatient)) {
      updatePatientListViewItems();
      requestFeedbackText.setText("Request sent");
    } else {
      requestFeedbackText.setText("Request failed");
    }
  }

  public void handleShowDataButton() {
    tabPane.getSelectionModel().select(dataTab);
  }

  public void handleShowMessageButton() {
    tabPane.getSelectionModel().select(messageTab);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    this.userDataFetch = FxApp.userDataFetch;
    this.user = userDataFetch.getCurrentUser();

    setProfileValues();

    updatePatientListViewItems();
  }

  public void setProfileValues() {
    String name = user.getUsername();
    nameText.setText(name);
    String gender = user.getGender();
    genderText.setText(gender);
    String age = Integer.toString(user.getAge());
    ageText.setText(age);
  }

  public void updatePatientListViewItems() {
    patientListViewItems = patientListView.getItems();
    patientListViewItems.clear();
    List<DataAccessRequest> requests = FxApp.userDataFetch.getAccessRequestsByDoctor();
    requests
        .stream()
        .filter(request -> !patientListViewItems.contains(request))
        .forEach(request -> patientListViewItems.add(request));
  }
}
