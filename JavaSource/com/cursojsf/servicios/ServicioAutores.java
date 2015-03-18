package com.cursojsf.servicios;

import java.util.ArrayList;
import java.util.List;

import com.cursojsf.entidades.Autor;

public class ServicioAutores {

	private static List<Autor> autores;

	private static void cargarAutores() {
		if (autores == null) {
			autores = new ArrayList<Autor>();
			autores.add(new Autor(1, "Martin", "Romero", ServicioNacionalidad
					.buscarNacionalidad(1)));
			autores.add(new Autor(2, "Francisco", "Montenegro",
					ServicioNacionalidad.buscarNacionalidad(2)));
		}
	}

	public static void insertar(Autor autor) throws Exception {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarAutores();
		if ((buscarAutor(autor.getCodigo()) == null)) {
			autores.add(autor);
		} else {
			throw new Exception("El autor ya existe");
			//System.out.println("El Autor ya existe, no se agrega");
		}
	}

	public static void actualizar(Autor autor) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarAutores();
		Autor modificado = buscarAutor(autor.getCodigo());
		if (modificado != null) {
			modificado.setNombre(autor.getNombre());
			modificado.setApellido(autor.getApellido());
			modificado.setNacionalidad(autor.getNacionalidad());

		} else {
			System.out.println("No existe el Autor que desea modificar");
		}
	}

	public static void eliminar(Autor autor) {
		Autor encontrado = buscarAutor(autor.getCodigo());
		if (encontrado != null) {
			autores.remove(encontrado);
		} else {
			System.out.println("No existe el Autor que desea eliminar ");
		}
	}

	public static Autor buscarAutor(int codigo) {
		cargarAutores();
		for (Autor aut : autores) {
			if (aut.getCodigo() == codigo) {
				return aut;
			}
		}
		return null;
	}

	public static List<Autor> recuperarTodos() {
		cargarAutores();
		return autores;
	}

}
