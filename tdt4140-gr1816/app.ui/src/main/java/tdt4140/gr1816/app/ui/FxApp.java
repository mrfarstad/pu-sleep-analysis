package tdt4140.gr1816.app.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FxApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("FxApp.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void showDocterScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FxApp.class.getResource("DoctorGUI.fxml"));
	}
	
	public static void showPasientScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FxApp.class.getResource("UserGUI.fxml"));
	}
	
	public static void showCreateNewUserScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(FxApp.class.getResource("CreateNewUserGUI.fxml"));
	}

	public static void main(String[] args) {
		launch(args);
	}

}
