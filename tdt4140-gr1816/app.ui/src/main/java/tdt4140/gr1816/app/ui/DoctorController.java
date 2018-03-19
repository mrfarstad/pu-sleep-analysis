package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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


  // Patient tab
  @FXML private ChoiceBox<String> patientChoiceBox;
  private ObservableList<User> accessToPatientList;
  @FXML private ChoiceBox<String> dataChoiceBox;

  @FXML private Button viewGraphButton;

  @FXML private PieChart sleepPieChart;

  @FXML private LineChart<String, Number> pulseLineChart;
  @FXML private CategoryAxis pulseChartXAxis;
  @FXML private NumberAxis pulseChartYAxis;

  @FXML private BarChart<String, Number> stepBarChart;
  @FXML private CategoryAxis stepChartXAxis;
  @FXML private NumberAxis stepChartYAxis;


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


  public void handleViewGraphButton() {
    if (dataChoiceBox.getValue().equals("Sleep")) {
      hideCharts();
      showSleepChart();
    } else if (dataChoiceBox.getValue().equals("Pulse")) {
      hideCharts();
      showPulseChart();
    } else if (dataChoiceBox.getValue().equals("Steps")) {
      hideCharts();
      showStepChart();
    }
  }

  private void showSleepChart() {
    System.out.println("showSleepPieChart");
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Grapefruit", 13),
            new PieChart.Data("Oranges", 25),
            new PieChart.Data("Plums", 10),
            new PieChart.Data("Pears", 22),
            new PieChart.Data("Apples", 30));
    sleepPieChart.getData().clear();
    sleepPieChart.setData(pieChartData);
    sleepPieChart.setVisible(true);
  }

  private void showPulseChart() {
    ObservableList<XYChart.Data<String, Number>> lineChartData =
        FXCollections.observableArrayList(
            new XYChart.Data("1", 90),
            new XYChart.Data("2", 95),
            new XYChart.Data("3", 92),
            new XYChart.Data("4", 101),
            new XYChart.Data("5", 130),
            new XYChart.Data("6", 90),
            new XYChart.Data("7", 90),
            new XYChart.Data("8", 120),
            new XYChart.Data("9", 180));

    XYChart.Series<String, Number> series = new XYChart.Series<>(lineChartData);
    pulseLineChart.getData().clear();
    pulseLineChart.getData().add(series);
    pulseLineChart.setVisible(true);
  }

  private void showStepChart() {
    ObservableList<XYChart.Data<String, Number>> barChartData =
        FXCollections.observableArrayList(
            new XYChart.Data("20.09", 3059),
            new XYChart.Data<>("21.09", 10084),
            new XYChart.Data<>("22.09", 6738),
            new XYChart.Data("23.09", 7559),
            new XYChart.Data<>("24.09", 12084),
            new XYChart.Data<>("25.09", 8738));
    XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
    stepBarChart.getData().clear();
    stepBarChart.getData().add(series);
    stepBarChart.setVisible(true);
  }

  private void hideCharts() {
    sleepPieChart.setVisible(false);
    pulseLineChart.setVisible(false);
    stepBarChart.setVisible(false);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    this.userDataFetch = FxApp.userDataFetch;
    this.user = userDataFetch.getCurrentUser();

    setProfileValues();

    setDataChoiceBox();
    setPatientChoiceBox();
    hideCharts();

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

  public void setPatientChoiceBox() {}

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep");
  }
}
