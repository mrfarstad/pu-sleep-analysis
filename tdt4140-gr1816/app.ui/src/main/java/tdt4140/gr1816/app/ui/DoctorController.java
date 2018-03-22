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
import javafx.scene.control.DatePicker;
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

  // Patient data tab
  @FXML private ChoiceBox<User> patientChoiceBox;
  private ObservableList<User> acceptedPatientList = FXCollections.observableArrayList();
  @FXML private ChoiceBox<String> dataChoiceBox;
  @FXML private Text patientTF;
  @FXML private Button viewGraphButton;
  @FXML private DatePicker fromDate;
  @FXML private DatePicker toDate;

  @FXML private BarChart<String, Number> sleepBarChart;
  @FXML private CategoryAxis sleepChartXAxis;
  @FXML private NumberAxis sleepChartYAxis;

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
    User newPatient = userDataFetch.getUserByUsername(username);
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
      // showSleepPieChart();
      showSleepBarChart();
    } else if (dataChoiceBox.getValue().equals("Pulse")) {
      hideCharts();
      showPulseChart();
    } else if (dataChoiceBox.getValue().equals("Steps")) {
      hideCharts();
      showStepChart();
    }
  }

  private void showSleepBarChart() {
    hideCharts();
    User user = getSelectedPatientCB();
    sleepBarChart.setBarGap(0);
    sleepChartXAxis.setLabel("Date");
    sleepChartYAxis.setLabel("Duration in hours");
    ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();
    sleepBarChart.getData().clear();
    List<SleepData> sleepDataList = userDataFetch.getAllSleepData(user.getId());
    sleepDataList
        .stream()
        .forEach(
            sleepData ->
                barChartData.add(
                    new XYChart.Data<>(
                        sleepData.getDate().toString(), sleepData.getDuration() / 60)));
    XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
    sleepBarChart.getData().clear();
    sleepBarChart.getData().add(series);
    sleepBarChart.setVisible(true);
  }
  // Piechart
  /*
    private void showSleepPieChart() {
  	User user = getSelectedPatientCB();
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
  */
  private void showPulseChart() {
    User user = getSelectedPatientCB();
    pulseChartXAxis.setLabel("Date");
    pulseChartYAxis.setLabel("Pulse, restHR");
    ObservableList<XYChart.Data<String, Number>> lineChartData =
        FXCollections.observableArrayList();
    List<PulseData> pulseDataList = userDataFetch.getAllPulseData(user.getId());
    pulseDataList
        .stream()
        .forEach(
            pulseData ->
                lineChartData.add(
                    new XYChart.Data<>(pulseData.getDate().toString(), pulseData.getRestHr())));

    XYChart.Series<String, Number> series = new XYChart.Series<>(lineChartData);
    pulseLineChart.getData().clear();
    pulseLineChart.getData().add(series);
    pulseLineChart.setVisible(true);
  }

  private void showStepChart() {
    User user = getSelectedPatientCB();
    stepBarChart.setBarGap(0);
    stepChartXAxis.setLabel("Date");
    stepChartYAxis.setLabel("Steps");
    ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();
    List<StepsData> stepDataList = userDataFetch.getAllStepsData(user.getId());
    stepDataList
        .stream()
        .forEach(
            stepData ->
                barChartData.add(
                    new XYChart.Data<>(stepData.getDate().toString(), stepData.getSteps())));
    XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
    stepBarChart.getData().clear();
    stepBarChart.getData().add(series);
    stepBarChart.setVisible(true);
  }

  private void hideCharts() {
    sleepPieChart.setVisible(false);
    pulseLineChart.setVisible(false);
    stepBarChart.setVisible(false);
    sleepBarChart.setVisible(false);
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
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    requests
        .stream()
        .filter(request -> !patientListViewItems.contains(request))
        .forEach(request -> patientListViewItems.add(request));
  }

  public void setPatientChoiceBox() {
    acceptedPatientList.clear();
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    requests
        .stream()
        .filter(request -> !acceptedPatientList.contains(request.getDataOwner()))
        .filter(request -> request.getStatusAsString().equals("ACCEPTED"))
        .forEach(request -> acceptedPatientList.add(request.getDataOwner()));
    patientChoiceBox.setItems(acceptedPatientList);
  }

  public User getSelectedPatientCB() {
    User user = patientChoiceBox.getValue();
    patientTF.setText(user.getUsername() + ".");
    return user;
  }

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep");
  }
}
