package es.iesdpm.fse.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Dialogs {

	public static void error(Stage owner, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle(owner.getTitle());
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void warning(Stage owner, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(owner);
		alert.setTitle(owner.getTitle());
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static boolean confirm(Stage owner, String header, String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(owner);
		alert.setTitle(owner.getTitle());
		alert.setHeaderText(header);
		alert.setContentText(content);
		return (alert.showAndWait().get() == ButtonType.OK);
	}
	
	public static void info(Stage owner, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(owner);
		alert.setTitle(owner.getTitle());
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
}
