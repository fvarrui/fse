package es.iesdpm.fse.ui.horas;

import es.iesdpm.fse.model.Asignatura;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class HorasAsignatura {
	private ObjectProperty<Asignatura> asignatura;
	private MapProperty<String, Horas> horas;

	public HorasAsignatura() {
		asignatura = new SimpleObjectProperty<Asignatura>(this, "asignatura");
		horas = new SimpleMapProperty<>(this, "horas", FXCollections.observableHashMap());
	}

	public ObjectProperty<Asignatura> asignaturaProperty() {
		return this.asignatura;
	}

	public Asignatura getAsignatura() {
		return this.asignaturaProperty().get();
	}

	public void setAsignatura(final Asignatura asignatura) {
		this.asignaturaProperty().set(asignatura);
	}

	public MapProperty<String, Horas> horasProperty() {
		return this.horas;
	}

	public ObservableMap<String, Horas> getHoras() {
		return this.horasProperty().get();
	}

	public void setHoras(final ObservableMap<String, Horas> horas) {
		this.horasProperty().set(horas);
	}
	
	@Override
	public String toString() {
		return getAsignatura() + ": " + getHoras();
	}

}
