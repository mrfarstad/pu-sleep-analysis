package tdt4140.gr1816.app.ui;

import java.net.URL;
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

public class DoctorController implements Initializable {

  @FXML private Button requestButton;

  @FXML private Button showDataButton;

  @FXML private Button showMessageButton;

  @FXML private Button searchButton;

  @FXML private Text nameText;

  @FXML private Tab dataTab;

  @FXML private TabPane tabPane;

  @FXML private Tab messageTab;
  
  @FXML private Tab profileTab;
  
  @FXML private Tab patientTab;

  @FXML private TextField requestTextField;

  @FXML private ListView<String> patientListView;

  @FXML private ListView<String> searchListView;

  ObservableList<String> patientListViewItems;
  ObservableList<String> searchListViewItems;

  public void handleRequestButton() {
    String selected = searchListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      patientListViewItems.add(selected + " is pending");
      searchListViewItems.remove(selected);
    }
  }

  public void handleShowDataButton() {
    tabPane.getSelectionModel().select(dataTab);
  }

  public void handleShowMessageButton() {
    tabPane.getSelectionModel().select(messageTab);
  }

  public void handleSearchButton() {
    searchListViewItems = searchListView.getItems();
    searchListViewItems.add("Patient 4");
    searchListViewItems.add("Patient 5");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setProfileValues();

    setPatientListViewItems();
  }

  public void setProfileValues() {
    String name = "Lege Legesen";
    nameText.setText(name);
  }

  public void setPatientListViewItems() {
    patientListViewItems = patientListView.getItems();
    patientListViewItems.add("Patient 1");
    patientListViewItems.add("Patient 2");
    patientListViewItems.add("Patient 3");
  }
}
