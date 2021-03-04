package es.iesdpm.fse.ui;

import java.io.File;

import es.iesdpm.fse.model.Centro;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {
	
	private static final File FSE_FILE = new File("fse.xml");
	
	private ObjectProperty<Centro> centro;
	
	private MainController mainController;
	
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		centro = new SimpleObjectProperty<Centro>(this, "centro", new Centro());
		centro.addListener((observable, oldValue, newValue) -> { mainController.bind(newValue); });
		
		mainController = new MainController(this);
		
		Scene scene = new Scene(mainController.getView(), 900, 600);

		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/eu-16x16.png")));
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/eu-32x32.png")));
		primaryStage.setTitle("FSE");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> onPrimaryStageCloseRequest(e));
		primaryStage.show();

		try {
			centro.set(Centro.read(FSE_FILE));
		} catch (Exception e) {
			centro.set(new Centro());
			e.printStackTrace();
		}
	}

	private void onPrimaryStageCloseRequest(WindowEvent e) {
		try {
			centro.get().save(FSE_FILE);
		} catch (Exception e1) {
			e1.printStackTrace();
			e.consume();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
