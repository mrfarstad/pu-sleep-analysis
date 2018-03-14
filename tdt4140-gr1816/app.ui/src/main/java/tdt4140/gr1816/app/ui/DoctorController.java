package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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

  // Patient tab
  @FXML private ChoiceBox<String> dataChoiceBox;

  @FXML private Button viewGraphButton;

  @FXML private PieChart sleepPieChart;

  @FXML private LineChart<String, Number> pulseLineChart;

  @FXML private CategoryAxis pulseChartXAxis;

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

  public void handleViewGraphButton() {
    if (dataChoiceBox.getValue().equals("Sleep")) {
      hideCharts();
      showSleepChart();
    } else if (dataChoiceBox.getValue().equals("Pulse")) {
      hideCharts();
      showPulseChart();
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
    sleepPieChart.setData(pieChartData);
    sleepPieChart.setVisible(true);
  }

  private void showPulseChart() {
    XYChart.Series series = new XYChart.Series();
    series.setName("My Test Data");
    series.getData().add(new XYChart.Data("1", 90));
    series.getData().add(new XYChart.Data("2", 95));
    series.getData().add(new XYChart.Data("3", 92));
    series.getData().add(new XYChart.Data("4", 101));
    series.getData().add(new XYChart.Data("5", 130));
    series.getData().add(new XYChart.Data("6", 90));
    series.getData().add(new XYChart.Data("7", 90));
    series.getData().add(new XYChart.Data("8", 120));
    series.getData().add(new XYChart.Data("9", 180));

    pulseLineChart.getData().add(series);
    pulseLineChart.setVisible(true);
  }

  private void hideCharts() {
    sleepPieChart.setVisible(false);
    pulseLineChart.setVisible(false);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setProfileValues();
    setPatientListViewItems();
    setDataChoiceBox();
    hideCharts();
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

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep");
  }
}
