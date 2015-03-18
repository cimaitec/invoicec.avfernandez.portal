package com.cursojsf.controladores;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean
public class DialogoControlador {
	private String cajatexto;

	public String getCajatexto() {
		return cajatexto;
	}

	public void setCajatexto(String cajatexto) {
		this.cajatexto = cajatexto;
	}
	
	public DialogoControlador(){
		
	}
	
}
