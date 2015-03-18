package com.general.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="fac_bitacora_auditoria")
public class FacBitacoraAuditoria {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_bitacora_auditoria")
	private Integer idBitacoraAuditoria;
	
	private String tabla;
	private String valor;
	private Date fecha;
	private String usuario;
	private String accion;

	public Integer getIdBitacoraAuditoria() {
		return idBitacoraAuditoria;
	}

	public void setIdBitacoraAuditoria(Integer idBitacoraAuditoria) {
		this.idBitacoraAuditoria = idBitacoraAuditoria;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

}
