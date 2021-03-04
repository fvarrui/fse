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
import javafx.collections.ObservableList;

@XmlType
public class Asignatura extends Identificable {
	private StringProperty denominacion;
	private ListProperty<SesionSemanal> sesiones;
	private ObjectProperty<Profesor> profesor;

	public Asignatura() {
		super();
		denominacion = new SimpleStringProperty(this, "denominacion");
		sesiones = new SimpleListProperty<>(this, "sesiones", FXCollections.observableArrayList());
		profesor = new SimpleObjectProperty<>(this, "profesor");
		profesor.addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) oldValue.asignaturasProperty().remove(Asignatura.this);
			if (newValue != null && !newValue.asignaturasProperty().contains(Asignatura.this)) newValue.asignaturasProperty().add(Asignatura.this);
		});
	}

	public ListProperty<SesionSemanal> sesionesProperty() {
		return this.sesiones;
	}

	@XmlElement(name="sesion")
	@XmlElementWrapper(name="sesiones")	
	public ObservableList<SesionSemanal> getSesiones() {
		return this.sesionesProperty().get();
	}

	public void setSesiones(final ObservableList<SesionSemanal> sesiones) {
		this.sesionesProperty().set(sesiones);
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

	public ObjectProperty<Profesor> profesorProperty() {
		return this.profesor;
	}

	@XmlIDREF
	public Profesor getProfesor() {
		return this.profesorProperty().get();
	}

	public void setProfesor(final Profesor profesor) {
		this.profesorProperty().set(profesor);
	}
	
	public ObservableList<SesionSemanal> getSesionesPorDia(final DiaSemana diaSemana) {
		return getSesiones().filtered(s -> s.getDia() == diaSemana);
	}

	public ObservableList<SesionSemanal> getSesionesPorDia(final int diaSemana) {
		return getSesionesPorDia(DiaSemana.values()[diaSemana - 1]);
	}
	
	@Override
	public String toString() {
		return getIdentificador();
	}

}
