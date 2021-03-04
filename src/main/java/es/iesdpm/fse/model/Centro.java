package es.iesdpm.fse.model;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.iesdpm.fse.utils.DateUtils;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

@XmlType
@XmlRootElement
public class Centro {
	private StringProperty denominacion;
	private ObjectProperty<LocalDate> inicioCurso;
	private ObjectProperty<LocalDate> finCurso;
	private ListProperty<LocalDate> festivos;
	private ListProperty<Profesor> profesores;
	private ListProperty<Grupo> grupos;

	private Map<String, Profesor> profesoresMap = new HashMap<>();
	private Map<String, Grupo> gruposMap = new HashMap<>();

	public Centro() {
		denominacion = new SimpleStringProperty(this, "denominacion");
		inicioCurso = new SimpleObjectProperty<>(this, "inicioCurso");
		finCurso = new SimpleObjectProperty<>(this, "finCurso");
		festivos = new SimpleListProperty<>(this, "festivos", FXCollections.observableArrayList());

		profesores = new SimpleListProperty<>(this, "profesores", FXCollections.observableArrayList());
		profesores.get().addListener((ListChangeListener.Change<? extends Profesor> c) -> {
			while (c.next()) {
				for (Profesor p : c.getAddedSubList()) {
					profesoresMap.put(p.getIdentificador(), p);
				}
				for (Profesor p : c.getRemoved()) {
					profesoresMap.remove((Object) p);
					for (int i = p.getAsignaturas().size() - 1; i >= 0; i--) {
						p.getAsignaturas().get(i).setProfesor(null);
					}
				}
			}
		});

		grupos = new SimpleListProperty<>(this, "grupos", FXCollections.observableArrayList());
		grupos.get().addListener((ListChangeListener.Change<? extends Grupo> c) -> {
			while (c.next()) {
				for (Grupo g : c.getAddedSubList()) {
					gruposMap.put(g.getIdentificador(), g);
				}
				for (Grupo g : c.getRemoved()) {
					gruposMap.remove((Object) g);
					for (int i = g.getAsignaturas().size() - 1; i >= 0; i--) {
						g.getAsignaturas().get(i).setProfesor(null);
						g.setTutor(null);
					}
				}
			}
		});

	}

	public void save(File file) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Centro.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(this, file);
	}

	public static Centro read(File file) throws Exception {
		JAXBContext context = JAXBContext.newInstance(Centro.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (Centro) unmarshaller.unmarshal(file);
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

	public ObjectProperty<LocalDate> inicioCursoProperty() {
		return this.inicioCurso;
	}

	@XmlAttribute(name = "inicio")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getInicioCurso() {
		return this.inicioCursoProperty().get();
	}

	public void setInicioCurso(final LocalDate inicioCurso) {
		this.inicioCursoProperty().set(inicioCurso);
	}

	public ObjectProperty<LocalDate> finCursoProperty() {
		return this.finCurso;
	}

	@XmlAttribute(name = "fin")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getFinCurso() {
		return this.finCursoProperty().get();
	}

	public void setFinCurso(final LocalDate finCurso) {
		this.finCursoProperty().set(finCurso);
	}

	public ListProperty<LocalDate> festivosProperty() {
		return this.festivos;
	}

	@XmlElement(name = "dia")
	@XmlElementWrapper(name = "festivos")
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public ObservableList<LocalDate> getFestivos() {
		return this.festivosProperty().get();
	}

	public void setFestivos(final ObservableList<LocalDate> festivos) {
		this.festivosProperty().set(festivos);
	}

	public ListProperty<Profesor> profesoresProperty() {
		return this.profesores;
	}

	@XmlElement(name = "profesor")
	@XmlElementWrapper(name = "profesores")
	public ObservableList<Profesor> getProfesores() {
		return this.profesoresProperty().get();
	}

	public void setProfesores(final ObservableList<Profesor> profesores) {
		this.profesoresProperty().set(profesores);
	}

	public ListProperty<Grupo> gruposProperty() {
		return this.grupos;
	}

	@XmlElement(name = "grupo")
	@XmlElementWrapper(name = "grupos")
	public ObservableList<Grupo> getGrupos() {
		return this.gruposProperty().get();
	}

	public void setGrupos(final ObservableList<Grupo> grupos) {
		this.gruposProperty().set(grupos);
	}

	public Grupo getGrupo(String identificador) {
		return gruposMap.get(identificador);
	}

	public Profesor getProfesor(String identificador) {
		return profesoresMap.get(identificador);
	}

	public ObservableList<Sesion> getSesionesPrevistas(int mes, int anyo, Asignatura asignatura) {
		ObservableList<Sesion> sesiones = FXCollections.observableArrayList();
		LocalDate dia = LocalDate.of(anyo, mes, 1);
		LocalDate ultimo = dia.plusMonths(1);
		while (dia.isBefore(ultimo)) {
			if (dia.compareTo(getInicioCurso()) >= 0 && dia.compareTo(getFinCurso()) <= 0 && !DateUtils.isWeekend(dia)
					&& !getFestivos().contains(dia)) {
				int weekday = DateUtils.getWeekday(dia);
				ObservableList<SesionSemanal> sesionesDia = asignatura.getSesionesPorDia(weekday);
				for (SesionSemanal sesionDia : sesionesDia) {
					Sesion sesion = new Sesion(dia, sesionDia.getHora());
					sesiones.add(sesion);
				}
			}
			dia = dia.plusDays(1);
		}
		return sesiones;
	}

	public ObservableList<Sesion> getSesionesElegibles(int mes, int anyo, Asignatura asignatura) {
		ObservableList<Sesion> elegibles = getSesionesPrevistas(mes, anyo, asignatura);
		if (asignatura.getProfesor() != null) {
			elegibles.removeAll(asignatura.getProfesor().getFaltas());
		}
		return elegibles;
	}

}
