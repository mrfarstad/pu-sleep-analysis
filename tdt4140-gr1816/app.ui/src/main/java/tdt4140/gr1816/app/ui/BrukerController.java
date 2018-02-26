package tdt4140.gr1816.app.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class BrukerController implements Initializable {

	public Button dataKnapp;
	
	private boolean dataLagringPaa;

	

	public void handleDataKnapp() {
		if (dataLagringPaa) {
			dataKnapp.setText("Skru på");
			dataLagringPaa = false;
		}
		else {
			dataKnapp.setText("Skru av");
			dataLagringPaa = true;
		}	
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.dataLagringPaa = true;
		
		if (dataLagringPaa) {
			dataKnapp.setText("Skru av");
		}
		else {
			dataKnapp.setText("Skru på");
		}		
	}
}
