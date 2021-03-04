package es.iesdpm.fse.ui.grupos;

import java.net.URL;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Asignatura;
import es.iesdpm.fse.model.Grupo;
import es.iesdpm.fse.model.Nivel;
import es.iesdpm.fse.model.Profesor;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import es.iesdpm.fse.ui.grupos.horario.HorarioController;
import es.iesdpm.fse.utils.Dialogs;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditarGrupoController extends Controller<App, AnchorPane> {
	
	private Grupo grupo;
	private ListProperty<Profesor> profesores;
	
	@FXML
	private TextField identificadorText, denominacionText, idAsignaturaText, denominacionAsignaturaText;

	@FXML
	private ComboBox<Nivel> nivelCombo;
	
	@FXML
	private ComboBox<Profesor> tutorCombo, profesorCombo;
	
	@FXML
	private TableColumn<Asignatura, String> identificadorColumn, denominacionColumn;

	@FXML
	private TableColumn<Asignatura, Profesor> profesorColumn;

	@FXML
	private TableColumn<Asignatura, Number> sesionesColumn;

	@FXML 
	private TableView<Asignatura> asignaturasTable;
	
	@FXML 
	private Button nuevaButton, eliminarButton, horarioButton;

	public EditarGrupoController(App app) {
		super(app, "/fxml/EditarGrupoView.fxml");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		profesores = new SimpleListProperty<Profesor>(this, "profesores"); 

		tutorCombo.itemsProperty().bind(this.profesores);
		profesorCombo.itemsProperty().bind(this.profesores);
		
		nivelCombo.setItems(FXCollections.observableArrayList(Nivel.values()));

		identificadorColumn.setCellValueFactory(value -> value.getValue().identificadorProperty());
		identificadorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		denominacionColumn.setCellValueFactory(value -> value.getValue().denominacionProperty());
		denominacionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		profesorColumn.setCellValueFactory(value -> value.getValue().profesorProperty());
		profesorColumn.setCellFactory(ComboBoxTableCell.forTableColumn(profesores));
		
		sesionesColumn.setCellValueFactory(value -> Bindings.size(value.getValue().sesionesProperty()));

		asignaturasTable.getSortOrder().addAll(denominacionColumn);
		asignaturasTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		nuevaButton.disableProperty().bind(idAsignaturaText.textProperty().isEmpty().or(denominacionAsignaturaText.textProperty().isEmpty()));
		eliminarButton.disableProperty().bind(asignaturasTable.getSelectionModel().selectedItemProperty().isNull());
		
	}
	
	public void bind(Grupo grupo, ListProperty<Profesor> profesores) {
		
		SortedList<Profesor> profesoresSorted = profesores.sorted((p1, p2) -> { return (p1.getApellidos().compareToIgnoreCase(p2.getApellidos())); });
		
		this.grupo = grupo;
		this.profesores.bind(new SimpleListProperty<>(profesoresSorted));
		identificadorText.textProperty().bindBidirectional(grupo.identificadorProperty());
		denominacionText.textProperty().bindBidirectional(grupo.denominacionProperty());
		tutorCombo.valueProperty().bindBidirectional(grupo.tutorProperty());
		nivelCombo.valueProperty().bindBidirectional(grupo.nivelProperty());

		FilteredList<Asignatura> asignaturasFiltered = grupo.asignaturasProperty().filtered(p -> p.getIdentificador() != null);
		SortedList<Asignatura> asignaturasSorted = asignaturasFiltered.sorted();
		asignaturasSorted.comparatorProperty().bind(asignaturasTable.comparatorProperty());
		asignaturasTable.itemsProperty().bind(new SimpleListProperty<>(asignaturasSorted));
	}

	public void unbind(Grupo grupo) {
		this.grupo = null;
		this.profesores.unbind();
		identificadorText.textProperty().unbindBidirectional(grupo.identificadorProperty());
		denominacionText.textProperty().unbindBidirectional(grupo.denominacionProperty());
		tutorCombo.valueProperty().unbindBidirectional(grupo.tutorProperty());
		nivelCombo.valueProperty().unbindBidirectional(grupo.nivelProperty());		
		asignaturasTable.itemsProperty().unbind();
	}

	@FXML 
	private void onNuevaButtonAction(ActionEvent e) {
		Asignatura asignatura = new Asignatura();
		asignatura.setIdentificador(idAsignaturaText.getText());
		asignatura.setDenominacion(denominacionAsignaturaText.getText());
		asignatura.setProfesor(profesorCombo.getValue());
		if (!grupo.getAsignaturas().contains(asignatura)) {
			grupo.getAsignaturas().add(asignatura);
			idAsignaturaText.clear();
			denominacionAsignaturaText.clear();
			profesorCombo.setValue(null);
		} else {
			Dialogs.warning(getApp().getPrimaryStage(), "No fue posible crear la asignatura.", "Ya existe una asignatura con el identificador " + asignatura.getIdentificador() + ".");
		}
	}
	
	@FXML 
	private void onHorarioButtonAction(ActionEvent e) {
		HorarioController controller = new HorarioController(getApp());
		controller.bind(grupo.asignaturasProperty());
		Scene horarioScene = new Scene(controller.getView(), 640, 480);
		Stage horarioStage = new Stage(StageStyle.UTILITY);
		horarioStage.setScene(horarioScene);
		horarioStage.setTitle("Horario de " + grupo.getIdentificador());
		horarioStage.initOwner(getApp().getPrimaryStage());
		horarioStage.initModality(Modality.APPLICATION_MODAL);
		horarioStage.showAndWait();
		controller.unbind();
	}

	@FXML 
	private void onEliminarButtonAction(ActionEvent e) {
		if (Dialogs.confirm(getApp().getPrimaryStage(), "Eliminando asignaturas.", "�Seguro que desea eliminar las asignaturas seleccionadas?")) {
			grupo.getAsignaturas().removeAll(asignaturasTable.getSelectionModel().getSelectedItems());
		}		
	}

}
