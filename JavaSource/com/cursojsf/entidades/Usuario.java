package com.cursojsf.entidades;

public class Usuario {
	private int codigo;
	private String login;
	private String password;

	public Usuario() {

	}

	public Usuario(int codigo, String login, String password) {
		super();
		this.codigo = codigo;
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

}
