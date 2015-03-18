package com.general.servicios;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.config.ConfigPersistenceUnit;
import com.documentos.entidades.FacCabDocumento;
import com.documentos.entidades.FacCabDocumentoPK;
import com.documentos.entidades.FacDetAdicional;
import com.documentos.entidades.FacDetDocumento;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacEmpresa;
import com.general.entidades.*;

@Stateless
public class FacDocumentoServicios {
	@PersistenceContext (unitName=ConfigPersistenceUnit.persistenceUnit)  
	private EntityManager em;
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	
	@SuppressWarnings("unchecked")
	public  List<FacTiposDocumento> BuscarDatosdeTipoDocumento() throws Exception{
		
		try{
			Query q = em.createQuery("SELECT E FROM FacTiposDocumento E where E.isActive = :Activado");
			q.setParameter("Activado", "Y");
			System.out.println("BuscarDatosdeTipoDocumento::"+q.toString());
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	
	
    public  List<FacDetRetencion> BuscarDatosdeRetenciones(String ruc, 
    														String codEstablecimiento, 
    														String codPuntoEmision,
    														String secuencial,
    														String codigoDocumento,
    														String ambiente) throws Exception{
		
		try{
			Query q = em.createQuery("SELECT E FROM FacDetRetencion E where E.id.ruc = :ruc " +
					" and E.id.codEstablecimiento = :establecimiento " +
					" and E.id.codPuntEmision = :puntoemision " +
					" and E.id.secuencial = :secuencial " +
					" and E.id.codigoDocumento = :codigoDocumento " +
					" and E.id.ambiente = :ambiente ");
			q.setParameter("ruc", ruc);
			q.setParameter("establecimiento", codEstablecimiento);
			q.setParameter("puntoemision", codPuntoEmision);
			q.setParameter("secuencial", secuencial);
			q.setParameter("codigoDocumento", codigoDocumento);
			q.setParameter("ambiente",Integer.valueOf(ambiente));
			System.out.println("BuscarDatosdeRetenciones::"+q.toString());
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	@SuppressWarnings("unchecked")
	public List<FacCabDocumento> MostrandoDetalleDocumento(Object [] obj) throws Exception{
		try{
			
			boolean habilitarFiltroFecha = true;
			String ls_filtro_sql = "";
			obj[0]= (obj[0].equals("")?null:obj[0]);
			obj[1]= (obj[1].equals("")?null:obj[1]);
			
			if (obj[0]!=null){
				ls_filtro_sql = ls_filtro_sql + " and E.identificacionComprador = :rucComprador ";
				habilitarFiltroFecha = false;
			}			
			
			if (obj[1]!=null){
				ls_filtro_sql = ls_filtro_sql + " and UPPER(E.razonSocialComprador) like :razonSocialComp ";
				habilitarFiltroFecha = false;
			}
			
			if (obj[2]!=null){
				ls_filtro_sql = ls_filtro_sql + " and E.id.codigoDocumento = :codDocumentoSelecionado ";
			}

				
				if (obj[6]!=null){
					ls_filtro_sql = ls_filtro_sql + " and E.estadoTransaccion = :estado ";
				}
				
				if (obj[7]!=null){
					if(obj[7].toString().length()<8){
						ls_filtro_sql = ls_filtro_sql + " and concat(E.id.codEstablecimiento,'-',E.id.codPuntEmision,'-',E.id.secuencial)   like :secuencial "
										+ " or concat(E.id.codEstablecimiento,E.id.codPuntEmision,E.id.secuencial)  like  :secuencial ";
					habilitarFiltroFecha = false;
					}else{
						ls_filtro_sql = ls_filtro_sql + " and concat(E.id.codEstablecimiento,'-',E.id.codPuntEmision,'-',E.id.secuencial)   = :secuencial "
													  + " or concat(E.id.codEstablecimiento,E.id.codPuntEmision,E.id.secuencial)   = :secuencial ";
					}
				}
			
				
				//VPI se cambia forma en la que se envia el rango de fecha
				if (habilitarFiltroFecha){
						if (obj[3]!=null){
							ls_filtro_sql = ls_filtro_sql + " and E.fechaEmision >= :fechaInicio ";
						}
						
						if (obj[4]!=null){
								ls_filtro_sql = ls_filtro_sql + " and E.fechaEmision <= :fechaFinal ";
						}
				}
				
			
			String ls_query = 	/*"SELECT id.ruc, id.codEstablecimiento, id.codPuntEmision, id.secuencial, id.codigoDocumento, id.ambiente, identificacionComprador, razonSocialComprador, "+
					   			" importeTotal, fechaEmision, email "+ */
					   			"SELECT E FROM FacCabDocumento E "+
								" where E.id.ruc = :ruc_empresa	" +										
								ls_filtro_sql +
								//" and E.estadoTransaccion in('AT','TD') " +
								" order by E.fechaEmision desc, E.id.secuencial desc ";
			
			System.out.println("ls_query::"+ls_query);
			Query query = em.createQuery(ls_query);
			
			System.out.println("rucComprador::"+ obj[0]);
			System.out.println("razonSocialComp::"+ obj[1]);
			System.out.println("codDocumentoSelecionado::"+ obj[2]);
			System.out.println("fechaInicio::"+ obj[3]);
			System.out.println("fechaFinal::"+ obj[4]);
			System.out.println("ruc_empresa::"+ obj[5]);
			//System.out.println("dialect::"+ obj[6]);
			System.out.println("estado::"+ obj[6]);
			System.out.println("numDocumento::"+ obj[7]);
			
			if (obj[5].toString()!=null){
				query.setParameter("ruc_empresa", obj[5].toString());// parametro que es el ruc de la empresa logoneada
			}
			//(E.identificacionComprador = :rucComprador or :rucComprador = :DatoVacio) 
			
			if (obj[0]!=null){
				query.setParameter("rucComprador", obj[0].toString());// parametro de identificador del comprador
			}			
			
			if (obj[1]!=null){
				query.setParameter("razonSocialComp","%"+obj[1].toString().toUpperCase()+"%"); // parametro de la razon social
			}
			if (obj[2]!=null){
				query.setParameter("codDocumentoSelecionado",obj[2].toString()); // parametro del documento seleccionado
			}

		   if(habilitarFiltroFecha){
				// fecha de inicio
				if (obj[3] != null) {
					Calendar calini = Calendar.getInstance();
					calini.setTime(Date.valueOf(obj[3].toString()));
					calini.add(Calendar.DATE, 1);
					
					query.setParameter("fechaInicio", calini.getTime()); // fecha de inicio
				}
				// fecha de final
				if (obj[4] != null) {
					Calendar calfin = Calendar.getInstance();
					calfin.setTime(Date.valueOf(obj[4].toString()));
					calfin.add(Calendar.DATE, 1);
					query.setParameter("fechaFinal", calfin.getTime()); 
				}
			
		   }
		   
			if (obj[6]!=null){
				query.setParameter("estado",obj[6].toString());
			}
			
			if (obj[7] != null) {
				if (obj[7].toString().length() < 8) {	
						query.setParameter("secuencial",obj[7].toString());
				}else{
						query.setParameter("secuencial",obj[7].toString()+"%");

				}
			}
				
			
			
			List<FacCabDocumento> rs = null; 
			rs = query.getResultList();
			return rs;
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FacEmpresa> listadoEmpresas(String Ruc_empresa) throws Exception{
		try{
			String stringQuery = "select E from FacEmpresa E where E.ruc = :ruc_empresa and E.isActive = :Estado";
			Query query = em.createQuery(stringQuery);
			query.setParameter("ruc_empresa", Ruc_empresa);
			query.setParameter("Estado", "Y");
			return query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	public FacEmpresa listadoEmpr(String Ruc_empresa) throws Exception{
		try{
			String stringQuery = "select E from FacEmpresa E where E.ruc = :ruc_empresa and E.isActive = :Estado";
			Query query = em.createQuery(stringQuery);
			query.setParameter("ruc_empresa", Ruc_empresa);
			query.setParameter("Estado", "Y");
			FacEmpresa e = (FacEmpresa) query.getSingleResult(); 
			return e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FacEmpresa> listadoTodasEmpresas() throws Exception{
		try{
			String stringQuery = "select E from FacEmpresa E where E.isActive = :Estado";
			Query query = em.createQuery(stringQuery);
			query.setParameter("Estado", "Y");
			return query.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	public List<facDetalleDocumentoEntidad> MotrandoDocumentoFiltrados(
																		List<FacCabDocumento> documento_detalles,
																		List<FacTiposDocumento> tipoDocumentos,List<FacEmpresa> detalleEmpresas) throws Exception{
		try{
			
			
			List<facDetalleDocumentoEntidad> documentoEntidad = new ArrayList<facDetalleDocumentoEntidad>();
			facDetalleDocumentoEntidad detalleEntidad = new facDetalleDocumentoEntidad();
			
			if(documento_detalles.isEmpty()){
				return documentoEntidad;
			}
			
			//for (FacCabDocumento detalle : documento_detalles) {
			for (int i = 0; i < documento_detalles.size(); i++) {
					for (FacTiposDocumento documento : tipoDocumentos) {
						if (documento_detalles.get(i).getId().getCodigoDocumento().equalsIgnoreCase(documento.getIdDocumento())) {// validando si el codigo del documento existe
							for (FacEmpresa empresa : detalleEmpresas) {
										if (documento_detalles.get(i).getId().getRuc().equalsIgnoreCase(empresa.getRuc())) {// validando si el codigo del documento existe
											String formato = Integer.parseInt(documento_detalles.get(i).getId().getCodigoDocumento()) + documento_detalles.get(i).getId().getRuc()+documento_detalles.get(i).getId().getCodigoDocumento()+ documento_detalles.get(i).getId().getCodEstablecimiento() + documento_detalles.get(i).getId().getCodPuntEmision() + documento_detalles.get(i).getId().getSecuencial();
											String Estado = documento_detalles.get(i).getEstadoTransaccion().trim();
											detalleEntidad.setRFCREC(documento_detalles.get(i).getIdentificacionComprador());
											detalleEntidad.setNOMREC(documento_detalles.get(i).getRazonSocialComprador());
											detalleEntidad.setCodDoc(documento.getIdDocumento());
											detalleEntidad.setTIPODOC(documento.getDescripcion());
											detalleEntidad.setFOLFAC(documento_detalles.get(i).getId().getCodEstablecimiento() + "-" + documento_detalles.get(i).getId().getCodPuntEmision() + "-" + documento_detalles.get(i).getId().getSecuencial());
											
											
											if (documento_detalles.get(i).getId().getCodigoDocumento().equals("01"))
												//VPI
												detalleEntidad.setTOTAL(String.valueOf(documento_detalles.get(i).getImporteTotal()));
											if (documento_detalles.get(i).getId().getCodigoDocumento().equals("07"))
												//VPI
												detalleEntidad.setTOTAL(String.valueOf(documento_detalles.get(i).getImporteTotal()));
											if (documento_detalles.get(i).getId().getCodigoDocumento().equals("04")){
												//VPI se cambia la forma en que se muestra el total
												//ya que varia por decimales y no muestra lo de la base 
												double total =documento_detalles.get(i).getImporteTotal();
												/*
												double total = detalle.getSubtotal12()+
															   detalle.getSubtotalNoIva()+
															   detalle.getSubtotal0()+
															   detalle.getIva12()+
															   detalle.getTotalvalorICE();
															   */
												//VPI
												detalleEntidad.setTOTAL(String.valueOf(total));
											}
											detalleEntidad.setFECHA(Date.valueOf(String.valueOf(documento_detalles.get(i).getFechaEmision())));
											
											detalleEntidad.setEDOFAC((Estado.trim().equals("IN") ? "INICIAL" : 
																	  Estado.trim().equals("GE") ? "GENERADO" : 
																	  Estado.trim().equals("FI") ? "FIRMADO" : 
																	  Estado.trim().equals("RE") ? "RECIBIDA" : 
																	  Estado.trim().equals("DE") ? "DEVUELTA" : 
																	  Estado.trim().equals("AT") ? "AUTORIZADO" : 
																	  Estado.trim().equals("NA") ? "NO AUTORIZADO" : 
																      Estado.trim().equals("AN") ? "NO AUTORIZADO" : 
																	  Estado.trim().equals("CT") ? "CONTINGENCIA" :
																      Estado.trim().equals("RS") ? "RECIBIDA SRI" :
																      Estado.trim().equals("RS") ? "SIN RESPUESTA" :
																	  Estado.trim().equals("TD") ? "CONTINGENCIA RECEPCION" : ""));
											detalleEntidad.setPDFARC(formato + ".pdf");
											detalleEntidad.setXMLARC(formato + ".xml");
											detalleEntidad.setEmail(documento_detalles.get(i).getEmail());
											detalleEntidad.setDireccion(empresa.getPathCompFirmados());
											detalleEntidad.setFormato(formato);
											detalleEntidad.setXmlAutorizacion(documento_detalles.get(i).getDocuAutorizacion());
											
											detalleEntidad.setCodEstablecimiento(documento_detalles.get(i).getId().getCodEstablecimiento());
											detalleEntidad.setCodPuntoEmision(documento_detalles.get(i).getId().getCodPuntEmision());
											detalleEntidad.setSecuencial(documento_detalles.get(i).getId().getSecuencial());
											detalleEntidad.setAmbiente((documento_detalles.get(i).getId().getAmbiente().intValue()==1?"Pruebas":"Produccion"));
											
											//vpi se agrega validacion para traer ruc del documento
											detalleEntidad.setRucEmisor(documento_detalles.get(i).getId().getRuc());
											
											documentoEntidad.add(detalleEntidad);
											detalleEntidad =  new facDetalleDocumentoEntidad();
											break;
										}
									}
							break;
						}
					}
			}
			
			return documentoEntidad;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
	@SuppressWarnings("unchecked")
	public  List<String> BuscarfitroEmpresaDocumentos(String parametro_ruc, String ruc_emppresa, String ambiente) throws Exception{
		
		try{
			Query q = em.createQuery("SELECT distinct E.identificacionComprador FROM FacCabDocumento E where E.identificacionComprador like :ruc_comprador " +
					"		and E.id.ruc = :ruc_emppresa and E.id.ambiente = :ambiente ");
			q.setParameter("ruc_comprador", "%" + parametro_ruc);
			q.setParameter("ruc_emppresa", ruc_emppresa);
			q.setParameter("ambiente", (ambiente.equals("Pruebas")?1:2));
			q.setFirstResult(0);
			q.setMaxResults(10);
			return q.getResultList();
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar registro");
		}
	}
	
public FacCabDocumento buscarCabDocumentoPorPk(String ruc, String codEstablecimiento, String codPuntoEmision, String secuencial, String codigoDocumento, String ambiente) throws Exception{
		
		try{
			
			Query q =  em.createQuery("SELECT E FROM FacCabDocumento E where E.id.ruc = :ruc and E.id.codEstablecimiento = :codEstablecimiento " +
					"and E.id.codPuntEmision = :codPuntoEmision and E.id.secuencial = :secuencial and E.id.codigoDocumento = :codigoDocumento " +
					"and E.id.ambiente = :ambiente") ;
			q.setParameter("ruc", ruc);
			q.setParameter("codEstablecimiento", codEstablecimiento);
			q.setParameter("codPuntoEmision", codPuntoEmision);
			q.setParameter("secuencial", secuencial);
			q.setParameter("codigoDocumento", codigoDocumento);			
			q.setParameter("ambiente",Integer.valueOf(ambiente));
			FacCabDocumento e = (FacCabDocumento)q.getSingleResult(); 	
			System.out.println(" ===>> 5 : Resulset "+q.getSingleResult());
			return e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}
	
	public List<FacDetDocumento> buscarDetDocumentoPorFk(String ruc, String codEstablecimiento, String codPuntoEmision, String secuencial, String codigoDocumento, String ambiente) throws Exception{
		
		try{;
			List<FacDetDocumento> lstFactDetDocumento = new ArrayList<FacDetDocumento>();
			Query q =  em.createQuery("SELECT E FROM FacDetDocumento E where E.id.ruc = :param1 and E.id.codEstablecimiento = :param2 and E.id.codPuntEmision = :param3 and E.id.secuencial = :param4 and E.id.codigoDocumento = :param5 and E.id.ambiente = :param6 ") ;
			q.setParameter("param1", ruc);
			q.setParameter("param2", codEstablecimiento);
			q.setParameter("param3", codPuntoEmision);
			q.setParameter("param4", secuencial);
			q.setParameter("param5", codigoDocumento);					
			q.setParameter("param6", Integer.valueOf(ambiente));
			lstFactDetDocumento = q.getResultList();
			return lstFactDetDocumento;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}

	public List<FacDetAdicional> buscarDetDocumentoAdicPorFk(String ruc, String codEstablecimiento, String codPuntoEmision, String secuencial, String codigoDocumento, String ambiente) throws Exception{
		
		try{
			List<FacDetAdicional> lstFactDetDocumento = new ArrayList<FacDetAdicional>();
			Query q =  em.createQuery("SELECT E FROM FacDetAdicional E where E.id.ruc = :ruc and E.id.codEstablecimiento = :codEstablecimiento and E.id.codPuntEmision = :codPuntoEmision and E.id.secuencial = :secuencial and E.id.codigoDocumento = :codigoDocumento and E.id.ambiente = :ambiente") ;
			q.setParameter("ruc", ruc);
			q.setParameter("codEstablecimiento", codEstablecimiento);
			q.setParameter("codPuntoEmision", codPuntoEmision);
			q.setParameter("secuencial", secuencial);
			q.setParameter("codigoDocumento", codigoDocumento);
			q.setParameter("ambiente",  Integer.valueOf(ambiente));
			lstFactDetDocumento = q.getResultList();
			return lstFactDetDocumento;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}
	
	public FacProducto buscarProductoPorId(String codPrincipal) throws Exception{
		
		try{
			
			Query q =  em.createQuery("SELECT E FROM FacProducto E where E.codPrincipal = 4");//:codPrincipal") ;
			//q.setParameter("codPrincipal", Integer.getInteger(codPrincipal));		
			FacProducto e = (FacProducto)q.getSingleResult(); 					
			return e;
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Buscar el registro");
		}
	}
	
	public FacEstablecimiento buscarCodEstablecimiento(String ruc, String codEstablecimiento){
		FacEstablecimiento busqueda =null;
		try {
			Query q = em.createQuery("select ES from FacEstablecimiento ES where ES.id.ruc= :Ruc AND ES.id.codEstablecimiento = :codEstablecimiento");
			q.setParameter("Ruc", ruc);
			q.setParameter("codEstablecimiento", codEstablecimiento);
			busqueda = (FacEstablecimiento) q.getSingleResult();
			
		} catch (Exception e) {
			System.out.println("tu error es ...: "+e);
			return null;
		}
		return busqueda;
	}
	
	public void actualizarEstadoDocumento(com.general.lazy.wrappers.FacCabDocumento documento,String estado){
		try {
			try {
		    		Thread.sleep(1000);
	     		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	    		}
		    Query q = em.createQuery("UPDATE FacCabDocumento SET  estadoTransaccion= :estado " +
		    						 " WHERE id= :idDocuemnto ");
		    q.setParameter("estado", estado);
		    q.setParameter("idDocuemnto", documento.getId());

		    q.executeUpdate();
			    System.out.println("Se actualiza documento" );
			    
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  	
		}
	}
}
