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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
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

  // Graph Tab
  @FXML private ChoiceBox<String> dataChoiceBox;
  @FXML private DatePicker fromDate;
  @FXML private DatePicker toDate;
  @FXML private Button viewGraphButton;

  @FXML private BarChart<String, Number> stepBarChart;
  @FXML private CategoryAxis stepChartXAxis;
  @FXML private NumberAxis stepChartYAxis;

  @FXML private LineChart<String, Number> pulseLineChart;
  @FXML private CategoryAxis pulseChartXAxis;
  @FXML private NumberAxis pulseChartYAxis;

  @FXML private BarChart<String, Number> sleepBarChart;
  @FXML private CategoryAxis sleepChartXAxis;
  @FXML private NumberAxis sleepChartYAxis;

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
    DataAccessRequest selected = doctorsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      FxApp.userDataFetch.answerDataAccessRequest(selected, "REJECTED");
      doctorsListViewItems.remove(selected);
    }
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

  public void handleViewGraphButton() {
    if (dataChoiceBox.getValue().equals("Steps")) {
      hideCharts();
      showStepChart();
    } else if (dataChoiceBox.getValue().equals("Pulse")) {
      hideCharts();
      showPulseChart();
    } else if (dataChoiceBox.getValue().equals("Sleep - duration")) {
      hideCharts();
      showSleepDChart();
    }
  }

  public void showStepChart() {
    hideCharts();
    stepBarChart.getData().clear();
    stepBarChart.setBarGap(0);
    stepChartXAxis.setLabel("Date");
    stepChartYAxis.setLabel("Steps");
    ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList();
    List<StepsData> stepsDataList = FxApp.userDataFetch.getStepsDataByViewer();
    stepsDataList
        .stream()
        .forEach(
            stepData ->
                barChartData.add(
                    new XYChart.Data<>(stepData.getDate().toString(), stepData.getSteps())));
    XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);

    stepBarChart.getData().add(series);
    stepBarChart.setVisible(true);
  }

  public void showPulseChart() {
    hideCharts();
    pulseLineChart.getData().clear();
    pulseChartXAxis.setLabel("Date");
    pulseChartYAxis.setLabel("RestHR");
    ObservableList<XYChart.Data<String, Number>> lineChartData =
        FXCollections.observableArrayList();
    List<PulseData> pulseDataList = FxApp.userDataFetch.getPulseDataByViewer();
    pulseDataList
        .stream()
        .forEach(
            pulseData ->
                lineChartData.add(
                    new XYChart.Data<>(pulseData.getDate().toString(), pulseData.getRestHr())));
    XYChart.Series<String, Number> series = new XYChart.Series<>(lineChartData);

    pulseLineChart.getData().add(series);
    pulseLineChart.setVisible(true);
  }

  public void showSleepDChart() {
    hideCharts();
    sleepBarChart.getData().clear();
    sleepBarChart.setBarGap(0);
    sleepChartXAxis.setLabel("Date");
    sleepChartYAxis.setLabel("Duration in hours");
    ObservableList<XYChart.Data<String, Number>> sleepBarChartData =
        FXCollections.observableArrayList();
    List<SleepData> sleepDataList = FxApp.userDataFetch.getSleepDataByViewer();
    sleepDataList
        .stream()
        .forEach(
            sleepData ->
                sleepBarChartData.add(
                    new XYChart.Data<>(
                        sleepData.getDate().toString(), sleepData.getDuration() / 60)));
    XYChart.Series<String, Number> series = new XYChart.Series<>(sleepBarChartData);

    sleepBarChart.getData().add(series);
    sleepBarChart.setVisible(true);
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

    setDataChoiceBox();

    hideCharts();
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

  private void hideCharts() {
    stepBarChart.setVisible(false);
    pulseLineChart.setVisible(false);
    sleepBarChart.setVisible(false);
  }

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep - duration");
  }
}
