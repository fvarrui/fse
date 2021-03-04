package es.iesdpm.fse.ui.profesores;

import javafx.beans.property.ListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class HoraChangeListener implements ChangeListener<Boolean> {
	
	private Integer hora;
	private ListProperty<Integer> horas;
	
	public HoraChangeListener(Integer hora, ListProperty<Integer> horas) {
		this.hora = hora;
		this.horas = horas;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (newValue) {
			horas.add(hora); 		
		} else {
			horas.remove(hora);
		}
	}

}
