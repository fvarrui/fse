package es.iesdpm.fse.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

@XmlType
public class SesionSemanal {
	private ObjectProperty<DiaSemana> dia;
	private IntegerProperty hora;

	public SesionSemanal(DiaSemana dia, Integer hora) {
		this.dia = new SimpleObjectProperty<>(this, "dia", dia);
		this.hora = new SimpleIntegerProperty(this, "hora", hora);
	}

	public SesionSemanal() {
		this(null, 0);
	}

	public ObjectProperty<DiaSemana> diaProperty() {
		return this.dia;
	}

	@XmlAttribute
	public DiaSemana getDia() {
		return this.diaProperty().get();
	}

	public void setDia(final DiaSemana dia) {
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
	public String toString() {
		return this.getDia() + " a " + this.getHora() + "ª";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof SesionSemanal) {
			SesionSemanal s = (SesionSemanal) obj;
			return getDia().equals(s.getDia()) && getHora() == s.getHora();
		}
		return super.equals(obj);		
	}

}
