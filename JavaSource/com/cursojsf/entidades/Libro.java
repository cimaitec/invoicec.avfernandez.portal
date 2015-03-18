package com.cursojsf.entidades;

public class Libro {
	private int codigo;
	private Categoria categoria;
	private Autor autor;
	private String titulo;
	private int anio;
	private double precio;
	
	public Libro(){
		
	}
	
	
	public Libro(int codigo, Categoria categoria, Autor autor, String titulo,
			int anio, double precio) {
		super();
		this.codigo = codigo;
		this.categoria = categoria;
		this.autor = autor;
		this.titulo = titulo;
		this.anio = anio;
		this.precio = precio;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getAnio() {
		return anio;
	}
	public void setAnio(int anio) {
		this.anio = anio;
	}
	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public Autor getAutor() {
		return autor;
	}
	public void setAutor(Autor autor) {
		this.autor = autor;
	}
	
	
}
