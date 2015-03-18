package com.recepcion.servicios;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.FacEmpresa;

@Stateless
public class ComprobanteRecepcionServicios {
	@PersistenceContext(unitName = ConfigPersistenceUnit.persistenceUnit)
	private EntityManager em;

	//TODO consulta de la empresa y verifica si esta activa
	public FacEmpresa verificarRuc(String ruc){
		FacEmpresa empresa=null;
		try {
			Query q = em.createQuery("SELECT E FROM FacEmpresa E WHERE E.ruc = :ruc");
			q.setParameter("ruc", ruc);
			empresa = (FacEmpresa) q.getSingleResult();
		} catch (Exception e) {
			empresa = null;
			e.printStackTrace();
		}
		
		return empresa;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
}
