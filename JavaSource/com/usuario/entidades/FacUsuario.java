package com.usuario.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the fac_usuarios database table.
 * 
 */
@Entity
@Table(name="fac_usuarios")
public class FacUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FacUsuarioPK id;

	@Column(name="\"isActive\"")
	private String isActive;

	@Column(name="\"Nombre\"")
	private String nombre;

	@Column(name="\"Password\"")
	private String password;

	@Column(name="\"RucEmpresa\"")
	private String rucEmpresa;

	@Column(name="\"TipoUsuario\"")
	private String tipoUsuario;
	
	@Column(name="\"loginErroneo\"")
	private int loginErroneo;
	
	@Column(name="\"fechaCreacion\"")
	private Date fechaCreacion;

	@Column(name="\"userCreacion\"")
	private String userCreacion;
	
	@Column(name="\"fechaModificacion\"")
	private Date fechaModificacion;
	
    @Column(name="\"userModificacion\"")
	private String userModificacion;

    public FacUsuario() {
    }

	public FacUsuarioPK getId() {
		return this.id;
	}

	public void setId(FacUsuarioPK id) {
		this.id = id;
	}
	
	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRucEmpresa() {
		return this.rucEmpresa;
	}

	public void setRucEmpresa(String rucEmpresa) {
		this.rucEmpresa = rucEmpresa;
	}

	public String getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public int getLoginErroneo() {
		return loginErroneo;
	}

	public void setLoginErroneo(int loginErroneo) {
		this.loginErroneo = loginErroneo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUserCreacion() {
		return userCreacion;
	}

	public void setUserCreacion(String userCreacion) {
		this.userCreacion = userCreacion;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getUserModificacion() {
		return userModificacion;
	}

	public void setUserModificacion(String userModificacion) {
		this.userModificacion = userModificacion;
	}

	public void setLoginErroneo(Integer loginErroneo) {
		this.loginErroneo = loginErroneo;
	}	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ls_string = "id.Ruc:"+id.getRuc()+"|id.CodUsuario:"+id.getCodUsuario()+
				   "|isActive:"+isActive+
				   "|nombre:"+nombre+"|password:"+password+
				   "|rucEmpresa:"+rucEmpresa+"|tipoUsuario:"+tipoUsuario+
				   "|loginErroneo:"+loginErroneo;
		
		return ls_string;
	}
}