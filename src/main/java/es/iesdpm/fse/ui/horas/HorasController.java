package es.iesdpm.fse.ui.horas;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;

import es.iesdpm.fse.model.Asignatura;
import es.iesdpm.fse.model.Centro;
import es.iesdpm.fse.model.Grupo;
import es.iesdpm.fse.ui.Controller;
import es.iesdpm.fse.ui.App;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class HorasController extends Controller<App, BorderPane> {
	
	private Centro centro;
	
	private ObjectProperty<Grupo> grupoSeleccionado;
	private ListProperty<Grupo> grupos;
	private ListProperty<HorasAsignatura> horas;
	
	@FXML
	private TableView<HorasAsignatura> horasTable;

	@FXML
	private TableColumn<HorasAsignatura, String> asignaturaColumn, profesorColumn;
	
	@FXML
	private ComboBox<Grupo> grupoCombo;
	
	@FXML
	private Button calcularButton;
	
	@FXML
	private ProgressIndicator calcularProgress;

	public HorasController(App app) {
		super(app, "/fxml/HorasView.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		grupoSeleccionado = new SimpleObjectProperty<Grupo>(this, "grupoSeleccionado");
		grupos = new SimpleListProperty<>(this, "grupos");
		horas = new SimpleListProperty<>(this, "horas", FXCollections.observableArrayList());
		
		grupoCombo.itemsProperty().bind(grupos);
		grupoSeleccionado.bind(grupoCombo.valueProperty());
		horasTable.itemsProperty().bind(horas);
		
		asignaturaColumn.setCellValueFactory(value -> value.getValue().asignaturaProperty().get().identificadorProperty());
		profesorColumn.setCellValueFactory(value -> {
			String profesor = "";
			if (value.getValue().asignaturaProperty().isNotNull()
					.and(value.getValue().asignaturaProperty().get().profesorProperty().isNotNull()).get()) {
				profesor = value.getValue().asignaturaProperty().get().profesorProperty().get().toString();	
			}
			return new SimpleStringProperty(profesor);
		});
		
		calcularButton.disableProperty().bind(grupoCombo.valueProperty().isNull());
	}

	public void bind(Centro centro) {
		this.centro = centro;
		grupos.bind(centro.gruposProperty());
	}

	public void unbind() {
		this.centro = null;
		grupos.unbind();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	private void onCalcularButtonAction(ActionEvent e) {
		FilteredList<Asignatura> asignaturas = grupoSeleccionado.get().getAsignaturas().filtered(p -> p.getIdentificador() != null);

		horasTable.getColumns().retainAll(asignaturaColumn, profesorColumn);
		LocalDate actual = centro.getInicioCurso();
		while (actual.isBefore(centro.getFinCurso())) {
			final Periodo periodo = new Periodo(actual.getMonthValue(), actual.getYear());

			TableColumn<HorasAsignatura, Number> previstasColumn = new TableColumn<HorasAsignatura, Number>("Previstas");
			previstasColumn.setCellValueFactory(value -> value.getValue().horasProperty().get(periodo.toString()).previstasProperty() );

			TableColumn<HorasAsignatura, Number> elegiblesColumn = new TableColumn<HorasAsignatura, Number>("Elegibles");
			elegiblesColumn.setCellValueFactory(value -> value.getValue().horasProperty().get(periodo.toString()).elegiblesProperty());

			TableColumn<HorasAsignatura, String> periodoColumn = new TableColumn<HorasAsignatura, String>(periodo.toString());
			periodoColumn.getColumns().addAll(previstasColumn, elegiblesColumn);

			horasTable.getColumns().add(periodoColumn);

			actual = LocalDate.of(actual.getYear(), actual.getMonthValue(), 1).plusMonths(1);
		}
		
		Task<Void> backgroundTask = new Task<Void>() {
			protected Void call() throws Exception {

				updateMessage("Calculando horas...");
				
				int meses = Period.between(centro.getInicioCurso(), centro.getFinCurso()).getMonths();
				int tiempoTotal = asignaturas.size() * meses;
				int hecho = 0;

				updateProgress(hecho++, tiempoTotal);
						
				horas.clear();
				for (Asignatura asignatura : asignaturas) {
					HorasAsignatura horasAsignatura = new HorasAsignatura();
					horasAsignatura.setAsignatura(asignatura);
					LocalDate actual = centro.getInicioCurso();
					while (actual.isBefore(centro.getFinCurso())) {
						Periodo periodo = new Periodo(actual.getMonthValue(), actual.getYear());
						Horas horas = new Horas(); 
						horas.setPeriodo(periodo);
						horas.setPrevistas(centro.getSesionesPrevistas(actual.getMonthValue(), actual.getYear(), asignatura).size());
						horas.setElegibles(centro.getSesionesElegibles(actual.getMonthValue(), actual.getYear(), asignatura).size());
						horasAsignatura.getHoras().put(periodo.toString(), horas);
						actual = LocalDate.of(actual.getYear(), actual.getMonthValue(), 1).plusMonths(1);
						updateProgress(hecho++, tiempoTotal);
					}
					horas.add(horasAsignatura);
				}

				return null;
			}

		};
		calcularButton.disableProperty().bind(backgroundTask.runningProperty());
		calcularProgress.progressProperty().bind(backgroundTask.progressProperty());
		
		new Thread(backgroundTask).start();

	}

}
