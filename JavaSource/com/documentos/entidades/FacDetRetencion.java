package com.documentos.entidades;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fac_det_retenciones database table.
 * 
 */
@Entity
@Table(name="fac_det_retenciones")
public class FacDetRetencion implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FacDetRetencionPK id;

	@Column(name="\"baseImponible\"")
	private double baseImponible;

	@Column(name="\"codPorcentaje\"")
	private String codPorcentaje;

	@Column(name="\"porcentajeRetencion\"")
	private double porcentajeRetencion;

	private Integer tarifa;
	
	@Column(name="\"codDocSustento\"")
	private String codDocSustento;
	
	@Column(name="\"numDocSustento\"")
	private String numDocSustento;
	
	@Column(name="\"fechaEmisionDocSustento\"")
	private String fechaEmisionDocSustento;

	private double valor;

    public FacDetRetencion() {
    }

	public FacDetRetencionPK getId() {
		return this.id;
	}

	public void setId(FacDetRetencionPK id) {
		this.id = id;
	}
	
	public double getBaseImponible() {
		return this.baseImponible;
	}

	public void setBaseImponible(double baseImponible) {
		this.baseImponible = baseImponible;
	}

	public String getCodPorcentaje() {
		return this.codPorcentaje;
	}

	public void setCodPorcentaje(String codPorcentaje) {
		this.codPorcentaje = codPorcentaje;
	}

	public double getPorcentajeRetencion() {
		return this.porcentajeRetencion;
	}

	public void setPorcentajeRetencion(double porcentajeRetencion) {
		this.porcentajeRetencion = porcentajeRetencion;
	}

	public Integer getTarifa() {
		return this.tarifa;
	}

	public void setTarifa(Integer tarifa) {
		this.tarifa = tarifa;
	}

	public double getValor() {
		return this.valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getCodDocSustento() {
		return codDocSustento;
	}

	public void setCodDocSustento(String codDocSustento) {
		this.codDocSustento = codDocSustento;
	}

	public String getNumDocSustento() {
		return numDocSustento;
	}

	public void setNumDocSustento(String numDocSustento) {
		this.numDocSustento = numDocSustento;
	}

	public String getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}

	public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}

	
}