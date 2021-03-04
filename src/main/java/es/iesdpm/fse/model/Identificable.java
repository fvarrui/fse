package es.iesdpm.fse.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Identificable {
	private StringProperty identificador;

	public Identificable() {
		identificador = new SimpleStringProperty(this, "identificador");
	}
	
	
	public StringProperty identificadorProperty() {
		return this.identificador;
	}

	@XmlAttribute(name="id")
	@XmlID
	public String getIdentificador() {
		return this.identificadorProperty().get();
	}

	public void setIdentificador(final String identificador) {
		this.identificadorProperty().set(identificador);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this.getClass().equals(obj.getClass())) {
			Identificable p = (Identificable) obj;
			if (getIdentificador() != null) return getIdentificador().equals(p.getIdentificador());
		}
		return super.equals(obj);
	}

}
