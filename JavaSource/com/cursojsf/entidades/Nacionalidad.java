package com.cursojsf.entidades;

public class Nacionalidad {
	private int id;
	private String pais;
	private String nombre;
	
	public Nacionalidad() {
		super();
	}
	
	public Nacionalidad(int id, String pais, String nombre) {
		super();
		this.id = id;
		this.pais = pais;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
