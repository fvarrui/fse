package es.iesdpm.fse.ui.profesores;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Falta;
import es.iesdpm.fse.model.Profesor;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import es.iesdpm.fse.utils.Dialogs;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;

public class EditarProfesorController extends Controller<App, AnchorPane> {
	
	private CheckBox [] horasCheck;
	private TableColumn<Falta, Boolean> [] horasColumn;
	private ObjectProperty<LocalDate> dia;
	private ListProperty<Integer> horas;
	
	private Profesor profesor;
	
	@FXML
	private TextField identificadorText, nombreText, apellidosText;
	
	@FXML
	private DatePicker diaDate;
	
	@FXML
	private CheckBox todasCheck, hora1Check, hora2Check, hora3Check, hora4Check, hora5Check, hora6Check;
	
	@FXML
	private TableColumn<Falta, LocalDate> diaColumn;

	@FXML
	private TableColumn<Falta, Boolean> hora1Column, hora2Column, hora3Column, hora4Column, hora5Column, hora6Column;
	
	@FXML 
	private TableView<Falta> faltasTable;
	
	@FXML 
	private Button ponerButton, quitarButton;

	public EditarProfesorController(App app) {
		super(app, "/fxml/EditarProfesorView.fxml");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		dia = new SimpleObjectProperty<LocalDate>(this, "dia");
		dia.bindBidirectional(diaDate.valueProperty());
		
		horas = new SimpleListProperty<>(this, "horas", FXCollections.observableArrayList());
		
		horasCheck = new CheckBox[]{ hora1Check, hora2Check, hora3Check, hora4Check, hora5Check, hora6Check};
		int i = 1;
		for (CheckBox horaCheck : horasCheck) {
			horaCheck.disableProperty().bind(todasCheck.selectedProperty());
			horaCheck.selectedProperty().addListener(new HoraChangeListener(i++, horas));
		}
		
		todasCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
			for (CheckBox horaCheck : horasCheck) horaCheck.setSelected(newValue);
		});

		diaColumn.setCellValueFactory(value -> value.getValue().diaProperty());
		diaColumn.setSortType(SortType.ASCENDING);

		horasColumn = new TableColumn[]{ hora1Column, hora2Column, hora3Column, hora4Column, hora5Column, hora6Column};
		i = 1;
		for (TableColumn<Falta, Boolean> horaColumn : horasColumn) {
			horaColumn.setCellFactory(CheckBoxTableCell.forTableColumn(horaColumn));
			horaColumn.setCellValueFactory(new HoraCellValueFactory(i++));
		}
		
		faltasTable.getSortOrder().addAll(diaColumn);
		faltasTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ponerButton.disableProperty().bind(
				diaDate.valueProperty().isNull()
				.or(hora1Check.selectedProperty()
					.or(hora2Check.selectedProperty())
					.or(hora3Check.selectedProperty())
					.or(hora4Check.selectedProperty())
					.or(hora5Check.selectedProperty())
					.or(hora6Check.selectedProperty()).not()
				)
			);
		quitarButton.disableProperty().bind(faltasTable.getSelectionModel().selectedItemProperty().isNull());
		
	}
	
	public void bind(Profesor profesor) {
		this.profesor = profesor;
		identificadorText.textProperty().bindBidirectional(profesor.identificadorProperty());
		nombreText.textProperty().bindBidirectional(profesor.nombreProperty());
		apellidosText.textProperty().bindBidirectional(profesor.apellidosProperty());
		
		SortedList<Falta> faltasSorted = profesor.faltasProperty().sorted();
		faltasSorted.comparatorProperty().bind(faltasTable.comparatorProperty());
		faltasTable.itemsProperty().bind(new SimpleListProperty<>(faltasSorted));
	}

	public void unbind(Profesor profesor) {
		this.profesor = null;
		identificadorText.textProperty().unbindBidirectional(profesor.identificadorProperty());
		nombreText.textProperty().unbindBidirectional(profesor.nombreProperty());
		apellidosText.textProperty().unbindBidirectional(profesor.apellidosProperty());
		faltasTable.itemsProperty().unbind();
	}

	@FXML 
	private void onPonerButtonAction(ActionEvent e) {
		Falta falta = new Falta();
		falta.setDia(dia.get());
		falta.getHoras().addAll(horas.get());
		if (!profesor.getFaltas().contains(falta)) {
			profesor.getFaltas().add(falta);
			dia.set(null);
			todasCheck.setSelected(false);
			for (CheckBox horaCheck : horasCheck) horaCheck.setSelected(false);			
		} else {
			Dialogs.warning(getApp().getPrimaryStage(), "No fue posible poner la falta.", "Ya existe un registro para las faltas del d�a " + dia.get() + ".");
		}		
	}

	@FXML 
	private void onQuitarButtonAction(ActionEvent e) {
		if (Dialogs.confirm(getApp().getPrimaryStage(), "Eliminando faltas.", "�Seguro que desea eliminar las faltas seleccionadas?")) {
			profesor.getFaltas().removeAll(faltasTable.getSelectionModel().getSelectedItems());
		}		
	}

}
