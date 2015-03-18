package com.cursojsf.controladores;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.cursojsf.entidades.Cliente;
import com.cursojsf.entidades.DetalleFactura;
import com.cursojsf.entidades.Factura;
import com.cursojsf.entidades.Libro;
import com.cursojsf.servicios.ServicioClientes;
import com.cursojsf.servicios.ServicioFacturas;
import com.cursojsf.servicios.ServicioLibros;

@ViewScoped
@ManagedBean
public class FacturaControlador1 {
	private Cliente cliente;
	private Libro libro;
	private String cedula;
	private int codigolibro;
	private int cantidad;
	private int numfactura;
	private Date fecha; 
	private Factura factura;
	private double totalfactura;
	private List<DetalleFactura> detalle;
	private DetalleFactura detfactobjeto;
	
	public double getTotalfactura() {
		return totalfactura;
	}
	public void setTotalfactura(double totalfactura) {
		this.totalfactura = totalfactura;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public int getNumfactura() {
		return numfactura;
	}
	public void setNumfactura(int numfactura) {
		this.numfactura = numfactura;
	}
	public Factura getFactura() {
		return factura;
	}
	public void setFactura(Factura factura) {
		this.factura = factura;
	}
	public DetalleFactura getDetfactobjeto() {
		return detfactobjeto;
	}
	public void setDetfactobjeto(DetalleFactura detfactobjeto) {
		this.detfactobjeto = detfactobjeto;
	}
	public List<DetalleFactura> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<DetalleFactura> detalle) {
		this.detalle = detalle;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public int getCodigolibro() {
		return codigolibro;
	}
	public void setCodigolibro(int codigolibro) {
		this.codigolibro = codigolibro;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	
	public FacturaControlador1(){
		factura = new Factura();
		cliente = new Cliente();
		libro = new Libro();
		detalle = new ArrayList<DetalleFactura>();
	}
	
	public void buscarCliente(){
		cliente = ServicioClientes.buscarPorCedula(cedula);
		if (cliente == null){
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,"El cliente no existe",null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	public void buscarLibro(){
		libro = ServicioLibros.buscarLibro(codigolibro);
		if (libro == null){
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,"El libro no existe",null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
	}
	
	public void ingresarCantidad(){
		if (cantidad > 0 && codigolibro > 0){
			detfactobjeto = new DetalleFactura();
			detfactobjeto.setCantidad(cantidad);
			detfactobjeto.setLibro(libro);
			detfactobjeto.setSubtotal(libro.getPrecio() * cantidad);
			detalle.add(detfactobjeto);
			libro = new Libro();
			cantidad = 0;
			codigolibro = 0;
			totalfactura = totalfactura + (detfactobjeto.getSubtotal());
		}
	}
	
	public void guardarRegistros(){
		try{
			factura = new Factura();
			factura.setCliente(cliente);
			factura.setDetalles(detalle);
			factura.setNumero(numfactura);
			factura.setFecha(fecha);
			ServicioFacturas.insertar(factura);
			totalfactura = 0;
			cliente = new Cliente();
			libro = new Libro();
			detalle = new ArrayList<DetalleFactura>();
			fecha = null;
			totalfactura = 0;
			numfactura = 0;
		}catch (Exception e) {
			FacesMessage mensaje = new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(), null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
		
		
	}
	
}
