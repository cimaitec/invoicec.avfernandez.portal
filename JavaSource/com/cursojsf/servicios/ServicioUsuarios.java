package com.cursojsf.servicios;

import java.util.ArrayList;
import java.util.List;

import com.cursojsf.entidades.Usuario;

public class ServicioUsuarios {
	private static List<Usuario> usuarios;

	private static void cargarUsuarios() {
		if (usuarios == null) {
			// aqui cargan todo los usuarios
			usuarios = new ArrayList<Usuario>();
			usuarios.add(new Usuario(1, "Juan", "pwd1"));
			usuarios.add(new Usuario(2, "Mario", "pwd2"));
		}
	}
	

	public static void insertar(Usuario usuario) throws Exception {
		cargarUsuarios();
		if (buscarUsuario(usuario.getCodigo()) == null) {
			usuarios.add(usuario);
		} else {							//excepcion faltaria
		//	System.out.println("El usuario ya existe, no se agrega");
			throw new Exception ("El usuario ya existe, no se agrega");
		}
	}

	public static void actualizar(Usuario usuario) {
		cargarUsuarios();
		Usuario modificado = buscarUsuario(usuario.getCodigo());
		if (modificado != null) {
			modificado.setLogin(usuario.getLogin());
			modificado.setPassword(usuario.getPassword());
		} else {
			System.out.println("No existe el usuario que desea modificar");
		}
	}

	public static void eliminar(Usuario usuario) {
		Usuario encontrado = buscarUsuario(usuario.getCodigo());
		if (encontrado != null) {
			usuarios.remove(encontrado);
		} else {
			System.out.println("No existe el usuario que desea eliminar ");
		}
	}

	public static Usuario buscarUsuario(int codigo) {
		cargarUsuarios();
		for (Usuario usr : usuarios) {
			if (usr.getCodigo() == codigo) {
				return usr;
			}
		}
		return null;
	}

	public static boolean validarUsuario(Usuario usuario) {
		cargarUsuarios();
		for (Usuario usr : usuarios) {
			if (usr.getLogin().equals(usuario.getLogin())
					&& usr.getPassword().equals(usuario.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public static List<Usuario> recuperarTodos() {
		cargarUsuarios();
		return usuarios;
	}

}
