package com.cursojsf.controladores;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.cursojsf.entidades.Usuario;
import com.cursojsf.servicios.ServicioUsuarios;
@ViewScoped
@ManagedBean
public class ControladorUsuario {
	private Usuario usuario;
	private List<Usuario> usuarios;

	public ControladorUsuario() {
		usuario = new Usuario();
		this.usuarios = ServicioUsuarios.recuperarTodos();
	}

	public void agregarUsuario() {

		try {
			ServicioUsuarios.insertar(usuario);
			
			//exito
		
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor insertado con exito", null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//error
			
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		usuario = new Usuario();
	}

	public void AcualizarUsuario() {

		try {
			ServicioUsuarios.actualizar(usuario);
			
			//exito
		
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor actualizado con exito", null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//error
			
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		usuario = new Usuario();
	}
	
	
	public void EliminarUsuario() {

		try {
			ServicioUsuarios.eliminar(usuario);
			
			//exito
		
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Autor eliminado con exito", null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//error
			
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		usuario = new Usuario();
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
