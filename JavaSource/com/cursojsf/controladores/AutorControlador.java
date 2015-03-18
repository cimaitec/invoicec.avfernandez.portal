package com.cursojsf.controladores;


import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.cursojsf.entidades.Autor;
import com.cursojsf.entidades.Nacionalidad;
import com.cursojsf.servicios.ServicioAutores;
import com.cursojsf.servicios.ServicioNacionalidad;

@ViewScoped
@ManagedBean
public class AutorControlador {

	private Autor autor;
	private List<Autor> autores;
	private Nacionalidad nacElegida;
	private List<Nacionalidad> nacionalidades;

	public AutorControlador(){
		autor = new Autor();
		autores = ServicioAutores.recuperarTodos();
		nacElegida = new Nacionalidad();
		nacionalidades = ServicioNacionalidad.recuperarTodos();	
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Nacionalidad getNacElegida() {
		return nacElegida;
	}

	public void setNacElegida(Nacionalidad nacElegida) {
		this.nacElegida = nacElegida;
	}

	public List<Nacionalidad> getNacionalidades() {
		return nacionalidades;
	}

	public void setNacionalidades(List<Nacionalidad> nacionalidades) {
		this.nacionalidades = nacionalidades;
	}

	public List<Autor> getAutores() {
		return autores;
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}

	public void insertar() {
		try {
			Nacionalidad nac = ServicioNacionalidad
					.buscarNacionalidad(nacElegida.getId());
			autor.setNacionalidad(nac);
			if (autor.getCodigo() <= 0)
				mensajes(102);
			else{	
			ServicioAutores.insertar(autor);
			autor = new Autor();
			nacElegida = new Nacionalidad();
			mensajes(100);
			}

		} catch (Exception e) {
			mensajes(101);
		}

	}
	
	public void actualizar() {
		try {
			Nacionalidad nac = ServicioNacionalidad
					.buscarNacionalidad(nacElegida.getId());
			autor.setNacionalidad(nac);
			if (autor.getCodigo() <= 0)
				mensajes(102);
			else{	
			ServicioAutores.actualizar(autor);
			autor = new Autor();
			nacElegida = new Nacionalidad(); 
			//mensajes(100);
			}

		} catch (Exception e) {
			mensajes(101);
		}

	}
	
	public void eliminar() {
		try {
			Nacionalidad nac = ServicioNacionalidad
					.buscarNacionalidad(nacElegida.getId());
			autor.setNacionalidad(nac);
			
			ServicioAutores.eliminar(autor);
			autor = new Autor();
			nacElegida = new Nacionalidad(); 
			//mensajes(100);
			

		} catch (Exception e) {
			mensajes(101);
		}

	}
	
	private void mensajes(int valor){
		FacesMessage mensaje = null;
		if (valor == 100)		
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,"Autor Ingresado con éxito",null);
		
		if (valor == 101)
			mensaje = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error al ingresar",null);
		
		if (valor == 102)
			mensaje = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Debe Ingresar un valor mayor a cero",null);
		
		FacesContext.getCurrentInstance().addMessage(null, mensaje); //El primer argumento es el item al cual está atado
		
	}

}
