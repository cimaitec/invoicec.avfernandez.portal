package com.cursojsf.test;

import java.util.List;

import com.cursojsf.entidades.Autor;
import com.cursojsf.servicios.ServicioAutores;
import com.cursojsf.servicios.ServicioNacionalidad;

public class TestAutor {

	public static void main(String[] args) {
		/*
		 * System.out.println("**************Antes de Ingresar************");
		 * autoresIngresados(); ingresarAutor();
		 * System.out.println("**************Despues de Ingresar************");
		 * autoresIngresados();
		 */
		/*
		 * System.out.println("**************Antes de Eliminar************");
		 * autoresIngresados(); eliminarAutor();
		 * System.out.println("**************Antes de Ingresar************");
		 * autoresIngresados();
		 */
		System.out.println("************Antes de Actualizar********");
		autoresIngresados();
		actualizarAutor();
		System.out.println("**************Despues de Actualizar************");
		autoresIngresados();
	}

	public static void autoresIngresados() {
		imprimirAutores(ServicioAutores.recuperarTodos());
	}

	public static void imprimirAutores(List<Autor> autores) {

		for (Autor autor : autores) {
			System.out.println("Autor ID: " + autor.getCodigo()
					+ "\nAutor Nombre: " + autor.getNombre());
			System.out.println("*******************************************");
		}
	}

	public static void ingresarAutor() throws Exception {
		Autor autor = new Autor(3, "Patti", "Galvez",
				ServicioNacionalidad.buscarNacionalidad(1));
		ServicioAutores.insertar(autor);
	}

	public static void eliminarAutor() {
		Autor autor = new Autor();
		autor.setCodigo(1);
		ServicioAutores.eliminar(autor);
	}

	public static void actualizarAutor() {
		Autor autor = new Autor(1, "Alejandro", "Cisneros",
				ServicioNacionalidad.buscarNacionalidad(2)); 
		ServicioAutores.actualizar(autor);
	}
}
