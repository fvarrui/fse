package es.iesdpm.fse.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

@XmlType
public class Profesor extends Identificable implements Comparable<Profesor> {
	private StringProperty nombre;
	private StringProperty apellidos;
	private ListProperty<Grupo> tutorias;
	private ListProperty<Asignatura> asignaturas;
	private ListProperty<Falta> faltas;

	public Profesor() {
		super();
		nombre = new SimpleStringProperty(this, "nombre");
		apellidos = new SimpleStringProperty(this, "apellidos");
		tutorias = new SimpleListProperty<>(this, "tutorias", FXCollections.observableArrayList());
		tutorias.get().addListener((ListChangeListener.Change<? extends Grupo> c) -> {
			while (c.next()) {
				for (Grupo g : c.getAddedSubList()) {
					g.setTutor(Profesor.this);
				}
				for (Grupo g : c.getRemoved()) {
					g.setTutor(null);
				}
			}
		});
		asignaturas = new SimpleListProperty<>(this, "asignaturas", FXCollections.observableArrayList());
		asignaturas.get().addListener((ListChangeListener.Change<? extends Asignatura> c) -> {
			while (c.next()) {
				for (Asignatura a : c.getAddedSubList()) {
					a.setProfesor(Profesor.this);
				}
				for (Asignatura a : c.getRemoved()) {
					a.setProfesor(null);
				}
			}
		});
		faltas = new SimpleListProperty<>(this, "faltas", FXCollections.observableArrayList());
	}

	public StringProperty nombreProperty() {
		return this.nombre;
	}

	@XmlAttribute
	public String getNombre() {
		return this.nombreProperty().get();
	}

	public void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}
	
	public StringProperty apellidosProperty() {
		return this.apellidos;
	}

	@XmlAttribute
	public String getApellidos() {
		return this.apellidosProperty().get();
	}

	public void setApellidos(final String apellidos) {
		this.apellidosProperty().set(apellidos);
	}

	public ListProperty<Grupo> tutoriasProperty() {
		return this.tutorias;
	}

	@XmlIDREF
	@XmlElement(name = "grupo")
	@XmlElementWrapper(name = "tutorias")	
	public ObservableList<Grupo> getTutorias() {
		return this.tutoriasProperty().get();
	}
	
	public void setTutorias(final ObservableList<Grupo> tutorias) {
		this.tutoriasProperty().set(tutorias);
	}
	
	public ListProperty<Asignatura> asignaturasProperty() {
		return this.asignaturas;
	}

	@XmlIDREF
	@XmlElement(name="asignatura")
	@XmlElementWrapper(name="asignaturas")	
	public ObservableList<Asignatura> getAsignaturas() {
		return this.asignaturasProperty().get();
	}

	public void setAsignaturas(final ObservableList<Asignatura> asignaturas) {
		this.asignaturasProperty().set(asignaturas);
	}

	public ListProperty<Falta> faltasProperty() {
		return this.faltas;
	}

	@XmlElement(name="falta")
	@XmlElementWrapper(name="faltas")	
	public ObservableList<Falta> getFaltas() {
		return this.faltasProperty().get();
	}

	public void setFaltas(final ObservableList<Falta> faltas) {
		this.faltasProperty().set(faltas);
	}
	
	@Override
	public String toString() {
		return (getApellidos() + ", " + getNombre()).trim();
	}

	@Override
	public int compareTo(Profesor o) {
		return (getApellidos().compareToIgnoreCase(o.getApellidos()));
	}

}
