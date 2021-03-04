package es.iesdpm.fse.ui.profesores;

import es.iesdpm.fse.model.Falta;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class HoraCellValueFactory implements Callback<TableColumn.CellDataFeatures<Falta,Boolean>, ObservableValue<Boolean>> {
	
	private Integer hora;
	
	public HoraCellValueFactory(int hora) {
		this.hora = hora;
	}

	@Override
	public ObservableValue<Boolean> call(CellDataFeatures<Falta, Boolean> param) {
		BooleanProperty bool = new SimpleBooleanProperty(param.getValue().horasProperty().contains(hora));
		bool.addListener((observable, oldValue, newValue) -> {
			if (newValue) {
				param.getValue().getHoras().add(hora); 
			} else {
				param.getValue().getHoras().remove(hora);
			}
		});
		return bool;
	}

}
