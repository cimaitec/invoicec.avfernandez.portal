package com.cursojsf.controladores;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.cursojsf.entidades.Cliente;
import com.cursojsf.entidades.Nacionalidad;
import com.cursojsf.servicios.ServicioClientes;
import com.cursojsf.servicios.ServicioNacionalidad;

@ViewScoped
@ManagedBean
public class ClienteControlador {
	private Cliente cliente;
	private String valorelegido;
	private List<Nacionalidad> nac;
	private List<Cliente> cli;
	@ManagedProperty("#{usuarioDataManager}")
	private UsuarioDataManager udm;
	private boolean mostrarBoton;
	
	public void mostrar(){
		mostrarBoton = true;
	}
	public void ocultar(){
		mostrarBoton = false;
	}
	
	public boolean isMostrarBoton() {
		return mostrarBoton;
	}

	public void setMostrarBoton(boolean mostrarBoton) {
		this.mostrarBoton = mostrarBoton;
	}
	
	public UsuarioDataManager getUdm() {
		return udm;
	}
	public void setUdm(UsuarioDataManager udm) {
		this.udm = udm;
	}
	public List<Cliente> getCli() {
		return cli;
	}
	public void setCli(List<Cliente> cli) {
		this.cli = cli;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getValorelegido() {
		return valorelegido;
	}
	public void setValorelegido(String valorelegido) {
		this.valorelegido = valorelegido;
	}
	public List<Nacionalidad> getNac() {
		return nac;
	}
	public void setNac(List<Nacionalidad> nac) {
		this.nac = nac;
	}
	
	public ClienteControlador(){
		cliente = new Cliente();
		nac = ServicioNacionalidad.recuperarTodos();
		cli = ServicioClientes.recuperarTodos();
	}
	
	public void insertar(){
		try {
			cliente.setNacionalidad(ServicioNacionalidad.buscarNacionalidad(Integer.parseInt(valorelegido)));
			ServicioClientes.insertar(cliente);
			cliente = new Cliente();
			System.out.println("El usuario: " + udm.getUsuario().getLogin());
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,"Cliente insertado con éxito",null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			//EXITO
		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
			//ERROR
		}
	}
	
	public void actualizar(){
		try {
			cliente.setNacionalidad(ServicioNacionalidad.buscarNacionalidad(Integer.parseInt(valorelegido)));
			ServicioClientes.actualizar(cliente);
			cliente = new Cliente();
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,"Cliente actualizado con éxito",null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		} catch (Exception e) {
			// TODO: handle exception
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
			//ERROR
		}
	}
	
	public void eliminar(){
		try {
			//Para eliminar no es necesario setear la nacionalidad del registro
			//cliente.setNacionalidad(ServicioNacionalidad.buscarNacionalidad(Integer.parseInt(valorelegido)));
			ServicioClientes.eliminar(cliente);
			cliente = new Cliente();
		} catch (Exception e) {
			// TODO: handle exception
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
			e.printStackTrace();
			//ERROR
		}
	}
	
	public void procesar(){
		RequestContext.getCurrentInstance().equals("dlg1.show()");
	}

}
