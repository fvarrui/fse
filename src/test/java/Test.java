

import java.io.File;
import java.time.LocalDate;

import es.iesdpm.fse.model.Asignatura;
import es.iesdpm.fse.model.Centro;
import es.iesdpm.fse.model.DiaSemana;
import es.iesdpm.fse.model.Falta;
import es.iesdpm.fse.model.Grupo;
import es.iesdpm.fse.model.Nivel;
import es.iesdpm.fse.model.Profesor;
import es.iesdpm.fse.model.Sesion;
import es.iesdpm.fse.model.SesionSemanal;
import javafx.collections.ObservableList;

public class Test {

	public static void main(String[] args) throws Exception {

		Centro centro;
		centro = crearGrupo();
		centro.save(new File("ejemplo.xml"));
		centro = Centro.read(new File("ejemplo.xml"));
		
	}
	
	public static Centro crearGrupo() {
		// PROFESORES
		
		Profesor fvargas = new Profesor();
		fvargas.setIdentificador("fvarrui");
		fvargas.setNombre("Francisco");
		fvargas.setApellidos("Vargas Ruiz");
		fvargas.getFaltas().addAll(Falta.jornadaCompleta(LocalDate.of(2016, 9, 28)));
		fvargas.getFaltas().addAll(Falta.variasSesiones(LocalDate.of(2016, 9, 29), 2, 3, 4, 5, 6));

		Profesor bpermar = new Profesor();
		bpermar.setIdentificador("bpermar");
		bpermar.setNombre("Beatriz");
		bpermar.setApellidos("Pérez Martínez");
		
		// ASIGNATURAS
		
		Asignatura aed = new Asignatura();
		aed.setIdentificador("AED");
		aed.setDenominacion("Acceso a datos");
		aed.setProfesor(bpermar);

		Asignatura dad = new Asignatura();
		dad.setIdentificador("DAD");
		dad.setDenominacion("Desarrollo de Interfaces");
		dad.setProfesor(fvargas);
		dad.getSesiones().add(new SesionSemanal(DiaSemana.LUNES, 3));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.LUNES, 4));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.MARTES, 4));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.MIERCOLES, 4));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.MIERCOLES, 5));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.MIERCOLES, 6));
		dad.getSesiones().add(new SesionSemanal(DiaSemana.VIERNES, 4));

		// GRUPOS
		
		Grupo dam2 = new Grupo();
		dam2.setNivel(Nivel.CFGS);
		dam2.setIdentificador("2DAM");
		dam2.setDenominacion("2º CFGS Desarrollo de Aplicaciones Multiplataforma");
		dam2.setTutor(bpermar);
		dam2.getAsignaturas().addAll(aed, dad);

		dam2.setTutor(fvargas);

		// CENTRO
		
		Centro centro = new Centro();
		centro.setDenominacion("IES Domingo Pérez Minik");
		centro.setInicioCurso(LocalDate.of(2016, 9, 12));
		centro.setFinCurso(LocalDate.of(2017, 6, 20));
		centro.getFestivos().add(LocalDate.of(2016, 9, 14));
		centro.getFestivos().add(LocalDate.of(2016, 10, 12));
		centro.getFestivos().add(LocalDate.of(2016, 11, 1));
		centro.getGrupos().add(dam2);
		centro.getProfesores().addAll(fvargas, bpermar);
		
		
		// CALCULOS
		ObservableList<Sesion> previstas = centro.getSesionesPrevistas(9, 2016, dad);
		ObservableList<Sesion> elegibles = centro.getSesionesElegibles(9, 2016, dad);
		
		System.out.println("Elegibles : " + previstas.size() + " " + previstas);
		System.out.println("Faltas    : " + dad.getProfesor().getFaltas());
		System.out.println("Reales    : " + elegibles.size() + " " + elegibles);
		
		return centro;
	}

}
