package com.cursojsf.entidades;

public class Autor {
	private int codigo;
	private String nombre;
	private String apellido;
	private Nacionalidad nacionalidad;
	

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Autor(int codigo , String nombre, String apellido, Nacionalidad nacionalidad) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nacionalidad = nacionalidad;
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}
	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public Autor(){
	}
	
	public Autor(String nombre, String apellido){
		this.nombre= nombre;
		this.apellido= apellido;
	}
	
	
}
