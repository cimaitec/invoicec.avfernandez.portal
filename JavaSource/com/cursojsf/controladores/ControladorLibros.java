package com.cursojsf.controladores;

import java.util.List;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.cursojsf.entidades.Autor;
import com.cursojsf.entidades.Categoria;
import com.cursojsf.entidades.Libro;
import com.cursojsf.servicios.ServicioAutores;
import com.cursojsf.servicios.ServicioCategorias;
import com.cursojsf.servicios.ServicioLibros;

@ManagedBean
public class ControladorLibros {
	private Libro beanLibro;
	private List<Categoria> categorias;
	private List<Autor> autores;
	private String valorCategoria; 
	private String valorAutor; 
	private List<Libro> libros; 
	
	
	
	public ControladorLibros(){
		this.beanLibro = new Libro();
		this.categorias=ServicioCategorias.recuperarTodos();
		this.autores=ServicioAutores.recuperarTodos();
		this.libros= ServicioLibros.recuperarTodos();
	}
    
	public void agregarLibro(){
	
			Categoria cat = ServicioCategorias.buscarCategoria(Integer.parseInt(valorCategoria));
			Autor aut = ServicioAutores.buscarAutor(Integer.parseInt(valorAutor));
			beanLibro.setAutor(aut);
			beanLibro.setCategoria(cat);
			try {
				ServicioLibros.insertar(beanLibro);
				//Creo el mensaje
				FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_INFO,"Libro insertado exitosamente",null);
				//Lo agrego a la pila de mensajes de jsf 
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			} catch (Exception e) {
				FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			}
			beanLibro= new Libro();
		}
		

		public void actualizarLibro(){
			Categoria cat = ServicioCategorias.buscarCategoria(Integer.parseInt(valorCategoria));
			Autor aut = ServicioAutores.buscarAutor(Integer.parseInt(valorAutor));
			beanLibro.setAutor(aut);
			beanLibro.setCategoria(cat);
			try {
			ServicioLibros.actualizar(beanLibro);
			FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_INFO,"Libro actualizado exitosamente",null);
			//Lo agrego a la pila de mensajes de jsf 
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		    } catch (Exception e) {
			FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		   }
			
			beanLibro= new Libro();
		}
		
		public void eliminarLibro(){
			try {
			ServicioLibros.eliminar(beanLibro);
			FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_INFO,"Libro eliminado exitosamente",null);
			//Lo agrego a la pila de mensajes de jsf 
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			} catch (Exception e) {
				FacesMessage mensaje= new  FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
				FacesContext.getCurrentInstance().addMessage(null, mensaje);
			   }
				
		}
		
	public Libro getBeanLibro() {
		return beanLibro;
	}
	public void setBeanLibro(Libro beanLibro) {
		this.beanLibro = beanLibro;
	}
	public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public List<Autor> getAutores() {
		return autores;
	}
	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}


	public String getValorCategoria() {
		return valorCategoria;
	}


	public void setValorCategoria(String valorCategoria) {
		this.valorCategoria = valorCategoria;
	}
	
	


	public List<Libro> getLibros() {
		return libros;
	}


	public void setLibros(List<Libro> libros) {
		this.libros = libros;
	}

	public String getValorAutor() {
		return valorAutor;
	}

	public void setValorAutor(String valorAutor) {
		this.valorAutor = valorAutor;
	}
	
	
	
	
}
