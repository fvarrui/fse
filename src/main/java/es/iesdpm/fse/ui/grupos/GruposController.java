package es.iesdpm.fse.ui.grupos;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import es.iesdpm.fse.model.Asignatura;
import es.iesdpm.fse.model.DiaSemana;
import es.iesdpm.fse.model.Grupo;
import es.iesdpm.fse.model.Nivel;
import es.iesdpm.fse.model.Profesor;
import es.iesdpm.fse.model.SesionSemanal;
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

public class GruposController extends Controller<App, AnchorPane> {
	
	private ListProperty<Grupo> grupos;
	private ListProperty<Profesor> profesores;
	
	private VBox vacioPane;
	
	private EditarGrupoController editarController;
	
	@FXML
	private TitledPane grupoPane;
	
	@FXML
	private TableColumn<Grupo, String> identificadorColumn;
	
	@FXML
	private TableColumn<Grupo, Nivel> nivelColumn;

	@FXML
	private TableView<Grupo> gruposTable;

	@FXML
	private Button nuevoButton, eliminarButton;

	public GruposController(App app) {
		super(app, "/fxml/GruposView.fxml");
		editarController = new EditarGrupoController(app);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		vacioPane = new VBox(new Label("No hay ning�n profesor seleccionado."));
		vacioPane.setAlignment(Pos.CENTER);
		
		eliminarButton.disableProperty().bind(gruposTable.getSelectionModel().selectedItemProperty().isNull());

		identificadorColumn.setCellValueFactory(value -> value.getValue().identificadorProperty());
		nivelColumn.setCellValueFactory(value -> value.getValue().nivelProperty());
		
		gruposTable.getSortOrder().addAll(nivelColumn, identificadorColumn);
		gruposTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		gruposTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> onGruposTableSelectedItem(oldValue, newValue));
		
		grupoPane.setContent(vacioPane);
	}

	public void bind(ListProperty<Grupo> grupos, ListProperty<Profesor> profesores) {
		this.grupos = grupos;
		this.profesores = profesores;
		
		SortedList<Grupo> gruposSorted = grupos.sorted();
		gruposSorted.comparatorProperty().bind(gruposTable.comparatorProperty());
		gruposTable.itemsProperty().bind(new SimpleListProperty<>(gruposSorted));
	}


	public void unbind() {
		this.grupos = null;
		gruposTable.itemsProperty().unbind();
	}

	private void onGruposTableSelectedItem(Grupo oldValue, Grupo newValue) {
		if (oldValue != null) {
			editarController.unbind(oldValue);
		} 
		if (newValue != null) {
			grupoPane.setContent(editarController.getView());
			editarController.bind(newValue, profesores);
		} else {
			grupoPane.setContent(vacioPane);
		}
	}
	
	@FXML
	protected void onNuevoButtonAction(ActionEvent e) {
		Asignatura asignatura = new Asignatura();
		for (DiaSemana dia : DiaSemana.values()) {
			if (!dia.equals(DiaSemana.SABADO) && !dia.equals(DiaSemana.DOMINGO)) {
				for (int hora = 1; hora <= 6; hora++) { 
					asignatura.getSesiones().add(new SesionSemanal(dia, hora));
				}
			}
		}
		
		Grupo grupo = new Grupo();
		grupo.setIdentificador(UUID.randomUUID().toString());
		grupo.setDenominacion("Sin denominaci�n");
		grupo.getAsignaturas().add(asignatura);
		grupos.add(grupo);
	}
	
	@FXML
	protected void onEliminarButtonAction(ActionEvent e) {
		if (Dialogs.confirm(getApp().getPrimaryStage(), "Eliminando grupos.", "�Seguro que desea eliminar los grupos seleccionados?")) {
			grupos.removeAll(gruposTable.getSelectionModel().getSelectedItems());
		}
	}

}
