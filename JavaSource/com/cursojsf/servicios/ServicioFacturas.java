package com.cursojsf.servicios;

import java.util.ArrayList;
import java.util.List;

import com.cursojsf.entidades.Factura;

public class ServicioFacturas {
	private static List<Factura> facturas;

	private static void cargarFacturas() {
		if (facturas == null) {
			facturas = new ArrayList<Factura>();
		}
	}

	public static void insertar(Factura factura) throws Exception {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarFacturas();
		if (buscarPorNumero(factura.getNumero())==null) {
			facturas.add(factura);
		} else {
			throw new Exception("La CabeceraFactura ya existe");
			
		}
	}

	public static Factura buscarPorNumero(int numero) {
		cargarFacturas();
		for (Factura factura: facturas) {
			if (factura.getNumero()==numero) {
				return factura;
			}
		}
		return null;
	}

	public static List<Factura> recuperarTodos() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cargarFacturas();
		return facturas;
	}
}
