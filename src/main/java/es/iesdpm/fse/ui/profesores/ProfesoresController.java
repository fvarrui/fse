package es.iesdpm.fse.ui.profesores;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import es.iesdpm.fse.model.Profesor;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import es.iesdpm.fse.utils.Dialogs;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ProfesoresController extends Controller<App, AnchorPane> {
	
	private ListProperty<Profesor> profesores;
	
	private EditarProfesorController editarController;
	
	private VBox vacioPane;
	
	@FXML
	private TitledPane profesorPane;
	
	@FXML
	private TableColumn<Profesor, String> nombreColumn, apellidosColumn;

	@FXML
	private TableView<Profesor> profesoresTable;

	@FXML
	private Button nuevoButton, eliminarButton;

	public ProfesoresController(App app) {
		super(app, "/fxml/ProfesoresView.fxml");
		editarController = new EditarProfesorController(app);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		vacioPane = new VBox(new Label("No hay ning�n profesor seleccionado."));
		vacioPane.setAlignment(Pos.CENTER);
		
		eliminarButton.disableProperty().bind(profesoresTable.getSelectionModel().selectedItemProperty().isNull());

		nombreColumn.setCellValueFactory(value -> value.getValue().nombreProperty());
		apellidosColumn.setCellValueFactory(value -> value.getValue().apellidosProperty());
		
		profesoresTable.getSortOrder().addAll(apellidosColumn, nombreColumn);
		profesoresTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		profesoresTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onProfesoresTableSelectedItem(oldValue, newValue));
		
		profesorPane.setContent(vacioPane);
	}

	public void bind(ListProperty<Profesor> profesores) {
		this.profesores = profesores;
		SortedList<Profesor> profesoresSorted = profesores.sorted();
		profesoresSorted.comparatorProperty().bind(profesoresTable.comparatorProperty());
		profesoresTable.itemsProperty().bind(new SimpleListProperty<>(profesoresSorted));
	}

	public void unbind() {
		this.profesores = null;
		profesoresTable.itemsProperty().unbind();
	}

	private void onProfesoresTableSelectedItem(Profesor oldValue, Profesor newValue) {
		if (oldValue != null) {
			editarController.unbind(oldValue);
		} 
		if (newValue != null) {
			profesorPane.setContent(editarController.getView());
			profesorPane.requestFocus();
			editarController.bind(newValue);
		} else {
			profesorPane.setContent(vacioPane);
		}
	}
	
	@FXML
	protected void onNuevoButtonAction(ActionEvent e) {
		Profesor profesor = new Profesor();
		profesor.setIdentificador(UUID.randomUUID().toString());
		profesor.setNombre("Sin nombre");
		profesor.setApellidos("Sin apellidos");
		profesores.add(profesor);
		profesoresTable.getSelectionModel().select(profesor);
	}
	
	@FXML
	protected void onEliminarButtonAction(ActionEvent e) {
		if (Dialogs.confirm(getApp().getPrimaryStage(), "Eliminando profesores.", "�Seguro que desea eliminar los profesores seleccionados?")) {
			profesores.removeAll(profesoresTable.getSelectionModel().getSelectedItems());
			
		}
	}

}
