package tdt4140.gr1816.app.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import tdt4140.gr1816.app.core.*;

public class UserController implements Initializable {

  @FXML private Button dataButton;

  @FXML private Button deleteUserButton;

  @FXML private Button logOutButton;

  @FXML private Button acceptDoctorButton;

  @FXML private Button removeDoctorButton;

  @FXML private Button deleteDataButton;

  @FXML private Button sendButton;

  @FXML private Text nameText;

  @FXML private Text ageText;

  @FXML private Text genderText;

  @FXML private Text dataDeletionResponseText;

  @FXML private Text subjectText;

  @FXML private Text toText;

  @FXML private Text fromText;

  @FXML private Text dateText;

  @FXML private TextField subjectTextField;

  @FXML private TextArea sendMessageTextArea;

  @FXML private TextArea messageTextArea;

  @FXML private ChoiceBox<User> toChoiceBox;

  @FXML private Text sentText;

  @FXML private DatePicker dataDatePicker;

  @FXML private ListView<DataAccessRequest> doctorsListView;

  @FXML private ListView<DataAccessRequest> doctorRequestListView;

  @FXML private ListView<Message> messagesListView;

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
  
  // Edit profile
  @FXML private Button editProfileButton;
  @FXML private Text editProfileResponse;

  @FXML private HBox profileBox;
  @FXML private VBox editProfileBox;

  @FXML private TextField newUsernameField;
  @FXML private PasswordField newPasswordField;
  @FXML private TextField newAgeField;
  @FXML private RadioButton male;
  @FXML private Button saveButton;
  
  @FXML private Text averageText;
  @FXML private Text averageNumberText;

  private User user;
  private UserDataFetch userDataFetch;

  ObservableList<DataAccessRequest> doctorsListViewItems;
  ObservableList<DataAccessRequest> doctorRequestListViewItems;
  ObservableList<Message> messagesListViewItems;
  ObservableList<User> acceptedDoctorsList = FXCollections.observableArrayList();

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

  public void handleDataButton() {
    if (user.getIsGatheringData()) {
      turnOffDataGathering();
    } else {
      turnOnDataGathering();
    }
    setInitialDataButtonValue();
  }

  public void handleEditProfileButton() {
    profileBox.setVisible(false);
    editProfileBox.setVisible(true);
  }

  public void handleSaveButton() {
    String newUsername = newUsernameField.getText();
    if (newUsername.equals("")) {
      newUsername = "null";
    }
    newUsernameField.clear();

    String newPassword = newPasswordField.getText();
    if (newPassword.equals("")) {
      newPassword = "null";
    }
    newPasswordField.clear();

    String newAge = newAgeField.getText();
    int newAgeInt;
    if (newAge.equals("")) {
      newAgeInt = -1;
    } else {
      newAgeInt = Integer.parseInt(newAge);
      newAgeField.clear();
    }

    String newGender = "null";
    if (male.getToggleGroup().getSelectedToggle() != null) {
      RadioButton genderRB = (RadioButton) male.getToggleGroup().getSelectedToggle();
      newGender = genderRB.getText();
      male.getToggleGroup().selectToggle(null);
    }
    String msg = "";
    if (userDataFetch.editUser(newUsername, newPassword, newAgeInt, newGender)) {
      user = userDataFetch.getCurrentUser();
      setProfileValues();
      msg = "Saved profile";
    } else {
      msg = "Username in use";
    }

    editProfileBox.setVisible(false);
    profileBox.setVisible(true);

    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(event -> editProfileResponse.setText(""));
    editProfileResponse.setText(msg);
    pause.play();
  }

  public void handleCancelButton() {
    editProfileBox.setVisible(false);
    profileBox.setVisible(true);

    newUsernameField.clear();
    newAgeField.clear();
    male.getToggleGroup().selectToggle(null);
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

  public void handleDeleteDataButton() {
    LocalDate ld = dataDatePicker.getValue();

    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(event -> dataDeletionResponseText.setText(""));

    deleteSleepDataFromDate(ld);
    deleteStepsDataFromDate(ld);
    deletePulseDataFromDate(ld);
    pause.play();
  }

  public void deleteSleepDataFromDate(LocalDate ld) {
    List<SleepData> sleepDataList = Login.userDataFetch.getSleepDataByViewer();
    SleepData sleepData =
        sleepDataList.stream().filter(sd -> sd.getDate().equals(ld)).findFirst().orElse(null);
    if (sleepData != null) {
      userDataFetch.deleteSleepData(sleepData.getId());
      dataDeletionResponseText.setText("Data deleted");
    } else {
      dataDeletionResponseText.setText("No data on ths date");
    }
  }

  public void deleteStepsDataFromDate(LocalDate ld) {
    List<StepsData> stepsDataList = Login.userDataFetch.getStepsDataByViewer();
    StepsData stepsData =
        stepsDataList.stream().filter(sd -> sd.getDate().equals(ld)).findFirst().orElse(null);
    if (stepsData != null) {
      userDataFetch.deleteStepsData(stepsData.getId());
      dataDeletionResponseText.setText("Data deleted");
    } else {
      dataDeletionResponseText.setText("No data on ths date");
    }
  }

  public void deletePulseDataFromDate(LocalDate ld) {
    List<PulseData> pulseDataList = Login.userDataFetch.getPulseDataByViewer();
    PulseData pulseData =
        pulseDataList.stream().filter(pd -> pd.getDate().equals(ld)).findFirst().orElse(null);
    if (pulseData != null) {
      dataDeletionResponseText.setText("Data deleted");
      userDataFetch.deletePulseData(pulseData.getId());
    } else {
      dataDeletionResponseText.setText("No data on ths date");
    }
  }

  public void handleRemoveDoctorButton() {
    DataAccessRequest selected = doctorsListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      Login.userDataFetch.answerDataAccessRequest(selected, "REJECTED");
      doctorsListViewItems.remove(selected);
    }
  }

