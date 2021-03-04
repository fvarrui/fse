package es.iesdpm.fse.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

@XmlType
public class Falta {
	private ObjectProperty<LocalDate> dia;
	private SetProperty<Integer> horas;

	public Falta() {
		dia = new SimpleObjectProperty<LocalDate>(this, "dia");
		horas = new SimpleSetProperty<>(this, "horas", FXCollections.observableSet());
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

	public SetProperty<Integer> horasProperty() {
		return this.horas;
	}

	@XmlAttribute
	public ObservableSet<Integer> getHoras() {
		return this.horasProperty().get();
	}

	public void setHoras(final ObservableSet<Integer> horas) {
		this.horasProperty().set(horas);
	}
	
	@Override
	public String toString() {
		return getDia() + ": " + getHoras();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Falta) {
			Falta f = (Falta) obj;
			if (getDia() != null && getDia().equals(f.getDia())) {
				return true;
			}
		}
		if (obj instanceof Sesion) {
			Sesion s = (Sesion) obj;
			if (getDia() != null && getDia().equals(s.getDia()) && getHoras().contains(s.getHora())) {
				return true;
			}
		}
		return super.equals(obj);
	}

	public static Falta variasSesiones(LocalDate dia, Integer ... horas) {
		return variasSesiones(dia, Arrays.asList(horas));
	}
	
	public static Falta variasSesiones(LocalDate dia, List<Integer> horas) {
		Falta falta = new Falta();
		falta.setDia(dia);
		falta.getHoras().addAll(horas);
		return falta;
	}

	public static Falta jornadaCompleta(LocalDate dia) {
		return variasSesiones(dia, 1, 2, 3, 4, 5, 6);
	}

}
