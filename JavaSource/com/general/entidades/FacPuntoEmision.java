package com.general.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the fac_punto_emision database table.
 * 
 */
@Entity
@Table(name="fac_punto_emision")
public class FacPuntoEmision implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FacPuntoEmisionPK id;

	@Column(name="\"FormaEmision\"")
	private String formaEmision;

	@Column(name="\"isActive\"")
	private String isActive;

	@Column(name="\"TipoAmbiente\"")
	private String tipoAmbiente;
	
	//*@Column(name="\"caja\""/*, insertable=true, updatable=true*/)
	//private String caja;*/
	
	@Column(name="\"Secuencial\""/*, insertable=true, updatable=true*/)
	private int secuencial;

	
	@Column(name="\"fechaCreacion\"")
	private Date fechaCreacion;

	@Column(name="\"userCreacion\"")
	private String userCreacion;
	
	@Column(name="\"fechaModificacion\"")
	private Date fechaModificacion;
	
    @Column(name="\"userModificacion\"")
	private String userModificacion;
    
    public FacPuntoEmision() {
    }
    
	public FacPuntoEmisionPK getId() {
		return this.id;
	}
	public void setId(FacPuntoEmisionPK id) {
		this.id = id;
	}
	
	public String getFormaEmision() {
		return this.formaEmision;
	}
	public void setFormaEmision(String formaEmision) {
		this.formaEmision = formaEmision;
	}

	public String getIsActive() {
		return this.isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getTipoAmbiente() {
		return this.tipoAmbiente;
	}
	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}
	
	/*public String getCaja() {
		return this.caja;
	}
	public void setCaja(String caja) {
		this.caja = caja;
	}*/
	
	public int getSecuencial() {
		return this.secuencial;
	}
	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String ls_string = "id.Ruc:"+id.getRuc()+"|id.CodEstablecimiento:"+id.getCodEstablecimiento()+
						   "|id.CodPuntEmision:"+id.getCodPuntEmision()+
						   "|id.Caja:"+id.getCaja()+"|id.TipoDocumento:"+id.getTipoDocumento()+
						   "|formaEmision:"+formaEmision+"|isActive:"+isActive+
						   "|tipoAmbiente:"+tipoAmbiente+"|secuencial:"+secuencial;
		return ls_string;
	}
}