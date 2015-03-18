package com.cursojsf.controladores;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.cursojsf.entidades.Usuario;

@SessionScoped
@ManagedBean
public class UsuarioDataManager {
	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
