package es.iesdpm.fse.ui;

import java.net.URL;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Centro;
import es.iesdpm.fse.ui.centro.CentroController;
import es.iesdpm.fse.ui.festivos.FestivosController;
import es.iesdpm.fse.ui.grupos.GruposController;
import es.iesdpm.fse.ui.horas.HorasController;
import es.iesdpm.fse.ui.profesores.ProfesoresController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class MainController extends Controller<App, BorderPane> {
	
	private CentroController centroController;
	private FestivosController festivosController;
	private GruposController gruposController;
	private ProfesoresController profesoresController;
	private HorasController horasController;
	
	@FXML 
	private Tab centroTab, festivosTab, gruposTab, profesoresTab, horasTab; 

	public MainController(App app) {
		super(app, "/fxml/MainView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		centroController = new CentroController(getApp());
		festivosController = new FestivosController(getApp());
		gruposController = new GruposController(getApp());
		profesoresController = new ProfesoresController(getApp());
		horasController = new HorasController(getApp());
		
		centroTab.setContent(centroController.getView());
		festivosTab.setContent(festivosController.getView());
		gruposTab.setContent(gruposController.getView());
		profesoresTab.setContent(profesoresController.getView());
		horasTab.setContent(horasController.getView());
	}
	
	public void bind(Centro centro) {
		centroController.bind(centro);
		festivosController.bind(centro.festivosProperty());
		gruposController.bind(centro.gruposProperty(), centro.profesoresProperty());
		profesoresController.bind(centro.profesoresProperty());
		horasController.bind(centro);
	}

	public void unbind(Centro centro) {
		centroController.unbind(centro);
		festivosController.unbind();
		gruposController.unbind();
		profesoresController.unbind();
		horasController.unbind();
	}

}
