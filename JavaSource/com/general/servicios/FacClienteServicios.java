package com.general.servicios;

import java.util.ArrayList;
import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.FacTransporte;
import com.general.entidades.FacCliente;
import com.general.entidades.FacGeneral;


@Stateless
public class FacClienteServicios {
	@PersistenceContext(unitName = ConfigPersistenceUnit.persistenceUnit)
	private EntityManager em;

	public void insertCliente(FacCliente cliente){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				e.printStackTrace();
	    		}
		    	em.persist(cliente);
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
	
	public void modificarCliente(FacCliente cliente){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		}
		    Query q = em.createQuery("UPDATE FacCliente SET  razonSocial= :razonS, direccion=:dir, email=:email," +
		    		" tipoCliente= :tCliente,  rise= :rise, telefono= :telf " +
		    		" WHERE id.rucCliente= :rucClientes And id.ruc =:ruc And tipoIdentificacion=:tIdentificacion ");	
		    q.setParameter("ruc", cliente.getId().getRuc().trim());
		    q.setParameter("rucClientes", cliente.getId().getRucCliente().trim());
		    q.setParameter("dir", cliente.getDireccion());
		    q.setParameter("email", cliente.getEmail());
		    q.setParameter("razonS", cliente.getRazonSocial());
		    q.setParameter("rise", cliente.getRise());
		    q.setParameter("telf", cliente.getTelefono());
		    q.setParameter("tCliente", cliente.getTipoCliente());
		    q.setParameter("tIdentificacion", cliente.getTipoIdentificacion());
		    q.executeUpdate();
			//em.merge(cliente);
			    System.out.println("Guarda cliente" );
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
	
	public void insertTransporte(FacTransporte transporte){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		}
		    	em.persist(transporte);
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
	
	public void modificarTransporte(FacTransporte transporte){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				e.printStackTrace();
	    		}
		    em.merge(transporte);
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
	
