package com.general.servicios;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.general.entidades.FacBitacoraAuditoria;
import com.usuario.entidades.FacLoginBitacora;

@Stateless
public class FacBitacoraAuditoriaServicio {
	@PersistenceContext(unitName = ConfigPersistenceUnit.persistenceUnit)
	private EntityManager em;
	
	public void IngresarBitacoraAuditoria(FacBitacoraAuditoria bitacora){
		try {
			em.persist(bitacora);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getSecuencialBitacoraAuditoria(){
		int secuencial = 1;
		FacBitacoraAuditoria bitacoraAudit = null;
		try {
			Query q = null;			
			q = (Query) em.createQuery("select F from FacBitacoraAuditoria F where F.id = (select max(G.idBitacoraAuditoria) from FacBitacoraAuditoria G)");
			try{
				bitacoraAudit =  (FacBitacoraAuditoria) q.getSingleResult();
			}catch(Exception e){
				bitacoraAudit = null;
			}
			Integer res = new Integer(0);
			if (bitacoraAudit != null)
			{
				System.out.println("getSecuencialBitacoraAuditoriaMax::"+bitacoraAudit.getIdBitacoraAuditoria());
				res = bitacoraAudit.getIdBitacoraAuditoria();
			}
			if (res==null){
				secuencial = 1;
			}
			else{
				secuencial = res.intValue();
			}			
				++secuencial;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secuencial;
	}
}
