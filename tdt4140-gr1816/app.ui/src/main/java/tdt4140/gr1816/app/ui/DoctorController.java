package tdt4140.gr1816.app.ui;

import java.net.URL;
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
import javafx.scene.chart.XYChart.Data;
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
  @FXML private NumberAxis pulseChartYAxis;
  
  @FXML private BarChart<String, Number> stepBarChart;
  @FXML private CategoryAxis stepChartXAxis;
  @FXML private NumberAxis stepChartYAxis;
  

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
	ObservableList<XYChart.Data<String, Number>> lineChartData = FXCollections.observableArrayList( 
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
	 ObservableList<XYChart.Data<String, Number>> barChartData = FXCollections.observableArrayList(
			 new XYChart.Data("20.09", 3059),
			 new XYChart.Data<>("21.09", 10084),
			 new XYChart.Data<>("22.09", 6738),
			 new XYChart.Data("23.09", 7559),
			 new XYChart.Data<>("24.09", 12084),
			 new XYChart.Data<>("25.09", 8738)
			 );
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