  public void handleAcceptDoctorButton() {
    DataAccessRequest selected = doctorRequestListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      Login.userDataFetch.answerDataAccessRequest(selected, "ACCEPTED");
      doctorRequestListViewItems.remove(selected);
      updateDoctorsListViewItems();
      updateToChoiceBox();
    }
  }

  public void handleRejectDoctorButton() {
    DataAccessRequest selected = doctorRequestListView.getSelectionModel().getSelectedItem();
    if (selected != null) {
      Login.userDataFetch.answerDataAccessRequest(selected, "REJECTED");
      doctorRequestListViewItems.remove(selected);
      updateDoctorsListViewItems();
    }
  }

  public void toggleDataGathering() {
    boolean newStatus = !user.getIsGatheringData();
    userDataFetch.setIsGatheringData(newStatus);
    user.setIsGatheringData(newStatus);
  }

  public void turnOffDataGathering() {
    toggleDataGathering();
  }

  public void turnOnDataGathering() {
    toggleDataGathering();
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
    List<StepsData> stepsDataList = Login.userDataFetch.getStepsDataByViewer();
    stepsDataList
        .stream()
        .forEach(
            stepData ->
                barChartData.add(
                    new XYChart.Data<>(stepData.getDate().toString(), stepData.getSteps())));
    XYChart.Series<String, Number> series = new XYChart.Series<>(barChartData);
    stepBarChart.getData().add(series);
    stepBarChart.setVisible(true);

    averageText.setText("Average steps: ");
    averageNumberText.setText(Integer.toString(Login.userDataFetch.getAverage("steps")));
  }

  public void showPulseChart() {
    hideCharts();
    pulseLineChart.getData().clear();
    pulseChartXAxis.setLabel("Date");
    pulseChartYAxis.setLabel("RestHR");
    ObservableList<XYChart.Data<String, Number>> lineChartData =
        FXCollections.observableArrayList();
    List<PulseData> pulseDataList = Login.userDataFetch.getPulseDataByViewer();
    pulseDataList
        .stream()
        .forEach(
            pulseData ->
                lineChartData.add(
                    new XYChart.Data<>(pulseData.getDate().toString(), pulseData.getRestHr())));
    XYChart.Series<String, Number> series = new XYChart.Series<>(lineChartData);
    pulseLineChart.getData().add(series);
    pulseLineChart.setVisible(true);

    averageText.setText("Average resting heart rate: ");
    averageNumberText.setText(Integer.toString(Login.userDataFetch.getAverage("pulse")));
  }

  public void showSleepDChart() {
    hideCharts();
    sleepBarChart.getData().clear();
    sleepBarChart.setBarGap(0);
    sleepChartXAxis.setLabel("Date");
    sleepChartYAxis.setLabel("Duration in hours");
    ObservableList<XYChart.Data<String, Number>> sleepBarChartData =
        FXCollections.observableArrayList();
    List<SleepData> sleepDataList = Login.userDataFetch.getSleepDataByViewer();
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

    averageText.setText("Average hour of sleep: ");
    averageNumberText.setText(Integer.toString(Login.userDataFetch.getAverage("sleep")));
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
  public void initialize(URL arg0, ResourceBundle arg1) {

    this.userDataFetch = Login.userDataFetch;
    this.user = userDataFetch.getCurrentUser();

    setProfileValues();

    setInitialDataButtonValue();

    updateDoctorsListViewItems();

    updateDoctorRequestListViewItems();

    updateMessagesListViewItems();

    setDataChoiceBox();

    hideCharts();

    updateToChoiceBox();
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
    boolean dataGatheringOn = user.getIsGatheringData();
    if (dataGatheringOn) {
      dataButton.setText("Turn off");
    } else {
      dataButton.setText("Turn on");
    }
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
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    requests
        .stream()
        .filter(request -> request.getStatusAsString().equals("PENDING"))
        .forEach(request -> doctorRequestListViewItems.add(request));
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

  private void hideCharts() {
    stepBarChart.setVisible(false);
    pulseLineChart.setVisible(false);
    sleepBarChart.setVisible(false);
    averageText.setText("");
    averageNumberText.setText("");
  }

  public void setDataChoiceBox() {
    dataChoiceBox.getItems().add("Pulse");
    dataChoiceBox.getItems().add("Steps");
    dataChoiceBox.getItems().add("Sleep - duration");
  }

  public void updateToChoiceBox() {
    acceptedDoctorsList.clear();
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    requests
        .stream()
        .filter(request -> request.getStatusAsString().equals("ACCEPTED"))
        .forEach(request -> acceptedDoctorsList.add(request.getRequestedBy()));
    toChoiceBox.setItems(acceptedDoctorsList);
  }
}
