package es.iesdpm.fse.ui.horas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Periodo {
	private static final String [] MESES = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
	
	private IntegerProperty mes;
	private IntegerProperty anyo;

	public Periodo(int mes, int anyo) {
		this.mes = new SimpleIntegerProperty(this, "mes", mes);
		this.anyo = new SimpleIntegerProperty(this, "anyo", anyo);
	}

	public IntegerProperty mesProperty() {
		return this.mes;
	}

	public int getMes() {
		return this.mesProperty().get();
	}

	public void setMes(final int mes) {
		this.mesProperty().set(mes);
	}

	public IntegerProperty anyoProperty() {
		return this.anyo;
	}

	public int getAnyo() {
		return this.anyoProperty().get();
	}

	public void setAnyo(final int anyo) {
		this.anyoProperty().set(anyo);
	}
	
	@Override
	public String toString() {
		return MESES[getMes() - 1] + " " + getAnyo();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof Periodo) {
			Periodo p = (Periodo) obj;
			if (getMes() == p.getMes() && getAnyo() == getAnyo()) {
				return true;
			}
		}
		return super.equals(obj);	
	}

}