	public void eliminarVehiculo(String ruc,String rucCliente,String placa){
		try {
			try {
		    		Thread.sleep(100);
	     		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		}
		    	Query q = em.createQuery("DELETE FROM FacTransporte WHERE id.ruc =:rucEm And id.rucCliente =:rucCl And id.placa =:placas");
		    	q.setParameter("rucEm", ruc);
		    	q.setParameter("rucCl", rucCliente);
		    	q.setParameter("placas", placa);

			    System.out.println("Guarda transporte               :"+q.executeUpdate());
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public FacCliente buscaUsuario(String Usuario){
		FacCliente buscar = null;
		
		try {
			List<FacCliente> auxList = new  ArrayList<FacCliente>();
			buscar = new FacCliente();
			Query q  = em.createQuery("Select c From FacCliente c Where c.id.rucCliente = :usuario");
			q.setParameter("usuario", Usuario);
			
			auxList = q .getResultList();
			if (auxList.isEmpty()) {
				return null;
			}else{
				for (int i = 0; i < auxList.size(); i++) {
					buscar.setRazonSocial(auxList.get(i).getRazonSocial());
					buscar.setDireccion(auxList.get(i).getDireccion());
					buscar.setEmail(auxList.get(i).getEmail());
					buscar.setRise(auxList.get(i).getRise());
					buscar.setTelefono(auxList.get(i).getTelefono());
					return buscar;
				}
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buscar;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	public FacCliente buscaUsuarioEmpresa(String Usuario, String rucEmpresa){
		FacCliente buscar = null;
		
		try {
			List<FacCliente> auxList = new  ArrayList<FacCliente>();
			buscar = new FacCliente();
			Query q  = em.createQuery("Select c From FacCliente c Where c.id.rucCliente = :usuario and c.id.ruc = :empresa");
			q.setParameter("usuario", Usuario);
			q.setParameter("empresa", rucEmpresa);
			auxList = q .getResultList();
			if (auxList.isEmpty()) {
				return null;
			}else{
				for (int i = 0; i < auxList.size(); i++) {
					buscar.setRazonSocial(auxList.get(i).getRazonSocial());
					buscar.setDireccion(auxList.get(i).getDireccion());
					buscar.setEmail(auxList.get(i).getEmail());
					buscar.setRise(auxList.get(i).getRise());
					buscar.setTelefono(auxList.get(i).getTelefono());
					buscar.setCodCliente(auxList.get(i).getCodCliente());
					return buscar;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buscar;			
	}
	
	@SuppressWarnings("unchecked")
	public List<FacGeneral> buscarCodigoTabla(String codigo){
		List<FacGeneral> general = null;
		try {
			Query q = em.createQuery("Select g From FacGeneral g Where g.codTabla =:cod and g.isActive = 'Y' Order By g.codTabla");
			q.setParameter("cod", codigo);
			general =  q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return general;
	}
	
	@SuppressWarnings("unchecked")
	public List<FacEmpresa> buscarRuc(){
		List<FacEmpresa> empresa = null;
		try {
			Query q = em.createQuery("Select e From FacEmpresa e where e.isActive = 'Y' Order By e.ruc");
			empresa = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empresa;
	}
	
	public void EliminarClienteLogico(FacCliente cliente){
		try {
			Query q = em.createQuery("update FacCliente set isActive =:Estado where id.rucCliente = :ruc_cliente1 and  id.ruc =:Ruc_empresa");
			q.setParameter("Ruc_empresa", cliente.getId().getRuc());
			q.setParameter("ruc_cliente1", cliente.getId().getRucCliente());
			q.setParameter("Estado", "0");
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FacCliente> buscarDatosCliente(){
		List<FacCliente> cliente = new ArrayList<FacCliente>();
		try {
			System.out.println("Vamos al query");
			Query q = em.createQuery("Select c From FacCliente c where isActive = 'Y' ");
			System.out.println("Vamos a llenar la lista");
			cliente = q.getResultList();
			System.out.println("tamaño de la lista : "+ cliente.size());
		} catch (Exception e) {
			System.out.println("FAC_CLIENTE_SERVICIOS -> Error grave");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return cliente;
	}
	
	@SuppressWarnings("unchecked")
	public List<FacTransporte> buscarVehiculos(String rucEmpresa,String rucCliente){
		List<FacTransporte> transporte = null;
		try {
			Query q = em.createQuery("Select T from FacTransporte T where T.id.ruc = :rucEm And T.id.rucCliente =:rucCli");
			q.setParameter("rucEm", rucEmpresa);
			q.setParameter("rucCli", rucCliente);
			
			transporte = q.getResultList();
		} catch (Exception e) {
			System.out.println("Tu error es "+e);
		}
		return transporte;
	}
	
	@SuppressWarnings("unchecked")
	public boolean busquedaCliente(String rucEmpresa ,String rucUsuario , String tipoIdentificacion){
		boolean correcto=false;
		try {
			
			List<FacCliente> cliente = new ArrayList<FacCliente>();
			Query q = em.createQuery("Select c From FacCliente c " +
									 " where c.id.ruc=:rucEmpresa " +
									 " And c.id.rucCliente=:rucUsuario " +
									 " And c.tipoIdentificacion=:tipoIdentificacion");
			
			q.setParameter("rucEmpresa", rucEmpresa);
			q.setParameter("rucUsuario", rucUsuario);
			q.setParameter("tipoIdentificacion", tipoIdentificacion);
			
			System.out.println("busquedaCliente::rucEmpresa::"+rucEmpresa);
			System.out.println("busquedaCliente::rucUsuario::"+rucUsuario);
			System.out.println("busquedaCliente::tipoIdentificacion::"+tipoIdentificacion);
			
			cliente = q.getResultList();
			if (cliente.isEmpty()) {
				correcto = true;
			}else{
				correcto = false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return correcto;
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
}
