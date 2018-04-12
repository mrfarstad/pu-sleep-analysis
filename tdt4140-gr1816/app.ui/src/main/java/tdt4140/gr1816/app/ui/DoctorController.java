package tdt4140.gr1816.app.ui;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import tdt4140.gr1816.app.core.*;

public class DoctorController implements Initializable {
  @FXML private Button logOutButton;

  @FXML private Button deleteUserButton;

  @FXML private Button requestButton;

  @FXML private Button showDataButton;

  @FXML private Button showMessageButton;

  @FXML private Button sendMessageButton;

  @FXML private Text nameText;

  @FXML private Text genderText;

  @FXML private Text ageText;

  @FXML private Text requestFeedbackText;

  @FXML private Text subjectText;

  @FXML private Text toText;

  @FXML private Text fromText;

  @FXML private Text dateText;

  @FXML private TextField subjectTextField;

  @FXML private TextArea sendMessageTextArea;

  @FXML private TextArea messageTextArea;

  @FXML private ChoiceBox<User> toChoiceBox;

  @FXML private Text sentText;

  @FXML private Tab dataTab;

  @FXML private TabPane tabPane;

  @FXML private Tab messageTab;

  @FXML private Tab profileTab;

  @FXML private Tab patientTab;

  @FXML private TextField requestUserTextField;

  @FXML private ListView<DataAccessRequest> patientListView;

  @FXML private ListView<Message> messagesListView;

  ObservableList<DataAccessRequest> patientListViewItems;
  ObservableList<Message> messagesListViewItems;

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

  @FXML private Text groupAverageNumberText;
  @FXML private Text groupAverageText;
  @FXML private Text pasientAverageNumberText;
  @FXML private Text pasientAverageText;

  private UserDataFetch userDataFetch;
  private User user;

  private void returnToLoginScreen(Button sceneHolder) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginGUI.fxml"));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));
    stage.show();
    Window stage1 = sceneHolder.getScene().getWindow();
    stage1.hide();
  }

  public void handleLogOutButton() throws Exception {
    user = null;
    userDataFetch.logOut();
    returnToLoginScreen(logOutButton);
  }

  public void handleDeleteUserButton() throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WarningGUI.fxml"));
    Parent root1 = (Parent) fxmlLoader.load();
    Stage stage = new Stage();
    stage.setScene(new Scene(root1));
    stage.show();
    Window stage1 = deleteUserButton.getScene().getWindow();
    stage1.hide();
  }

  public void handleRequestButton() {
    String username = requestUserTextField.getText();
    User newPatient = userDataFetch.getUserByUsername(username);
    PauseTransition pause = new PauseTransition(Duration.seconds(4));
    pause.setOnFinished(event -> requestFeedbackText.setText(""));
    if (newPatient == null) {
      requestFeedbackText.setText("User not found");
    } else if (Login.userDataFetch.requestDataAccess(newPatient)) {
      updatePatientListViewItems();
      requestFeedbackText.setText("Request sent");
    } else {
      requestFeedbackText.setText("Request failed");
    }
    pause.play();
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

    groupAverageText.setText("Average of same age: ");
    groupAverageNumberText.setText(Integer.toString(Login.userDataFetch.getGroupAverage("sleep")));
    pasientAverageText.setText("Pasients average: ");
    pasientAverageNumberText.setText(Integer.toString(Login.userDataFetch.getPasientAverage("sleep")));
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

    groupAverageText.setText("Average of same age: ");
    groupAverageNumberText.setText(Integer.toString(Login.userDataFetch.getGroupAverage("pulse")));
    pasientAverageText.setText("Pasients average: ");
    pasientAverageNumberText.setText(Integer.toString(Login.userDataFetch.getPasientAverage("pulse")));
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

    groupAverageText.setText("Average of same age: ");
    groupAverageNumberText.setText(Integer.toString(Login.userDataFetch.getGroupAverage("steps")));
    pasientAverageText.setText("Pasients average: ");
    pasientAverageNumberText.setText(Integer.toString(Login.userDataFetch.getPasientAverage("steps")));
  }

  private void hideCharts() {
    sleepPieChart.setVisible(false);
    pulseLineChart.setVisible(false);
    stepBarChart.setVisible(false);
    sleepBarChart.setVisible(false);
  }

  public void handleMessagesListViewClicked() {
    Message message = messagesListView.getSelectionModel().getSelectedItem();
    if (message != null) {
      subjectText.setText(message.getSubject());
      fromText.setText(message.getFrom().getUsername());
      toText.setText(message.getTo().getUsername());
      dateText.setText(message.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
      messageTextArea.setText(message.getMessage());
    }
  }

  public void handleSendMessageButton() {
    PauseTransition pause = new PauseTransition(Duration.seconds(5));
    pause.setOnFinished(event -> sentText.setText(""));

    String subject = subjectTextField.getText();
    String message = sendMessageTextArea.getText();
    User toUser = toChoiceBox.getValue();

    if (toUser != null && !subject.equals("") && !message.equals("")) {
      userDataFetch.createMessage(toUser.getId(), subject, message);
      updateMessagesListViewItems();
      subjectTextField.setText("");
      sendMessageTextArea.setText("");
      sentText.setText("Message Sent!");
    } else {
      sentText.setText("Fill in subject, to and message");
    }
    pause.play();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    this.userDataFetch = Login.userDataFetch;
    this.user = userDataFetch.getCurrentUser();

    setProfileValues();

    setDataChoiceBox();
    setPatientChoiceBoxes();
    hideCharts();

    updatePatientListViewItems();
    updateMessagesListViewItems();
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

  public void updateMessagesListViewItems() {
    messagesListViewItems = messagesListView.getItems();
    messagesListViewItems.clear();
    List<Message> fromMessages = userDataFetch.messagesForMe();
    fromMessages
        .stream()
        .filter(message -> !(message == null))
        .forEach(message -> messagesListViewItems.add(message));
    List<Message> toMessages = userDataFetch.messagesByMe();
    toMessages
        .stream()
        .filter(message -> !(message == null))
        .forEach(message -> messagesListViewItems.add(message));

    FXCollections.sort(messagesListViewItems, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
  }

  public void setPatientChoiceBoxes() {
    acceptedPatientList.clear();
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    requests
        .stream()
        .filter(request -> request.getStatusAsString().equals("ACCEPTED"))
        .forEach(request -> acceptedPatientList.add(request.getDataOwner()));
    patientChoiceBox.setItems(acceptedPatientList);
    toChoiceBox.setItems(acceptedPatientList);
  }

  public User getSelectedPatientCB() {
    User user = patientChoiceBox.getValue();
    return user;
  }

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep");
  }
}
