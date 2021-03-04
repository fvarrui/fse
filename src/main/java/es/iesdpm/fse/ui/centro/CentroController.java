package es.iesdpm.fse.ui.centro;

import java.net.URL;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Centro;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CentroController extends Controller<App, AnchorPane> {
	
	@FXML
	private TextField denominacionText;

	@FXML
	private DatePicker inicioCursoDate;

	@FXML
	private DatePicker finCursoDate;

	public CentroController(App app) {
		super(app, "/fxml/CentroView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void bind(Centro centro) {
		denominacionText.textProperty().bindBidirectional(centro.denominacionProperty());
		inicioCursoDate.valueProperty().bindBidirectional(centro.inicioCursoProperty());
		finCursoDate.valueProperty().bindBidirectional(centro.finCursoProperty());
	}

	public void unbind(Centro centro) {
		denominacionText.textProperty().unbindBidirectional(centro.denominacionProperty());
		inicioCursoDate.valueProperty().unbindBidirectional(centro.inicioCursoProperty());
		finCursoDate.valueProperty().unbindBidirectional(centro.finCursoProperty());
	}

}
