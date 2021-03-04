package es.iesdpm.fse.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public abstract class Controller<A, V> implements Initializable {

	private A app;
	private V view;
	
	public Controller(A app, String fxmlPath) {
		this.app = app;
		loadView(fxmlPath);
	}

	protected void loadView(String fxmlPath) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
			fxmlLoader.setController(this);
			view = fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public V getView() {
		return view;
	}
	
	public A getApp() {
		return app;
	}

}
