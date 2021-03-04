package es.iesdpm.fse.ui.grupos.horario;

import java.net.URL;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Asignatura;
import es.iesdpm.fse.model.DiaSemana;
import es.iesdpm.fse.model.SesionSemanal;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.BorderPane;

public class HorarioController extends Controller<App, BorderPane> {
	
	private ListProperty<Hora> horas;
	private ListProperty<Asignatura> asignaturas;
	
	@FXML
	private TableView<Hora> horarioTable; 

	@FXML
	private TableColumn<Hora, Number> horaColumn;

	@FXML
	private TableColumn<Hora, Asignatura> lunesColumn, martesColumn, miercolesColumn, juevesColumn, viernesColumn;

	public HorarioController(App app) {
		super(app, "/fxml/HorarioView.fxml");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		asignaturas = new SimpleListProperty<>(this, "asignaturas");
		horas = new SimpleListProperty<>(this, "horas", FXCollections.observableArrayList());
		for (int i = 1; i <= 6; i++) {
			final Hora hora = new Hora(i);
			hora.lunesProperty().addListener((observable, oldValue, newValue) -> {
				SesionSemanal sesion = new SesionSemanal(DiaSemana.LUNES, hora.getHora());
				if (oldValue != null) oldValue.getSesiones().remove(sesion);
				if (newValue != null && !newValue.getSesiones().contains(sesion)) newValue.getSesiones().add(sesion);
			});
			hora.martesProperty().addListener((observable, oldValue, newValue) -> {
				SesionSemanal sesion = new SesionSemanal(DiaSemana.MARTES, hora.getHora());
				if (oldValue != null) oldValue.getSesiones().remove(sesion);
				if (newValue != null && !newValue.getSesiones().contains(sesion)) newValue.getSesiones().add(sesion);
			});
			hora.miercolesProperty().addListener((observable, oldValue, newValue) -> {
				SesionSemanal sesion = new SesionSemanal(DiaSemana.MIERCOLES, hora.getHora());
				if (oldValue != null) oldValue.getSesiones().remove(sesion);
				if (newValue != null && !newValue.getSesiones().contains(sesion)) newValue.getSesiones().add(sesion);
			});
			hora.juevesProperty().addListener((observable, oldValue, newValue) -> {
				SesionSemanal sesion = new SesionSemanal(DiaSemana.JUEVES, hora.getHora());
				if (oldValue != null) oldValue.getSesiones().remove(sesion);
				if (newValue != null && !newValue.getSesiones().contains(sesion)) newValue.getSesiones().add(sesion);
			});
			hora.viernesProperty().addListener((observable, oldValue, newValue) -> {
				SesionSemanal sesion = new SesionSemanal(DiaSemana.VIERNES, hora.getHora());
				if (oldValue != null) oldValue.getSesiones().remove(sesion);
				if (newValue != null && !newValue.getSesiones().contains(sesion)) newValue.getSesiones().add(sesion);
			});
			horas.addAll(hora);
		}

		horaColumn.setCellValueFactory(value -> value.getValue().horaProperty());

		lunesColumn.setCellValueFactory(value -> value.getValue().lunesProperty());
		lunesColumn.setCellFactory(ComboBoxTableCell.forTableColumn(asignaturas));
		
		martesColumn.setCellValueFactory(value -> value.getValue().martesProperty());
		martesColumn.setCellFactory(ComboBoxTableCell.forTableColumn(asignaturas));
		
		miercolesColumn.setCellValueFactory(value -> value.getValue().miercolesProperty());
		miercolesColumn.setCellFactory(ComboBoxTableCell.forTableColumn(asignaturas));
		
		juevesColumn.setCellValueFactory(value -> value.getValue().juevesProperty());
		juevesColumn.setCellFactory(ComboBoxTableCell.forTableColumn(asignaturas));
		
		viernesColumn.setCellValueFactory(value -> value.getValue().viernesProperty());
		viernesColumn.setCellFactory(ComboBoxTableCell.forTableColumn(asignaturas));

		horarioTable.itemsProperty().bind(horas);
	}
	
	public void bind(ListProperty<Asignatura> asignaturas) {
		this.asignaturas.bind(asignaturas);
		for (Asignatura asignatura : asignaturas) {
			for (int i = 0; i < asignatura.getSesiones().size(); i++) {
				SesionSemanal sesion = asignatura.getSesiones().get(i);
				switch (sesion.getDia()) {
				case LUNES: horas.get(sesion.getHora() - 1).setLunes(asignatura); break;
				case MARTES: horas.get(sesion.getHora() - 1).setMartes(asignatura); break;
				case MIERCOLES: horas.get(sesion.getHora() - 1).setMiercoles(asignatura); break;
				case JUEVES: horas.get(sesion.getHora() - 1).setJueves(asignatura); break;
				case VIERNES: horas.get(sesion.getHora() - 1).setViernes(asignatura); break;
				default: break;
				}
			}
		}
	}

	public void unbind() {
		this.asignaturas.unbind();
	}

}
