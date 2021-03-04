package es.iesdpm.fse.ui.festivos;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import es.iesdpm.fse.utils.Dialogs;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class FestivosController extends Controller<App, GridPane> {
	
	private ListProperty<LocalDate> festivos;
	
	@FXML
	private DatePicker festivoDate;

	@FXML
	private Button nuevoButton;

	@FXML
	private Button eliminarButton;

	@FXML
	private TableView<LocalDate> festivosTable;
	
	@FXML
	private TableColumn<LocalDate, LocalDate> diaColumn;

	public FestivosController(App app) {
		super(app, "/fxml/FestivosView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nuevoButton.disableProperty().bind(festivoDate.valueProperty().isNull());
		eliminarButton.disableProperty().bind(festivosTable.getSelectionModel().selectedItemProperty().isNull());
		diaColumn.setCellValueFactory(value -> new SimpleObjectProperty<LocalDate>(value.getValue()));
		festivosTable.getSortOrder().add(diaColumn);
		festivosTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void bind(ListProperty<LocalDate> festivos) {
		this.festivos = festivos;
		SortedList<LocalDate> festivosSorted = festivos.sorted();
		festivosSorted.comparatorProperty().bind(festivosTable.comparatorProperty());
		festivosTable.itemsProperty().bind(new SimpleListProperty<>(festivosSorted));
	}

	public void unbind() {
		this.festivos = null;
		festivosTable.itemsProperty().unbind();
	}

	@FXML
	protected void onNuevoFestivoAction(ActionEvent e) {
		festivos.add(festivoDate.getValue());
	}
	
	@FXML
	protected void onEliminarFestivoAction(ActionEvent e) {
		if (Dialogs.confirm(getApp().getPrimaryStage(), "Eliminando d�as festivos.", "�Seguro que desea eliminar los festivos seleccionados?")) {
			festivos.removeAll(festivosTable.getSelectionModel().getSelectedItems());
		}
	}

}
