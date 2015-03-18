package com.cursojsf.controladores;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cursojsf.entidades.Factura;
import com.cursojsf.servicios.ServicioFacturas;
@ViewScoped
@ManagedBean
public class FacturaDetalleControlador {
	private List<Factura> lsFactura; 
	
	public List<Factura> getLsFactura() {
		return lsFactura;
	}

	public void setLsFactura(List<Factura> lsFactura) {
		this.lsFactura = lsFactura;
	}

	public FacturaDetalleControlador(){
		lsFactura=ServicioFacturas.recuperarTodos();
	}
	

}
