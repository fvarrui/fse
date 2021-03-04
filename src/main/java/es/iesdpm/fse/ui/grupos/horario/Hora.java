package es.iesdpm.fse.ui.grupos.horario;

import es.iesdpm.fse.model.Asignatura;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Hora {
	private IntegerProperty hora;
	private ObjectProperty<Asignatura> lunes;
	private ObjectProperty<Asignatura> martes;
	private ObjectProperty<Asignatura> miercoles;
	private ObjectProperty<Asignatura> jueves;
	private ObjectProperty<Asignatura> viernes;

	public Hora(int hora) {
		this.hora = new SimpleIntegerProperty(this, "hora", hora);
		this.lunes = new SimpleObjectProperty<>(this, "lunes");
		this.martes = new SimpleObjectProperty<>(this, "martes");
		this.miercoles = new SimpleObjectProperty<>(this, "miercoles");
		this.jueves = new SimpleObjectProperty<>(this, "jueves");
		this.viernes = new SimpleObjectProperty<>(this, "viernes");
	}

	public IntegerProperty horaProperty() {
		return this.hora;
	}

	public int getHora() {
		return this.horaProperty().get();
	}

	public void setHora(final int hora) {
		this.horaProperty().set(hora);
	}

	public ObjectProperty<Asignatura> lunesProperty() {
		return this.lunes;
	}

	public Asignatura getLunes() {
		return this.lunesProperty().get();
	}

	public void setLunes(final Asignatura lunes) {
		this.lunesProperty().set(lunes);
	}

	public ObjectProperty<Asignatura> martesProperty() {
		return this.martes;
	}

	public Asignatura getMartes() {
		return this.martesProperty().get();
	}

	public void setMartes(final Asignatura martes) {
		this.martesProperty().set(martes);
	}

	public ObjectProperty<Asignatura> miercolesProperty() {
		return this.miercoles;
	}

	public Asignatura getMiercoles() {
		return this.miercolesProperty().get();
	}

	public void setMiercoles(final Asignatura miercoles) {
		this.miercolesProperty().set(miercoles);
	}

	public ObjectProperty<Asignatura> juevesProperty() {
		return this.jueves;
	}

	public Asignatura getJueves() {
		return this.juevesProperty().get();
	}

	public void setJueves(final Asignatura jueves) {
		this.juevesProperty().set(jueves);
	}

	public ObjectProperty<Asignatura> viernesProperty() {
		return this.viernes;
	}

	public Asignatura getViernes() {
		return this.viernesProperty().get();
	}

	public void setViernes(final Asignatura viernes) {
		this.viernesProperty().set(viernes);
	}

}
