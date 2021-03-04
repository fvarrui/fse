package es.iesdpm.fse.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

@XmlType
public class Grupo extends Identificable {
	private StringProperty denominacion;
	private ListProperty<Asignatura> asignaturas;
	private ObjectProperty<Profesor> tutor;
	private ObjectProperty<Nivel> nivel;

	public Grupo() {
		super();
		denominacion = new SimpleStringProperty(this, "denominacion");
		asignaturas = new SimpleListProperty<>(this, "asignaturas", FXCollections.observableArrayList());
		asignaturas.get().addListener((ListChangeListener.Change<? extends Asignatura> c) -> {
			while (c.next()) {
				for (Asignatura a : c.getRemoved()) {
					asignaturas.get(0).getSesiones().addAll(a.getSesiones());
				}
			}
		});		
		tutor = new SimpleObjectProperty<>(this, "tutor");
		tutor.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) oldValue.tutoriasProperty().remove(Grupo.this);
			if (newValue != null && !newValue.tutoriasProperty().contains(Grupo.this)) newValue.tutoriasProperty().add(Grupo.this);
		});
		nivel = new SimpleObjectProperty<>(this, "nivel");
	}

	public StringProperty denominacionProperty() {
		return this.denominacion;
	}

	@XmlAttribute
	public String getDenominacion() {
		return this.denominacionProperty().get();
	}

	public void setDenominacion(final String denominacion) {
		this.denominacionProperty().set(denominacion);
	}

	public ListProperty<Asignatura> asignaturasProperty() {
		return this.asignaturas;
	}

	@XmlElement(name = "asignatura")
	@XmlElementWrapper(name = "asignaturas")
	public ObservableList<Asignatura> getAsignaturas() {
		return this.asignaturasProperty().get();
	}

	public void setAsignaturas(final ObservableList<Asignatura> asignaturas) {
		this.asignaturasProperty().set(asignaturas);
	}

	public ObjectProperty<Profesor> tutorProperty() {
		return this.tutor;
	}

	@XmlIDREF
	public Profesor getTutor() {
		return this.tutorProperty().get();
	}

	public void setTutor(final Profesor tutor) {
		this.tutorProperty().set(tutor);
	}

	@Override
	public String toString() {
		return getIdentificador();
	}

	public ObjectProperty<Nivel> nivelProperty() {
		return this.nivel;
	}

	@XmlAttribute
	public Nivel getNivel() {
		return this.nivelProperty().get();
	}

	public void setNivel(final Nivel nivel) {
		this.nivelProperty().set(nivel);
	}

}
