package com.puntoemision.servicios;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.FacEmpresa;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacPuntoEmision;
import com.general.entidades.FacTiposDocumento;
import com.general.entidades.facDetallePuntoEmision;

@Stateless
public class fac_puntoemisionServicios
{
	@PersistenceContext(unitName = ConfigPersistenceUnit.persistenceUnit)
	private EntityManager em;
	
	//TODO Consulta todas las empresas disponibles
	@SuppressWarnings("unchecked")
	public List<FacEmpresa> BuscarDatosEmpresa()throws Exception {
		List<FacEmpresa> facEmp=null;
		try {
			Query q = em.createQuery("SELECT E FROM FacEmpresa E where E.isActive = :activo ORDER BY E.ruc ");
			q.setParameter("activo", "Y");
			facEmp=q.getResultList();
		} catch (Exception e) {
			facEmp = null;
			e.printStackTrace();
		}
		return facEmp;
	}
	
	//TODO Consulta todas las empresas disponibles
	@SuppressWarnings("unchecked")
	public List<FacTiposDocumento> BuscarDatosTipoDocumentos()throws Exception {
		List<FacTiposDocumento> facEmp=null;
		try {
			Query q = em.createQuery("SELECT E FROM FacTiposDocumento E where E.isActive = :activo ORDER BY E.idDocumento");
			q.setParameter("activo", "Y");
			facEmp=q.getResultList();
		} catch (Exception e) {
			facEmp = null;
			e.printStackTrace();
		}
		return facEmp;
	}
	
	
	//TODO Consulta todas las establecimiento disponibles
	@SuppressWarnings("unchecked")
	public List<FacEstablecimiento> BuscarDatosEstablecimeinto(String rucseleccionado)throws Exception {
		try {
			Query q = em.createQuery("SELECT E FROM FacEstablecimiento E where E.isActive = :activo and E.id.ruc = :RucSeleccionado ");
			q.setParameter("activo", "Y");
			q.setParameter("RucSeleccionado", rucseleccionado.toString().trim());
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO insertar detalle de punto de emision
	public void ingresarDatos(FacPuntoEmision IngresandoPuntoEmision){
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			em.persist(IngresandoPuntoEmision);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO insertar detalle de punto de emision
	public void modificarDatos(FacPuntoEmision IngresandoPuntoEmision){
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			em.merge(IngresandoPuntoEmision);
			/*Query q = em.createQuery("update FacPuntoEmision  " +
									  "set  id.codPuntEmision = :cod_puntoEmision ," +
									  	"	id.tipoDocumento = :tipDocumento ," +
										"	id.caja = :caja ," +
										"	formaEmision = :FEmision ," +
										"	tipoAmbiente = :ambiente," +
										"	isActive = :estado " +
									 "where id.ruc = :ruc " +
									   "and id.codPuntEmision = :cod_puntoEmision " +
									   "and id.tipoDocumento = :tipDocumento ," +
									   "and id.caja = :caja "
										);
										//"where id.secuencial = :codSecuencial");
			
			//q.setParameter("codSecuencial", IngresandoPuntoEmision.getId().getSecuencial());
			q.setParameter("cod_puntoEmision", IngresandoPuntoEmision.getId().getCodPuntEmision());
			q.setParameter("tipDocumento", IngresandoPuntoEmision.getId().getTipoDocumento());
			q.setParameter("FEmision", IngresandoPuntoEmision.getFormaEmision());
			q.setParameter("ambiente", IngresandoPuntoEmision.getTipoAmbiente());
			q.setParameter("estado", IngresandoPuntoEmision.getIsActive());
			
			
			q.setParameter("ruc", IngresandoPuntoEmision.getId().getRuc());
			q.setParameter("caja", IngresandoPuntoEmision.getId().getCaja());
			q.executeUpdate();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO constructor que se encarga de eliminar el registro por completo
	public void eliminardatos(facDetallePuntoEmision seleccionarlistaPuntoEmision){
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
			Query q = em.createQuery("update FacPuntoEmision " +
										"set isActive = 'E' " +
									  "WHERE id.ruc = :ruc " +
									    "and id.codEstablecimiento = :establecimiento " +
									    "and id.codPuntEmision	   = :puntoEmision " +
									    "and id.tipoDocumento   = :tipoDocumento " +
									    "and id.caja			   = :caja ");
									    //"and id.secuencial = :codSecuencial");
			
			q.setParameter("ruc", seleccionarlistaPuntoEmision.getRuc());
			q.setParameter("establecimiento", seleccionarlistaPuntoEmision.getCodEstablecimiento());
			q.setParameter("puntoEmision", seleccionarlistaPuntoEmision.getCodPuntoEmision());
			q.setParameter("tipoDocumento", seleccionarlistaPuntoEmision.getCodTipoDocumento());
			q.setParameter("caja", seleccionarlistaPuntoEmision.getCaja());
			//q.setParameter("codSecuencial", seleccionarlistaPuntoEmision.getSecuencial());
			
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO constructor que me retorna las consula del punto emision filtrandolo con la empresa y establecimiento seleccionado
	@SuppressWarnings("unchecked")
	public List<FacPuntoEmision> consultaDetallePuntoEmision(String Ruc, String establecimiento){
		try {
			Query q = em.createQuery("select p from FacPuntoEmision p WHERE p.id.ruc = :RucEmpresa and p.id.codEstablecimiento = :cod_establecimiento");
			q.setParameter("RucEmpresa", Ruc);
			q.setParameter("cod_establecimiento", establecimiento);
			return q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO contructor que me retorna cuantos registro se encuentra en la tabla de punto emision para su respectivo secuencial en cada insert
	public int SecuencialPuntoEmision(){
		int secuencial = 0;
		try {
			Query q = em.createQuery("select max(id.secuencial) from FacPuntoEmision F");
			Object obj =  q.getSingleResult();
			secuencial = Integer.parseInt(obj.toString()) + 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secuencial;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
