package com.cursojsf.servicios;

import java.util.ArrayList;
import java.util.List;

import com.cursojsf.entidades.Nacionalidad;

public class ServicioNacionalidad {

	private static List<Nacionalidad> nacionalidades;

	private static void cargarNacionalidades() {
		if (nacionalidades == null) {
			nacionalidades = new ArrayList<Nacionalidad>();
			nacionalidades.add(new Nacionalidad(1, "Ecuador", "ecuatoriano"));
			nacionalidades.add(new Nacionalidad(2, "Peru", "peruano"));
		}
	}

	public static void insertar(Nacionalidad nacionalidad) {
		cargarNacionalidades();
		if (buscarNacionalidad(nacionalidad.getId()) == null) {
			nacionalidades.add(nacionalidad);
		} else {
			System.out.println("La nacionalidad ya existe, no se agrega");
		}
	}

	public static void actualizar(Nacionalidad nacionalidad) {
		cargarNacionalidades();
		Nacionalidad modificado = buscarNacionalidad(nacionalidad.getId());
		if (modificado != null) {
			modificado.setId(nacionalidad.getId());

		} else {
			System.out.println("No existe la nacionalidad que desea modificar");
		}
	}

	public static void eliminar(Nacionalidad nacionalidad) {
		Nacionalidad encontrado = buscarNacionalidad(nacionalidad.getId());
		if (encontrado != null) {
			nacionalidades.remove(encontrado);
		} else {
			System.out.println("No existe la nacionalidad que desea eliminar ");
		}
	}

	public static Nacionalidad buscarNacionalidad(int id) {
		cargarNacionalidades();
		for (Nacionalidad nac : nacionalidades) {
			if (nac.getId() == id) {
				return nac;
			}
		}
		return null;
	}

	public static List<Nacionalidad> recuperarTodos() {
		cargarNacionalidades();
		return nacionalidades;
	}

}
