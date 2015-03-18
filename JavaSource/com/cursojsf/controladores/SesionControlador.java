package com.cursojsf.controladores;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean
public class SesionControlador {
	@SuppressWarnings("unused")
	private String Usuario= "";
	
	public void cerrarSesion(){
		//1. Borrar mis objetos de sesion
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioDataManager");
		//2. Redireccionar a login
		//una puede ser con un return
		try{
		FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/Administrador/Cuenta/Login.jsf");
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
