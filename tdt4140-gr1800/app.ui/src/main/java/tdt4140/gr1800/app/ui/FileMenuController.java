package tdt4140.gr1800.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import tdt4140.gr1800.app.core.IDocumentStorage;

public class FileMenuController {

	private IDocumentStorage<File> documentStorage;

	public void setDocumentStorage(IDocumentStorage<File> documentStorage) {
		this.documentStorage = documentStorage;
	}

	private Consumer<IDocumentStorage<File>> onDocumentChanged;
	
	public void setOnDocumentChanged(Consumer<IDocumentStorage<File>> onDocumentChanged) {
		this.onDocumentChanged = onDocumentChanged;
	}
	
	@FXML
	public void handleNewAction() {
		documentStorage.newDocument();
		fireDocumentChanged();
	}

	private void fireDocumentChanged() {
		if (onDocumentChanged != null) {
			onDocumentChanged.accept(documentStorage);
		}
	}

	private List<File> recentFiles = new ArrayList<File>();

	@FXML
	private Menu recentMenu;
	
	protected void updateRecentMenu(File file) {
		recentFiles.remove(file);
		recentFiles.add(0, file);
		recentMenu.getItems().clear();
		for (File recentFile : recentFiles) {
			MenuItem menuItem = new MenuItem();
			menuItem.setText(recentFile.toString());
			menuItem.setOnAction(event -> handleOpenAction(event));
			recentMenu.getItems().add(menuItem);
		}
	}
	
	private FileChooser fileChooser;

	protected FileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
		}
		return fileChooser;
	}

	@FXML
	public void handleOpenAction(ActionEvent event) {
		File selection = null;
		if (event.getSource() instanceof MenuItem) {
			File file = new File(((MenuItem) event.getSource()).getText());
			if (file.exists()) {
				selection = file;
			}
		}
		if (selection == null) {
			FileChooser fileChooser = getFileChooser();
			selection = fileChooser.showOpenDialog(null);
		}
		if (selection != null) {
			try {
				documentStorage.openDocument(selection);
				updateRecentMenu(selection);
				fireDocumentChanged();
			} catch (IOException e) {
				// TODO
			}
		}
	}
	
	@FXML
	public void handleSaveAction() {
		try {
			documentStorage.saveDocument();
		} catch (IOException e) {
			// TODO
		}
	}
	
	@FXML
	public void handleSaveAsAction() {
		FileChooser fileChooser = getFileChooser();
		File selection = fileChooser.showSaveDialog(null);
		File oldStorage = documentStorage.getDocumentLocation();
		try {
			documentStorage.setDocumentLocation(selection);
			documentStorage.saveDocument();
		} catch (IOException e) {
			// TODO
			documentStorage.setDocumentLocation(oldStorage);
		}
	}
	
	@FXML
	public void handleSaveCopyAsAction() {
		FileChooser fileChooser = getFileChooser();
		File selection = fileChooser.showSaveDialog(null);
		File oldStorage = documentStorage.getDocumentLocation();
		try {
			documentStorage.setDocumentLocation(selection);
			documentStorage.saveDocument();
		} catch (IOException e) {
			// TODO
		} finally {
			documentStorage.setDocumentLocation(oldStorage);
		}
	}
}
