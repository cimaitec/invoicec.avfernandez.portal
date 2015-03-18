package com.producto.servicios;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacProducto;

@Stateless
public class Fac_ProductosServicios {
	@PersistenceContext (unitName=ConfigPersistenceUnit.persistenceUnit)  
	private EntityManager em;

	//TODO contructor que se encarga de buscar los tipo de impuesto detalle
	@SuppressWarnings("unchecked")
	public List<FacGeneral> buscarDatosPorCodigo(String codigo) throws Exception{
		Query q;
		try{
			q =  em.createQuery("SELECT E FROM FacGeneral E where codTabla= :codigo and isActive = 'Y' ") ;
			q.setParameter("codigo",codigo);
			System.out.println("codigo::"+codigo);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return q.getResultList();		
	}
	
	//TODO contructor que se encarga de consultar el detalle del producto
	@SuppressWarnings("unchecked")
	public List<FacProducto> ConsultarProductoProducto() throws Exception{
		Query q;
		try{
			q =  em.createQuery("SELECT E FROM FacProducto E order by E.codPrincipal");
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return q.getResultList();
	}
	
	//TODO contructor que se encarga de verificar el siguiente numero de codigo del producto
	public int secuencialproducto() throws Exception{
		Query q;
		int valor = 0;
		try{
			q =  em.createQuery("SELECT count(*) FROM FacProducto E");
			valor = Integer.parseInt(String.valueOf(q.getSingleResult())) + 1;
		}catch (Exception e) {
			valor = 0;
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return valor;
	}
	
	//TODO insertar detalle adicional del comprobante de retencion
	public int insertarProducto(FacProducto producto){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				e.printStackTrace();
	    		}
		    	em.persist(producto);
			    return 0;
	 	} catch (Exception e) {
			e.printStackTrace();
			return -1;
	 	}
	}	
	
	//TODO modificar detalle adicional del comprobante de retencion
	public int modificarProducto(FacProducto producto){
		try {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Query q = em.createQuery("update FacProducto  " +
					"set  codAuxiliar = :codAuxiliarMo ," +
					"	  descripcion = :descripcionMo ," +
					"	  valorUnitario = :valorUnitarioMo ," +
					"	  atributo1 = :atributo1Mo," +
					"	  atributo2 = :atributo2Mo," +
					"	  atributo3 = :atributo3Mo," +
					"	  valor1 = :valor1Mo," +
					"	  valor2 = :valor2Mo," +
					"	  valor3 = :valor3Mo, " +
					"	  porcentaje = :porcentaje " +
					"where codPrincipal = :codPrincipalMo");
			
			q.setParameter("codPrincipalMo", producto.getCodPrincipal());
			q.setParameter("codAuxiliarMo", producto.getCodAuxiliar());
			q.setParameter("descripcionMo", producto.getDescripcion());
			q.setParameter("valorUnitarioMo", producto.getValorUnitario());
			q.setParameter("atributo1Mo", producto.getAtributo1());
			q.setParameter("atributo2Mo", producto.getAtributo2());
			q.setParameter("atributo3Mo", producto.getAtributo3());
			q.setParameter("valor1Mo", producto.getValor1());
			q.setParameter("valor2Mo", producto.getValor2());
			q.setParameter("valor3Mo", producto.getValor3());
			q.setParameter("porcentaje", producto.getPorcentaje());
			q.executeUpdate();
			 return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}	
	
	//TODO contructor que consulta el registro que se va a modificar
	@SuppressWarnings("unchecked")
	public List<FacProducto> consultaModificables(int codigoprincipal) throws Exception{
		Query q;
		try {
			q = em.createQuery("SELECT E FROM FacProducto E where E.codPrincipal = :cod_principal");
			q.setParameter("cod_principal", codigoprincipal);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
		return q.getResultList();
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
