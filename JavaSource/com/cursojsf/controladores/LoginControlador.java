package com.cursojsf.controladores;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import com.cursojsf.entidades.Usuario;
import com.cursojsf.servicios.ServicioUsuarios;

@RequestScoped
@ManagedBean
public class LoginControlador {
	
	private Usuario usuario;
	//Para pasar un atributo de una clase a otra
    //1.Declarar un atributo ManagedBean
	//2.Getters y Setters
	//3.@ManagedProperty con el nombre del ManagedBean como si lo fuera a llamar desde la página
	@ManagedProperty("#{usuarioDataManager}")
	private UsuarioDataManager udm;
	
	public UsuarioDataManager getUdm() {
		return udm;
	}

	public void setUdm(UsuarioDataManager udm) {
		this.udm = udm;
	}

	public LoginControlador(){
		this.usuario=new Usuario();
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String validarLogin(){
		boolean resultado = ServicioUsuarios.validarUsuario(usuario);
		if(resultado){
			udm.setUsuario(usuario);
			return "cliente.xhtml";
		}
		
		else {
			FacesMessage mensaje= new FacesMessage(FacesMessage.SEVERITY_ERROR,"infoTributaria o password incorrectos", null);
			FacesContext.getCurrentInstance().addMessage("frm1", mensaje);
			return " ";
		}
		
	}
}
