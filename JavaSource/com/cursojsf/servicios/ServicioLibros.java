package com.cursojsf.servicios;

import java.util.ArrayList;
import java.util.List;

import com.cursojsf.entidades.Libro;

public class ServicioLibros {
	private static List<Libro> libros;

	private static void cargarLibros() {
		if (libros == null) {
			libros = new ArrayList<Libro>();

			libros.add(new Libro(1, ServicioCategorias.buscarCategoria(100),
					ServicioAutores.buscarAutor(1), "ciencia", 2000, 1500));
			libros.add(new Libro(2, ServicioCategorias.buscarCategoria(200),
					ServicioAutores.buscarAutor(2), "tecnologia", 2010, 12000));
		}

	}

	public static void insertar(Libro libro) throws Exception {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarLibros();
		if (buscarLibro(libro.getCodigo()) == null) {
			libros.add(libro);
		} else {
			throw new Exception("El libro ya existe, no se agrega");
		}
	}

	public static void actualizar(Libro libro) throws Exception {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarLibros();
		Libro modificado = buscarLibro(libro.getCodigo());
		if (modificado != null) {
			modificado.setAnio(libro.getAnio());
			modificado.setAutor(libro.getAutor());
			modificado.setCategoria(libro.getCategoria());
			modificado.setCodigo(libro.getCodigo());
			modificado.setTitulo(libro.getTitulo());
			modificado.setPrecio(libro.getPrecio());
			modificado.setCategoria(libro.getCategoria());
		} else {
			throw new Exception("No existe el libro que desea modificar");
			
		}
	}

	public static void eliminar(Libro libro)throws Exception {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Libro encontrado = buscarLibro(libro.getCodigo());
		if (encontrado != null) {
			libros.remove(encontrado);
		} else {
			throw new Exception("No existe el libro que desea eliminar ");
		}
	}

	public static Libro buscarLibro(int codigo) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarLibros();
		for (Libro lib : libros) {
			if (lib.getCodigo() == codigo) {
				return lib;
			}
		}
		return null;
	}

	public static List<Libro> recuperarTodos() {
		cargarLibros();
		return libros;
	}
}
