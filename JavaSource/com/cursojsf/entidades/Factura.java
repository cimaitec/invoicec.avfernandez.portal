package com.cursojsf.entidades;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;

public class Factura {
	private int numero;//caja de texto
	private Cliente cliente;
	@Future(message = "La fecha no puede ser mayor al dia de hoy")
	private Date fecha;//p:calendar pattern="dd/MM/yyyy"
	private List<DetalleFactura> detalles;
	private double total;//suma de subtotal de la lista de detalles
	
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public List<DetalleFactura> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleFactura> detalles) {
		this.detalles = detalles;
	}
	
	
}
