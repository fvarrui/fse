package es.iesdpm.fse.ui.horas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Horas {
	private ObjectProperty<Periodo> periodo;
	private IntegerProperty previstas;
	private IntegerProperty elegibles;

	public Horas() {
		periodo = new SimpleObjectProperty<Periodo>(this, "periodo");
		previstas = new SimpleIntegerProperty(this, "previstas", 0);
		elegibles = new SimpleIntegerProperty(this, "elegibles", 0);
	}

	public ObjectProperty<Periodo> periodoProperty() {
		return this.periodo;
	}

	public Periodo getPeriodo() {
		return this.periodoProperty().get();
	}

	public void setPeriodo(final Periodo periodo) {
		this.periodoProperty().set(periodo);
	}

	public IntegerProperty previstasProperty() {
		return this.previstas;
	}

	public int getPrevistas() {
		return this.previstasProperty().get();
	}

	public void setPrevistas(final int previstas) {
		this.previstasProperty().set(previstas);
	}

	public IntegerProperty elegiblesProperty() {
		return this.elegibles;
	}

	public int getElegibles() {
		return this.elegiblesProperty().get();
	}

	public void setElegibles(final int elegibles) {
		this.elegiblesProperty().set(elegibles);
	}
	
	@Override
	public String toString() {
		return "(" + getPrevistas() + ", " + getElegibles() + ")";
	}

}
