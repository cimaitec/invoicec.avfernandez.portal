package com.usuario.entidades;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the fac_usuarios_establecimiento database table.
 * 
 */
@Embeddable
public class FacUsuariosEstablecimientoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="\"Ruc\"")
	private String ruc;

	@Column(name="\"CodUsuario\"")
	private String codUsuario;

	@Column(name="\"CodEstablecimiento\"")
	private String codEstablecimiento;

    public FacUsuariosEstablecimientoPK() {
    }
	public String getRuc() {
		return this.ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getCodUsuario() {
		return this.codUsuario;
	}
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}
	public String getCodEstablecimiento() {
		return this.codEstablecimiento;
	}
	public void setCodEstablecimiento(String codEstablecimiento) {
		this.codEstablecimiento = codEstablecimiento;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FacUsuariosEstablecimientoPK)) {
			return false;
		}
		FacUsuariosEstablecimientoPK castOther = (FacUsuariosEstablecimientoPK)other;
		return 
			this.ruc.equals(castOther.ruc)
			&& this.codUsuario.equals(castOther.codUsuario)
			&& this.codEstablecimiento.equals(castOther.codEstablecimiento);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ruc.hashCode();
		hash = hash * prime + this.codUsuario.hashCode();
		hash = hash * prime + this.codEstablecimiento.hashCode();
		
		return hash;
    }
}