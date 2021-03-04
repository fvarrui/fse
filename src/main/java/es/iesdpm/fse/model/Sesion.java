package es.iesdpm.fse.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@XmlType
public class Sesion {
	private ObjectProperty<LocalDate> dia;
	private IntegerProperty hora;

	public Sesion(LocalDate dia, Integer hora) {
		this.dia = new SimpleObjectProperty<>(this, "dia", dia);
		this.hora = new SimpleIntegerProperty(this, "hora", hora);
	}

	public Sesion() {
		this(null, 0);
	}

	public ObjectProperty<LocalDate> diaProperty() {
		return this.dia;
	}

	@XmlAttribute
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getDia() {
		return this.diaProperty().get();
	}

	public void setDia(final LocalDate dia) {
		this.diaProperty().set(dia);
	}

	public IntegerProperty horaProperty() {
		return this.hora;
	}

	@XmlAttribute
	public int getHora() {
		return this.horaProperty().get();
	}

	public void setHora(final int hora) {
		this.horaProperty().set(hora);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Sesion) {
			Sesion s = (Sesion) obj;
			if (getDia() != null && getDia().equals(s.getDia()) && getHora() == s.getHora()) {
				return true;
			}
		}
		if (obj instanceof Falta) {
			Falta f = (Falta) obj;
			if (getDia() != null && getDia().equals(f.getDia()) && f.getHoras().contains(getHora())) {
				return true;
			}
		}
		return super.equals(obj);
	}
	
	
	@Override
	public String toString() {
		return getDia() + " a " + getHora() + "ª";
	}
	
}
