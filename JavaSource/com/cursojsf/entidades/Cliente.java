package com.cursojsf.entidades;

public class Cliente {
	private int codigoC;
	@cursojava.anotaciones.cedula(message ="Error con el numero de cedula")
	private String cedula;
	private String nombre;
	private String apellido;
	private String direccion;
	private Nacionalidad nacionalidad;

	public Cliente() {
		this.nacionalidad = new Nacionalidad();

	}

	public Cliente(int codigoC, String cedula, String nombre, String apellido,
			String direccion, Nacionalidad nacionalidad) {
		super();
		this.codigoC = codigoC;
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.direccion = direccion;
		this.nacionalidad = nacionalidad;
	}

	public Nacionalidad getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(Nacionalidad nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getCodigoC() {
		return codigoC;
	}

	public void setCodigoC(int codigoC) {
		this.codigoC = codigoC;
	}

}
