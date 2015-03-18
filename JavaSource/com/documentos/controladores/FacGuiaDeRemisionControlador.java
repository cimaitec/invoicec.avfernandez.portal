package com.documentos.controladores;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


import org.primefaces.component.api.UIData;

import com.documentos.entidades.DetalleImpuestosentidades;
import com.documentos.entidades.FacCabDocumento;
import com.documentos.entidades.FacCabDocumentoPK;
import com.documentos.entidades.FacDetAdicional;
import com.documentos.entidades.FacDetAdicionalPK;
import com.documentos.entidades.FacDetDocumento;
import com.documentos.entidades.FacDetDocumentoPK;
import com.documentos.entidades.FacDetImpuesto;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.FacTransporte;
import com.documentos.servicios.FacGuiaDeRemisionServicios;
import com.general.entidades.FacCliente;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacProducto;
import com.general.entidades.FacPuntoEmision;

@ViewScoped
@ManagedBean
public class FacGuiaDeRemisionControlador {

	// Servicios
	@EJB
	private FacGuiaDeRemisionServicios servicioGuia;
	
	
	// variables FacCabDocumento
	public int TamañoSecuencial = 9;
	protected String Ruc;
	protected String usuario;
	private int TipoIdentificacion;
	private String CodEstablecimiento;
	private String CodPuntEmision;
	private int secuencial;
	private Date fechaEmision;
	private int guiaRemision;
	private String razonSocialComprador="";
	private String identificacionComprador;
	private double totalSinImpuesto;
	private double totalDescuento;
	private String email;
	private double propina;
	private String moneda;
	private String infoAdicional;
	private String periodoFiscal;
	private String rise;
	private Date fechaInicioTransporte;
	private Date fechaFinTransporte;
	private String placa;
	private String fechaEmisionDocSustento;
	private String motivoRazon;
	private String identificacionDestinatario;
	private String razonSocialDestinatario;
	private String direccionDestinatario;
	private String motivoTraslado;
	private String docAduaneroUnico;
	private String codEstablecimientoDest;
	private String ruta;
	private String codDocSustento;
	private String numDocSustento;
	private String numAutDocSustento;
	private Date   fecEmisionDocSustento;
	private String autorizacion;
	private Date fechaautorizacion;
	private String claveAcceso;
	private double importeTotal;
	private String CodigoDocumento = "06";
	private String codDocModificado;
	private String numDocModificado;
	private double motivoValor;
	private String Estado;
	private String tipIdentificacionComprador;
	private String tipoEmision;
	private String dirPartida;
	private String telefonoTransp;
	private String TipoAmbiente;
	
	private List<SelectItem> litipoEmision;

	// variable FacDetAdicional
	private String nombre;
	private String descripcion;
	private int secuencialAdicianal;

	// variable adicionales
	private boolean activo;
	private boolean activoPlacas;
	private boolean grabarDet;
	private boolean grabarAdic;
	private String Espacio = "";
	
	// entidad FacCabDocumento
	private FacCabDocumento cabDoc;
	private FacCabDocumentoPK cabDocPK;

	// entidad FacDetAdicional
	private FacDetAdicional adCabDoc;
	private FacDetAdicionalPK adCabDocPK;
	private List<FacDetAdicional> listaAdiDoc;
	private FacDetAdicional selectAdicionales;
	
	// entidad FacEmpresa
	private FacEmpresa empresa;

	// entidad FacEstablecimiento
	private List<FacEstablecimiento> listEstab;
	private List<FacEstablecimiento> filtarEstab;
	private FacEstablecimiento selectEstab;

	// entidad FacPuntoEmision
	private FacPuntoEmision selectPuntoEm;
	private List<FacPuntoEmision> listPuntoEm;
	private List<FacPuntoEmision> filtarPuntoEm;

	// entidad FacTramsporte
	private List<FacTransporte> transporte;
	private List<FacTransporte> filtarTransp;
	private FacTransporte selectTransporte;

	// entidad FacGeneral
	private List<FacGeneral> listGeneral;

	// entidad FacCliente
	private FacCliente cliente;
	private FacCliente clienteDestinatario;

	// entidad FacProducto
	private FacProducto selectProd;
	private List<FacProducto> listProd;
	private List<FacProducto> filtarProd;
	
	//variable De FacProducto
	private String CodPorducto;
	private String descripcionProd;
	//VPI se cambia tipo de variable
	//private int Cantidad;
	private double Cantidad;
	private double precioUnitario;
	private double descuento;
	private double precioTotalSinImpuesto;
	
	//entidad FacDetDocumento
	private List<FacDetDocumento> listDetDoc;
	private FacDetDocumento detDoc;
	private FacDetDocumento detallesDocumento;
	private FacDetDocumentoPK detallesDocumentoPK;
	private FacDetDocumento selectDetalles;
	
	//varibale de facDetDocumento
	private String codAuxiliar;
	private double valorIce;
	
	// variable FaDetDocumento
	private int secuencialDetalle;
	
	//variable DE DataTable
	private UIData DataTableDetalle;
	private UIData DataTableDetalleAdicional;
	
	// variable de SelectItems
	private List<SelectItem> li_tipoIdenComp;
	
	String patron = "dd/MM/yyyy";
	SimpleDateFormat formato = new SimpleDateFormat(patron);
	
	
	// metodos de aplicaciones

	
	public void cargarDatos() {

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpSession sesion = (HttpSession) context.getExternalContext()
					.getSession(true);
			if (sesion.getAttribute("Ruc_Empresa") != null) {
				Ruc = sesion.getAttribute("Ruc_Empresa").toString();
				usuario = sesion.getAttribute("id_usuario").toString();
			} else {
				ExternalContext ctx = FacesContext.getCurrentInstance()
						.getExternalContext();
				String ctxPath = ((ServletContext) ctx.getContext())
						.getContextPath();
				try {
					ctx.redirect(ctxPath
							+ "/paginas/Administrador/Cuenta/Login.jsf");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setTipoEmision("0");
		CodEstablecimiento = null;
		CodPuntEmision = null;
		setDirPartida(null);
		setTipIdentificacionComprador("0");
		razonSocialComprador = "";
		secuencial = 0;
		setIdentificacionComprador(null);
		fechaInicioTransporte = new Date();
		fechaFinTransporte = new Date();
		placa = null;
		setIdentificacionDestinatario(null);
		razonSocialDestinatario = null;
		direccionDestinatario = null;
		setMotivoTraslado(null);
		setDocAduaneroUnico(null);
		setCodEstablecimientoDest(null);
		setRuta(null);
		setNumAutDocSustento("0");
		setNumDocSustento(null);
		fechaEmision = new Date();
		fechaEmisionDocSustento = new Date().toString();
		TipoAmbiente = "";
		email="";
		setTelefonoTransp("");
		listDetDoc = new ArrayList<FacDetDocumento>();
		listaAdiDoc = new ArrayList<FacDetAdicional>();
		
		this.llenarDatosDelEmisor();
		activo = true;
		activoPlacas = true;
		this.llenarTipoIdentificacionComprador();
		litipoEmision = new ArrayList<SelectItem>();
		LlenarComboGeneral("8",litipoEmision,"fac_general");
	}
	
	//TODO contructor que se encarga de llenar los combo
		private void LlenarComboGeneral(String codigoGeneral, List<SelectItem> listaItems, String lista){
			try{
				if(lista.toString().trim().equals("fac_general")){
					List<FacGeneral> listGeneral = new ArrayList<FacGeneral>();
					FacGeneral generales = new FacGeneral(); 
					listGeneral =  servicioGuia.buscarDatosPorCodigo(codigoGeneral);
					if (listGeneral.isEmpty()){	
						generales.setCodTabla("0");
						generales.setCodUnico("0");
						generales.setDescripcion("NO EXISTEN DATOS");
						generales.setIdGeneral(0);
						generales.setIsActive("Y");
						listGeneral.add(generales);
					}
		    		listaItems.add(new SelectItem(0,"escoja una opcion"));
					for(int i=0;i<listGeneral.size();i++){			
						listaItems.add(new SelectItem(listGeneral.get(i).getCodUnico(),listGeneral.get(i).getDescripcion())); 
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//TODO contructor que se encarga de aunmentar el secuencial del registro
	private String cadenaSecuencial(int secuencial, int Tamaño){
		String cadena = "";
		for (int i = 0; i < Tamaño; i++) 
			if(i > (String.valueOf(secuencial).length() - 1))
				cadena += "0";
		cadena += secuencial;
		return cadena;
	}
	
	//TODO guardando registro a la base de datos
	public void insertEntidades(){
		boolean guardar=false;
		try {
			List<FacEmpresa> listaEmpresa = new ArrayList<FacEmpresa>();
			listaEmpresa.add(empresa);
			List<FacCabDocumento> cabece_documento = new ArrayList<FacCabDocumento>();
			List<FacDetDocumento> detalle_documento = new ArrayList<FacDetDocumento>();
			List<FacDetAdicional> detelle_adicional = new ArrayList<FacDetAdicional>();
			 List<FacDetRetencion> detalle_retencion = new ArrayList<FacDetRetencion>();
			 List<FacDetImpuesto> detalle_impuesto = new ArrayList<FacDetImpuesto>();
			 List<DetalleImpuestosentidades> detalleCabecera_impuesto = new ArrayList<DetalleImpuestosentidades>();
			 List<String> MotivoRazon = new ArrayList<String>();
			 List<FacProducto> DetalleAdicionalproducto = new ArrayList<FacProducto>();
			 
			 
			if(CodEstablecimiento==null||CodPuntEmision==null||tipoEmision=="0"||tipoEmision==null||dirPartida.trim().equals("")||tipIdentificacionComprador=="0"||tipIdentificacionComprador==null||identificacionComprador.trim().equals("")
					||fechaInicioTransporte==null||fechaFinTransporte==null||identificacionDestinatario.trim().equals("")||motivoTraslado.trim().equals("")||docAduaneroUnico.trim().equals("")||codEstablecimientoDest.trim().equals("")
					||ruta.trim().equals("")||numDocSustento.trim().equals("")||fechaEmision==null||numAutDocSustento=="0"||listDetDoc.size()==0){
				if (CodEstablecimiento==null) {
					mensajeAlerta("Mensaje del sistema","Establecimiento:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (CodPuntEmision==null) {
					mensajeAlerta("Mensaje del sistema","Punto Emisión:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (tipoEmision.equals("0")||tipoEmision==null) {
					mensajeAlerta("Mensaje del sistema","Tipo Emisión:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (dirPartida.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Direccion de partida:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (tipIdentificacionComprador.trim().equals("0")||tipIdentificacionComprador==null) {
					mensajeAlerta("Mensaje del sistema","Tipo Identificación:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (identificacionComprador.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Identificacion del Transportita:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (fechaInicioTransporte==null) {
					mensajeAlerta("Mensaje del sistema","Fecha Inicio Transporte:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (fechaFinTransporte==null) {
					mensajeAlerta("Mensaje del sistema","Fecha Fin Transporte:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (identificacionDestinatario.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Identificación del destinatario:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (motivoTraslado.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Motivo de tralado:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (docAduaneroUnico.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Documentos Aduanero:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (codEstablecimientoDest.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Establecimiento Destinatario:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (ruta.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Ruta:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (numDocSustento.trim().equals("")) {
					mensajeAlerta("Mensaje del sistema","Numero:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (fechaEmision==null) {
					mensajeAlerta("Mensaje del sistema","Fecha Emisión:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (autorizacion==null) {
					mensajeAlerta("Mensaje del sistema","Autorización:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				}if (listDetDoc.size()==0) {
					mensajeAlerta("Mensaje del sistema","Detalle de Factura:: Detalles campos vacios", "peligro");
				}
			}else if(tipIdentificacionComprador.trim().equals("04")&&razonSocialComprador.trim().equals("")||tipIdentificacionComprador.trim().equals("05")&&razonSocialComprador.trim().equals("")||tipIdentificacionComprador.trim().equals("06")&&razonSocialComprador.trim().equals("")){
				mensajeAlerta("Mensaje del sistema","Indentificación de Transportita:: Es incorrecta verifique", "peligro");

			}else if(tipIdentificacionComprador.trim().equals("04")&&placa.trim().equals("")||tipIdentificacionComprador.trim().equals("05")&&placa.trim().equals("")||tipIdentificacionComprador.trim().equals("06")&&placa.trim().equals("")){
				mensajeAlerta("Mensaje del sistema","Placa del transportista:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");
			}else if(razonSocialDestinatario.trim().equals("")){
				mensajeAlerta("Mensaje del sistema","Identificacion Destinatario:: Es incorrecta verifique", "peligro");

			}else{
				for (int i = 0; i < listDetDoc.size(); i++) {
					if (listDetDoc.get(i).getCodPrincipal().trim().equals("")) {
						mensajeAlerta("Mensaje del sistema","El Detalle codigo del producto:: Detalle campo vacio", "peligro");

						return;
					}
					if(listDetDoc.get(i).getCantidad()==0){
						mensajeAlerta("Mensaje del sistema","La cantidad del producto:: Detalle campo vacio", "peligro");

						return;
					}
				}
				for (int j = 0; j < listaAdiDoc.size(); j++) {
					if (listaAdiDoc.get(j).getNombre().trim().equals("")) {
						mensajeAlerta("Mensaje del sistema","Nombre de Detalles Adicional:: Detalle campo vacio", "peligro");

						return;
					}
					if (listaAdiDoc.get(j).getValor().equals("")) {
						mensajeAlerta("Mensaje del sistema","Descripcion de Detalles Adicional:: Detalle campo vacio", "peligro");

						return;
					}
				}
				if(String.valueOf(numAutDocSustento).length() > 10 && String.valueOf(numAutDocSustento).length() < 37)
				{
					cabDoc =  new FacCabDocumento();
					cabDocPK = new FacCabDocumentoPK();
					this.llenarSecuencial();
					cabDocPK.setCodEstablecimiento(CodEstablecimiento);
					cabDocPK.setCodPuntEmision(CodPuntEmision);
					cabDocPK.setRuc(Ruc);
					cabDocPK.setSecuencial(cadenaSecuencial(secuencial,TamañoSecuencial));
					cabDocPK.setCodigoDocumento(CodigoDocumento);
					cabDocPK.setAmbiente(new Integer(TipoAmbiente.toString().trim().equals("D") ? 1 : 2));
					cabDoc.setId(cabDocPK);
					
					cabDoc.setAutorizacion(Espacio);////////////
					cabDoc.setClaveAcceso(Espacio);//////////////
					cabDoc.setCodDocModificado(Espacio);//////////7
					cabDoc.setCodDocSustento("01");
					cabDoc.setCodEstablecimientoDest(codEstablecimientoDest);
					cabDoc.setDireccionDestinatario(direccionDestinatario);
					cabDoc.setDocAduaneroUnico(docAduaneroUnico);
					cabDoc.setEmail(email);
					cabDoc.setIsActive("Y");
					cabDoc.setEstadoTransaccion("IN");
					cabDoc.setFecEmisionDocSustento(fecEmisionDocSustento);//////////
					cabDoc.setFechaautorizacion(fechaautorizacion);/////
					cabDoc.setFechaEmision(fechaEmision);
					cabDoc.setFechaEmisionDocSustento(fechaEmisionDocSustento);///////
					cabDoc.setFechaFinTransporte(fechaFinTransporte);
					cabDoc.setFechaInicioTransporte(fechaInicioTransporte);
					cabDoc.setGuiaRemision(Espacio);/////////
					cabDoc.setIdentificacionComprador(identificacionComprador);
					cabDoc.setIdentificacionDestinatario(identificacionDestinatario);
					cabDoc.setImporteTotal(importeTotal);////////////
					infoAdicional = Espacio;
					cabDoc.setInfoAdicional(infoAdicional);/////////7
					cabDoc.setMoneda(moneda);////////////
					motivoRazon = Espacio;
					cabDoc.setMotivoRazon(motivoRazon);/////////////
					cabDoc.setMotivoTraslado(motivoTraslado);
					cabDoc.setNumAutDocSustento(String.valueOf(numAutDocSustento));
					numDocModificado=Espacio;
					cabDoc.setNumDocModificado(numDocModificado);////////////
					cabDoc.setNumDocSustento(numDocSustento);
					cabDoc.setPartida(dirPartida);
					periodoFiscal = Espacio;
					cabDoc.setPeriodoFiscal(periodoFiscal);///////////
					cabDoc.setPlaca(placa);
					cabDoc.setPropina(propina);
					cabDoc.setRazonSocialComprador(razonSocialComprador);
					cabDoc.setRazonSocialDestinatario(razonSocialDestinatario);
					rise = Espacio;
					cabDoc.setRise(rise);
					cabDoc.setRuta(ruta);
					cabDoc.setTipIdentificacionComprador(tipIdentificacionComprador);
					cabDoc.setTipoEmision(tipoEmision);
					cabDoc.setTipoIdentificacion(TipoIdentificacion);
					cabDoc.setTotalDescuento(totalDescuento);
					cabDoc.setTotalSinImpuesto(precioTotalSinImpuesto);
					guardar = servicioGuia.insertarCabDocumentos(cabDoc);
					
					cabece_documento.add(cabDoc); /**guardando registro para archivo**/
	
					if (guardar==false) {
						return;
					}
					guardar = false;
					if (grabarAdic!=false) {
						
						for (int i = 0; i < listaAdiDoc.size(); i++) {
							adCabDoc = new FacDetAdicional();
							adCabDocPK = new FacDetAdicionalPK();
							adCabDocPK.setCodEstablecimiento(cabDoc.getId().getCodEstablecimiento());
							adCabDocPK.setCodPuntEmision(cabDoc.getId().getCodPuntEmision());
							adCabDocPK.setCodigoDocumento(cabDoc.getId().getCodigoDocumento());
							adCabDocPK.setRuc(Ruc);
							adCabDocPK.setSecuencial(cabDoc.getId().getSecuencial());
							adCabDocPK.setSecuencialDetAdicional(secuencialAdicianal+i);
							adCabDoc.setId(adCabDocPK);
							descripcion = listaAdiDoc.get(i).getValor();
							adCabDoc.setValor(listaAdiDoc.get(i).getValor());
							nombre = listaAdiDoc.get(i).getNombre();
							adCabDoc.setNombre(nombre);
							guardar = servicioGuia.insertarAdicionalCabDocumento(adCabDoc);
							detelle_adicional.add(adCabDoc);
							if (guardar==false) {
								break;
							}
						}
					}
					guardar = false;
					if (grabarDet!=false) {
						for (int j = 0; j < listDetDoc.size(); j++) {
							detallesDocumento = new FacDetDocumento();
							detallesDocumentoPK = new FacDetDocumentoPK();
							detallesDocumentoPK.setCodEstablecimiento(cabDoc.getId().getCodEstablecimiento());
							detallesDocumentoPK.setCodPuntEmision(cabDoc.getId().getCodPuntEmision());
							detallesDocumentoPK.setRuc(cabDoc.getId().getRuc());
							detallesDocumentoPK.setSecuencial(cabDoc.getId().getSecuencial());
							detallesDocumentoPK.setCodigoDocumento(cabDoc.getId().getCodigoDocumento());
							secuencialDetalle = j+1;
							detallesDocumentoPK.setSecuencialDetalle(secuencialDetalle);
							detallesDocumento.setId(detallesDocumentoPK);
							Cantidad = listDetDoc.get(j).getCantidad();
							detallesDocumento.setCantidad(Cantidad);
							codAuxiliar = listDetDoc.get(j).getCodAuxiliar();
							detallesDocumento.setCodAuxiliar(codAuxiliar);
							CodPorducto = listDetDoc.get(j).getCodPrincipal();
							detallesDocumento.setCodPrincipal(CodPorducto);
							descripcionProd = listDetDoc.get(j).getDescripcion();
							detallesDocumento.setDescripcion(descripcionProd);
							precioUnitario = listDetDoc.get(j).getPrecioUnitario();
							detallesDocumento.setPrecioUnitario(precioUnitario);
							precioTotalSinImpuesto = calPrecioTotalSinImpuesto(Cantidad, precioUnitario);
							detallesDocumento.setPrecioTotalSinImpuesto(precioTotalSinImpuesto);
							//valorIce = 0.0;				
							detallesDocumento.setValorIce(valorIce);/////
							guardar  = servicioGuia.insertarDetalleDocumento(detallesDocumento);
							detalle_documento.add(detallesDocumento);
							
							FacProducto pro = new FacProducto();
		  					pro = servicioGuia.buscarDatosProductofiltrando(Integer.parseInt(detallesDocumento.getCodPrincipal()));
		  					DetalleAdicionalproducto.add(pro);
		  					
							if (guardar==false) {
								break;
							}
						}
					}
					if (guardar!=false) {
						SpoolGenerarArchivoControlador generarArchivo = new SpoolGenerarArchivoControlador();
	
						generarArchivo.setEmpresa(listaEmpresa);
						generarArchivo.setCabece_documento(cabece_documento);
						generarArchivo.setDetelle_adicional(detelle_adicional);
						generarArchivo.setDetalle_documento(detalle_documento);
						generarArchivo.setDetalle_retencion(detalle_retencion);
						generarArchivo.setDetalle_impuesto(detalle_impuesto);
						generarArchivo.setDetalleCabecera_impuesto(detalleCabecera_impuesto);
						generarArchivo.setMotivoRazon(MotivoRazon);
						generarArchivo.setDetalleAdicionalproducto(DetalleAdicionalproducto);
						
						if(generarArchivo.generarDocumento() == 0){
							mensajeAlerta("Nota de Debito Electronica","Se produjo un error al generar el archivo txt", "alerta");
							return;
						}
						
						mensajeAlerta("Guia de Remision", "Guia de remision : " + CodEstablecimiento + "-" + CodPuntEmision + "-" + cadenaSecuencial(secuencial, TamañoSecuencial), "Información");
						try {
				    		Thread.sleep(2000);
			     		} catch (InterruptedException e) {
						e.printStackTrace();
			    		}
						this.cargarDatos();
						
					} else {
						return;
					}
				} else 
					mensajeAlerta("Mensaje del sistema","Nº Docu. Sustento:: El numero tiene que tener una cantidad de 10 a 37 numeros", "peligro");
			}
		} catch (Exception e) {
			mensajeAlerta("Mensaje del sistema", "Problema del sistema error al guardar", "alerta");
		}
	}
	
	public double calPrecioTotalSinImpuesto(double cantidad, double precioUnitario){
		double total=0.0;
		total = cantidad*precioUnitario;
		return total;
	}

	public void llenarDatosDelEmisor() {
		try {
			empresa = new FacEmpresa();
			/*
			 * aqui debe traer la informacion de acuerdo al usuario, por ahora
			 * se la enviamos quemado
			 */
			empresa = servicioGuia.buscarDatosPorRuc(Ruc);
			// listEmpresa.add(empresa);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void llenarEstablecimiento() {

		try {
			listEstab = servicioGuia.bucarEstsblecimiento(Ruc, usuario);
			if (listEstab.isEmpty()) {
				activo = true;
			} else {
				activo = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void llenarPuntoEmision() {
		try {
			listPuntoEm = servicioGuia.buscarPuntoEmision(Ruc,
					CodEstablecimiento, CodigoDocumento);
			if (listPuntoEm.isEmpty()) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void llenarSecuencial() {
		try {
			secuencial = servicioGuia.secuencial(Ruc, CodEstablecimiento,
					CodPuntEmision,"06");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void llenarTipoIdentificacionComprador() {
		li_tipoIdenComp = new ArrayList<SelectItem>();
		try {
			listGeneral = servicioGuia.buscarDatosPorCodigo("16");

			if (listGeneral.isEmpty()) {
			}
			li_tipoIdenComp.add(new SelectItem("0", "Selecionar  Tipo"));
			for (int i = 0; i < listGeneral.size(); i++) {
				li_tipoIdenComp.add(new SelectItem(listGeneral.get(i)
						.getCodUnico().trim(), listGeneral.get(i).getDescripcion()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void llenarCodigoProducto(){
		try {
			activoPlacas = true;
			activo = true;
			listProd = new ArrayList<FacProducto>();
			listProd = servicioGuia.buscarProductos();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ubicarEstablecimiento() {
		CodEstablecimiento = selectEstab.getId().getCodEstablecimiento();
	}
	

	public void ubicarPuntoEmision() {
		CodPuntEmision = selectPuntoEm.getId().getCodPuntEmision();
		TipoAmbiente = selectPuntoEm.getTipoAmbiente();
	}
	

	public void UbicarProductos(){
		//FacDetDocumento detDocDT = (FacDetDocumento) DataTableDetalle.getRowData();
		FacDetDocumento detDocDT = selectDetalles;
		try {
			
			CodPorducto = String.valueOf(selectProd.getCodPrincipal());
			codAuxiliar = String.valueOf(selectProd.getCodAuxiliar());
			descripcionProd = selectProd.getDescripcion();
			precioUnitario = selectProd.getValorUnitario();
			for (int i = 0; i < listDetDoc.size(); i++) {
				if (listDetDoc.get(i).getCodPrincipal().trim().equals(CodPorducto.trim())) {
					mensajeAlerta("Mensaje del sistema", "Codigo Producto:: Ya existe en la Tabla , Eliga otro", "peligro");
					return;
				}
			}
			detDocDT.setCodPrincipal(CodPorducto);
			detDocDT.setCodAuxiliar(codAuxiliar);
			detDocDT.setDescripcion(descripcionProd);
			detDocDT.setPrecioUnitario(precioUnitario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void buscarRazonSocial() {

		cliente = new FacCliente();
		try {
			if (tipIdentificacionComprador.equals("04") && soloNumero(identificacionComprador.trim(),
					"Identificiacion de transportista") != true
					&& identificacionComprador.length() == 13) {
				
					cliente = servicioGuia.buscarRazonSocial(Ruc, tipIdentificacionComprador.trim(),
							identificacionComprador, "T");
					if (cliente != null) {
						activoPlacas = false;
						this.setRazonSocialComprador(cliente.getRazonSocial());
						email=cliente.getEmail();
						this.setTelefonoTransp(cliente.getTelefono());
						//this.tablaPlacas();
					} else {
						this.setRazonSocialComprador("");
						this.setTelefonoTransp("");
						activoPlacas = true;
						placa = "";
						FacesMessage mensaje = null;
						mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Identificacion de transportista", "No encotrado");
						FacesContext.getCurrentInstance().addMessage(
								"Error al guardar", mensaje);
					}
			}else if (tipIdentificacionComprador.equals("05") && soloNumero(identificacionComprador.trim(),
						"Identificiacion de transportista") != true
						&& identificacionComprador.length() == 10) {
				
					cliente = servicioGuia.buscarRazonSocial(Ruc, tipIdentificacionComprador.trim(),
							identificacionComprador, "T");
					if (cliente != null) {
						activoPlacas = false;
						this.setRazonSocialComprador(cliente.getRazonSocial());
						email=cliente.getEmail();
						this.setTelefonoTransp(cliente.getTelefono());
						this.tablaPlacas();
					} else {
						this.setRazonSocialComprador("");
						this.setTelefonoTransp("");
						activoPlacas = true;
						placa = "";
						FacesMessage mensaje = null;
						mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Identificacion de transportista", "No encotrado");
						FacesContext.getCurrentInstance().addMessage(
								"Error al guardar", mensaje);
					}
				
			} else if(tipIdentificacionComprador.equals("06") && identificacionComprador.length()>9) {
				cliente = servicioGuia.buscarRazonSocial(Ruc, tipIdentificacionComprador.trim(),
						identificacionComprador, "T");
				if (cliente != null) {
					activoPlacas = false;
					this.setRazonSocialComprador(cliente.getRazonSocial());
					email=cliente.getEmail();
					this.setTelefonoTransp(cliente.getTelefono());
					this.tablaPlacas();
				} else {
					this.setRazonSocialComprador("");
					this.setTelefonoTransp("");
					activoPlacas = true;
					placa = "";
					mensajeAlerta("Mensaje del sistema", "Identificación de transportista:: No encotrado", "peligro");
				}
			}else if (tipIdentificacionComprador.equals("07")) {
				this.setIdentificacionComprador("9999999999999");
				this.setRazonSocialComprador("");
				this.setTelefonoTransp("");
				activoPlacas = true;
				placa = "";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void buscarRazonSocialxCombo() {
		try {
			if (tipIdentificacionComprador.equals("04")||tipIdentificacionComprador.equals("05")||tipIdentificacionComprador.equals("06")) {
				this.setIdentificacionComprador("");
				this.setRazonSocialComprador("");
				this.setTelefonoTransp("");
				this.setEmail("");
			}
			if (tipIdentificacionComprador.equals("07")) {
				this.setIdentificacionComprador("9999999999999");
				this.setRazonSocialComprador("");
				this.setTelefonoTransp("");
				this.setEmail("");
				activoPlacas = true;
				placa = "";

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void ubicarPlaca() {
		placa = selectTransporte.getId().getPlaca();
	}
	

	public void tablaPlacas() {
		try {
			transporte = servicioGuia.buscarTransporte(Ruc,
					identificacionComprador);
			if (transporte.isEmpty()) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscarRucDestinatario() {
		try {
			if (identificacionDestinatario.length() >= 10) {
				clienteDestinatario = servicioGuia.buscarDatosDestinatario(Ruc,
						identificacionDestinatario);

				if (clienteDestinatario != null) {
					razonSocialDestinatario = clienteDestinatario
							.getRazonSocial();
					direccionDestinatario = clienteDestinatario.getDireccion();
				} else {
					razonSocialDestinatario = "";
					direccionDestinatario = "";
				}
			} else {
				razonSocialDestinatario = "";
				direccionDestinatario = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean soloNumero(String dato, String nombre) {
		String texto = dato;
		boolean valido = false;
		for (char x : texto.toCharArray()) {
			if (Character.isDigit(x)) {
				valido = false;
			}
			if (Character.isLetter(x)) {
				valido = true;
				FacesMessage mensaje = null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Campo "	+ nombre, "Solo Numero");
				FacesContext.getCurrentInstance().addMessage("Campo " + dato,
						mensaje);
				break;
			}
		}
		return valido;
	}
	
	
	public void verificarDocAduanero(String nombre){
		if(nombre.trim().equals("Aduanero"))
			soloNumero(docAduaneroUnico, "Doc. Aduanero");
		else if(nombre.trim().equals("Autorización"))
			soloNumero(numAutDocSustento, "Nº Autorización");
	}
	
	
	public void ValidarFechaDeFin(){
		fechaFinTransporte = null;
	}

	public void nuevoDetalle(){
		if(CodEstablecimiento==null||CodPuntEmision==null){
			if(CodEstablecimiento==null){
				FacesMessage mensaje = null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Campo Codigo estableimiento", "Selecionar valos");
				FacesContext.getCurrentInstance().addMessage("Campo ",mensaje);
			}
			if(CodPuntEmision==null){
				FacesMessage mensaje = null;
				mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO, "Campo Codigo punto emision", "Requiere valor");
				FacesContext.getCurrentInstance().addMessage("Campo " ,	mensaje);
			}
			grabarDet = false;
		}else{
			detDoc = new FacDetDocumento();
			FacDetDocumentoPK id = new FacDetDocumentoPK();
			id.setRuc(Ruc);
			id.setCodEstablecimiento(CodEstablecimiento);
			id.setCodPuntEmision(CodPuntEmision);
			this.llenarSecuencial();
			id.setSecuencial(cadenaSecuencial(secuencial, TamañoSecuencial));
			secuencialDetalle = listDetDoc.size();
			secuencialDetalle = secuencialDetalle + 1;
			id.setSecuencialDetalle(secuencialDetalle);
			detDoc.setId(id);
			detDoc.setCantidad(0);
			detDoc.setCodAuxiliar("");
			detDoc.setCodPrincipal("");
			detDoc.setDescripcion("");
			detDoc.setDescuento(0);
			detDoc.setPrecioTotalSinImpuesto(0);
			detDoc.setPrecioUnitario(0);
			detDoc.setValorIce(0);
			listDetDoc.add(detDoc);
			grabarDet = true;
		}
	}
	
	public void nuevoDetalleAdicionales(){
		
		if(CodEstablecimiento==null||CodPuntEmision==null){
			if(CodEstablecimiento==null){
				mensajeAlerta("Mensaje del sistema", "Codigo establecimiento:: Selecionar valos", "Información");
			}
			if(CodPuntEmision==null){
				mensajeAlerta("Mensaje del sistema", "Codigo punto emision:: Requiere valor", "Información");
			}
			grabarAdic = false;
		}else{
			adCabDoc = new FacDetAdicional();
			adCabDocPK = new FacDetAdicionalPK();
			adCabDocPK.setRuc(Ruc);
			adCabDocPK.setCodEstablecimiento(CodEstablecimiento);
			adCabDocPK.setCodPuntEmision(CodPuntEmision);
			adCabDocPK.setCodigoDocumento(CodigoDocumento);
			adCabDocPK.setSecuencial(cadenaSecuencial(secuencialAdicianal, TamañoSecuencial));
			adCabDoc.setId(adCabDocPK);
			adCabDoc.setNombre("");
			adCabDoc.setValor("");
			listaAdiDoc.add(adCabDoc);
			grabarAdic=true;
		}
	}
	

	public void borrarlineaDetDoc(){
		FacDetDocumento detDocBorrar = (FacDetDocumento) DataTableDetalle.getRowData();
		listDetDoc.remove(detDocBorrar);

	}
	
	//TODO contructor que se encarga de autocompletar el ruc del comprador para facilitar al usuario
	public List<String> complete_RucComprador(String parametro_ruc, String tipo) throws Exception{
		List<String> resultado = new ArrayList<String>();
		String filtro = (tipo.trim().equals("transportista") ? "'T'" : "'E','C','P'");
		
		if(tipo.trim().equals("transportista"))
			resultado = servicioGuia.BuscarfitroEmpresaDocumentos(parametro_ruc.trim(), empresa.getRuc().trim(), tipIdentificacionComprador.trim(), filtro);	
		else
			resultado = servicioGuia.BuscarfitroEmpresaDocumentos2(parametro_ruc.trim(), empresa.getRuc().trim(), filtro);	
		
		return resultado;
	}
	
	public void BorrarLineaDetAdicional(){
		FacDetAdicional detAdicDocBorrar = (FacDetAdicional) DataTableDetalle.getRowData();
		listaAdiDoc.remove(detAdicDocBorrar);
	}
	
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
		 FacesContext context = FacesContext.getCurrentInstance();            
	      context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
    }
	
	// Metodo Get y Set
	public FacGuiaDeRemisionServicios getServicioGuia() {
		return servicioGuia;
	}

	public void setServicioGuia(FacGuiaDeRemisionServicios servicioGuia) {
		this.servicioGuia = servicioGuia;
	}

	public String getRuc() {
		return Ruc;
	}

	public void setRuc(String ruc) {
		Ruc = ruc;
	}

	public int getTipoIdentificacion() {
		return TipoIdentificacion;
	}

	public void setTipoIdentificacion(int tipoIdentificacion) {
		TipoIdentificacion = tipoIdentificacion;
	}

	public String getCodEstablecimiento() {
		return CodEstablecimiento;
	}

	public void setCodEstablecimiento(String codEstablecimiento) {
		CodEstablecimiento = codEstablecimiento;
	}

	public String getCodPuntEmision() {
		return CodPuntEmision;
	}

	public void setCodPuntEmision(String codPuntEmision) {
		CodPuntEmision = codPuntEmision;
	}

	public int getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}

	public int getGuiaRemision() {
		return guiaRemision;
	}

	public void setGuiaRemision(int guiaRemision) {
		this.guiaRemision = guiaRemision;
	}

	public String getRazonSocialComprador() {
		return razonSocialComprador;
	}

	public void setRazonSocialComprador(String razonSocialComprador) {
		this.razonSocialComprador = razonSocialComprador;
	}

	public String getIdentificacionComprador() {
		return identificacionComprador;
	}

	public void setIdentificacionComprador(String identificacionComprador) {
		this.identificacionComprador = identificacionComprador;
	}

	public double getTotalSinImpuesto() {
		return totalSinImpuesto;
	}

	public void setTotalSinImpuesto(double totalSinImpuesto) {
		this.totalSinImpuesto = totalSinImpuesto;
	}

	public double getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(double totalDescuento) {
		this.totalDescuento = totalDescuento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getPropina() {
		return propina;
	}

	public void setPropina(double propina) {
		this.propina = propina;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(String infoAdicional) {
		this.infoAdicional = infoAdicional;
	}

	public String getPeriodoFiscal() {
		return periodoFiscal;
	}

	public void setPeriodoFiscal(String periodoFiscal) {
		this.periodoFiscal = periodoFiscal;
	}

	public String getRise() {
		return rise;
	}

	public void setRise(String rise) {
		this.rise = rise;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMotivoRazon() {
		return motivoRazon;
	}

	public void setMotivoRazon(String motivoRazon) {
		this.motivoRazon = motivoRazon;
	}

	public String getIdentificacionDestinatario() {
		return identificacionDestinatario;
	}

	public void setIdentificacionDestinatario(String identificacionDestinatario) {
		this.identificacionDestinatario = identificacionDestinatario;
	}

	public String getRazonSocialDestinatario() {
		return razonSocialDestinatario;
	}

	public void setRazonSocialDestinatario(String razonSocialDestinatario) {
		this.razonSocialDestinatario = razonSocialDestinatario;
	}

	public String getDireccionDestinatario() {
		return direccionDestinatario;
	}

	public void setDireccionDestinatario(String direccionDestinatario) {
		this.direccionDestinatario = direccionDestinatario;
	}

	public String getMotivoTraslado() {
		return motivoTraslado;
	}

	public void setMotivoTraslado(String motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}

	public String getDocAduaneroUnico() {
		return docAduaneroUnico;
	}

	public void setDocAduaneroUnico(String docAduaneroUnico) {
		this.docAduaneroUnico = docAduaneroUnico;
	}
	
	public String getCodEstablecimientoDest() {
		return codEstablecimientoDest;
	}

	public void setCodEstablecimientoDest(String codEstablecimientoDest) {
		this.codEstablecimientoDest = codEstablecimientoDest;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
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

	public String getNumAutDocSustento() {
		return numAutDocSustento;
	}

	public void setNumAutDocSustento(String numAutDocSustento) {
		this.numAutDocSustento = numAutDocSustento;
	}

	public String getClaveAcceso() {
		return claveAcceso;
	}

	public void setClaveAcceso(String claveAcceso) {
		this.claveAcceso = claveAcceso;
	}

	public double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getCodigoDocumento() {
		return CodigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		CodigoDocumento = codigoDocumento;
	}

	public String getCodDocModificado() {
		return codDocModificado;
	}

	public void setCodDocModificado(String codDocModificado) {
		this.codDocModificado = codDocModificado;
	}

	public String getNumDocModificado() {
		return numDocModificado;
	}

	public void setNumDocModificado(String numDocModificado) {
		this.numDocModificado = numDocModificado;
	}

	public double getMotivoValor() {
		return motivoValor;
	}

	public void setMotivoValor(double motivoValor) {
		this.motivoValor = motivoValor;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getTipIdentificacionComprador() {
		return tipIdentificacionComprador;
	}

	public void setTipIdentificacionComprador(String tipIdentificacionComprador) {
		this.tipIdentificacionComprador = tipIdentificacionComprador;
	}

	public String getTipoEmision() {
		return tipoEmision;
	}

	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public FacCabDocumento getCabDoc() {
		return cabDoc;
	}

	public void setCabDoc(FacCabDocumento cabDoc) {
		this.cabDoc = cabDoc;
	}

	public FacCabDocumentoPK getCabDocPK() {
		return cabDocPK;
	}

	public void setCabDocPK(FacCabDocumentoPK cabDocPK) {
		this.cabDocPK = cabDocPK;
	}

	public FacDetAdicional getAdCabDoc() {
		return adCabDoc;
	}

	public void setAdCabDoc(FacDetAdicional adCabDoc) {
		this.adCabDoc = adCabDoc;
	}

	public FacDetAdicionalPK getAdCabDocPK() {
		return adCabDocPK;
	}

	public void setAdCabDocPK(FacDetAdicionalPK adCabDocPK) {
		this.adCabDocPK = adCabDocPK;
	}

	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}

	public List<FacEstablecimiento> getListEstab() {
		return listEstab;
	}

	public void setListEstab(List<FacEstablecimiento> listEstab) {
		this.listEstab = listEstab;
	}

	public List<FacPuntoEmision> getListPuntoEm() {
		return listPuntoEm;
	}

	public void setListPuntoEm(List<FacPuntoEmision> listPuntoEm) {
		this.listPuntoEm = listPuntoEm;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	public Date getFechaInicioTransporte() {
		return fechaInicioTransporte;
	}

	public void setFechaInicioTransporte(Date fechaInicioTransporte) {
		this.fechaInicioTransporte = fechaInicioTransporte;
	}

	public Date getFechaFinTransporte() {
		return fechaFinTransporte;
	}

	public void setFechaFinTransporte(Date fechaFinTransporte) {
		this.fechaFinTransporte = fechaFinTransporte;
	}

	public String getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}

	public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}

	public Date getFecEmisionDocSustento() {
		return fecEmisionDocSustento;
	}

	public void setFecEmisionDocSustento(Date fecEmisionDocSustento) {
		this.fecEmisionDocSustento = fecEmisionDocSustento;
	}

	public String getAutorizacion() {
		return autorizacion;
	}

	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	public Date getFechaautorizacion() {
		return fechaautorizacion;
	}

	public void setFechaautorizacion(Date fechaautorizacion) {
		this.fechaautorizacion = fechaautorizacion;
	}

	public List<SelectItem> getLi_tipoIdenComp() {
		return li_tipoIdenComp;
	}

	public void setLi_tipoIdenComp(List<SelectItem> li_tipoIdenComp) {
		this.li_tipoIdenComp = li_tipoIdenComp;
	}

	public List<FacGeneral> getListGeneral() {
		return listGeneral;
	}

	public void setListGeneral(List<FacGeneral> listGeneral) {
		this.listGeneral = listGeneral;
	}

	public List<FacEstablecimiento> getFiltarEstab() {
		return filtarEstab;
	}

	public void setFiltarEstab(List<FacEstablecimiento> filtarEstab) {
		this.filtarEstab = filtarEstab;
	}

	public FacEstablecimiento getSelectEstab() {
		return selectEstab;
	}

	public void setSelectEstab(FacEstablecimiento selectEstab) {
		this.selectEstab = selectEstab;
	}

	public FacPuntoEmision getSelectPuntoEm() {
		return selectPuntoEm;
	}

	public void setSelectPuntoEm(FacPuntoEmision selectPuntoEm) {
		this.selectPuntoEm = selectPuntoEm;
	}

	public List<FacPuntoEmision> getFiltarPuntoEm() {
		return filtarPuntoEm;
	}

	public void setFiltarPuntoEm(List<FacPuntoEmision> filtarPuntoEm) {
		this.filtarPuntoEm = filtarPuntoEm;
	}

	public FacCliente getCliente() {
		return cliente;
	}

	public void setCliente(FacCliente cliente) {
		this.cliente = cliente;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<FacTransporte> getTransporte() {
		return transporte;
	}

	public void setTransporte(List<FacTransporte> transporte) {
		this.transporte = transporte;
	}

	public boolean isActivoPlacas() {
		return activoPlacas;
	}

	public void setActivoPlacas(boolean activoPlacas) {
		this.activoPlacas = activoPlacas;
	}

	public List<FacTransporte> getFiltarTransp() {
		return filtarTransp;
	}

	public void setFiltarTransp(List<FacTransporte> filtarTransp) {
		this.filtarTransp = filtarTransp;
	}

	public FacTransporte getSelectTransporte() {
		return selectTransporte;
	}

	public void setSelectTransporte(FacTransporte selectTransporte) {
		this.selectTransporte = selectTransporte;
	}

	public FacCliente getClienteDestinatario() {
		return clienteDestinatario;
	}

	public void setClienteDestinatario(FacCliente clienteDestinatario) {
		this.clienteDestinatario = clienteDestinatario;
	}

	public FacProducto getSelectProd() {
		return selectProd;
	}

	public void setSelectProd(FacProducto selectProd) {
		this.selectProd = selectProd;
	}

	public List<FacProducto> getListProd() {
		return listProd;
	}

	public void setListProd(List<FacProducto> listProd) {
		this.listProd = listProd;
	}

	public List<FacProducto> getFiltarProd() {
		return filtarProd;
	}

	public void setFiltarProd(List<FacProducto> filtarProd) {
		this.filtarProd = filtarProd;
	}

	public String getCodPorducto() {
		return CodPorducto;
	}

	public void setCodPorducto(String codPorducto) {
		CodPorducto = codPorducto;
	}

	public String getDescripcionProd() {
		return descripcionProd;
	}

	public void setDescripcionProd(String descripcionProd) {
		this.descripcionProd = descripcionProd;
	}

	public double getCantidad() {
		return Cantidad;
	}

	public void setCantidad(double cantidad) {
		Cantidad = cantidad;
	}

	public List<FacDetDocumento> getListDetDoc() {
		return listDetDoc;
	}

	public void setListDetDoc(List<FacDetDocumento> listDetDoc) {
		this.listDetDoc = listDetDoc;
	}

	public UIData getDataTableDetalle() {
		return DataTableDetalle;
	}

	public void setDataTableDetalle(UIData dataTableDetalle) {
		DataTableDetalle = dataTableDetalle;
	}

	public FacDetDocumento getDetDoc() {
		return detDoc;
	}

	public void setDetDoc(FacDetDocumento detDoc) {
		this.detDoc = detDoc;
	}

	public int getSecuencialDetalle() {
		return secuencialDetalle;
	}

	public void setSecuencialDetalle(int secuencialDetalle) {
		this.secuencialDetalle = secuencialDetalle;
	}

	public List<FacDetAdicional> getListaAdiDoc() {
		return listaAdiDoc;
	}

	public void setListaAdiDoc(List<FacDetAdicional> listaAdiDoc) {
		this.listaAdiDoc = listaAdiDoc;
	}

	public FacDetAdicional getSelectAdicionales() {
		return selectAdicionales;
	}

	public void setSelectAdicionales(FacDetAdicional selectAdicionales) {
		this.selectAdicionales = selectAdicionales;
	}

	public UIData getDataTableDetalleAdicional() {
		return DataTableDetalleAdicional;
	}

	public void setDataTableDetalleAdicional(UIData dataTableDetalleAdicional) {
		DataTableDetalleAdicional = dataTableDetalleAdicional;
	}

	public int getSecuencialAdicianal() {
		return secuencialAdicianal;
	}

	public void setSecuencialAdicianal(int secuencialAdicianal) {
		this.secuencialAdicianal = secuencialAdicianal;
	}

	public String getDirPartida() {
		return dirPartida;
	}

	public void setDirPartida(String dirPartida) {
		this.dirPartida = dirPartida;
	}
	
	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getPrecioTotalSinImpuesto() {
		return precioTotalSinImpuesto;
	}

	public void setPrecioTotalSinImpuesto(double precioTotalSinImpuesto) {
		this.precioTotalSinImpuesto = precioTotalSinImpuesto;
	}

	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}

	public SimpleDateFormat getFormato() {
		return formato;
	}

	public void setFormato(SimpleDateFormat formato) {
		this.formato = formato;
	}

	public FacDetDocumento getDetallesDocumento() {
		return detallesDocumento;
	}

	public void setDetallesDocumento(FacDetDocumento detallesDocumento) {
		this.detallesDocumento = detallesDocumento;
	}

	public String getCodAuxiliar() {
		return codAuxiliar;
	}

	public void setCodAuxiliar(String codAuxiliar) {
		this.codAuxiliar = codAuxiliar;
	}

	public double getValorIce() {
		return valorIce;
	}

	public void setValorIce(double valorIce) {
		this.valorIce = valorIce;
	}

	public FacDetDocumentoPK getDetallesDocumentoPK() {
		return detallesDocumentoPK;
	}

	public void setDetallesDocumentoPK(FacDetDocumentoPK detallesDocumentoPK) {
		this.detallesDocumentoPK = detallesDocumentoPK;
	}

	public boolean isGrabarDet() {
		return grabarDet;
	}

	public void setGrabarDet(boolean grabarDet) {
		this.grabarDet = grabarDet;
	}

	public boolean isGrabarAdic() {
		return grabarAdic;
	}

	public void setGrabarAdic(boolean grabarAdic) {
		this.grabarAdic = grabarAdic;
	}

	public String getTelefonoTransp() {
		return telefonoTransp;
	}

	public void setTelefonoTransp(String telefonoTransp) {
		this.telefonoTransp = telefonoTransp;
	}

	public FacDetDocumento getSelectDetalles() {
		return selectDetalles;
	}

	public void setSelectDetalles(FacDetDocumento selectDetalles) {
		this.selectDetalles = selectDetalles;
	}

	public List<SelectItem> getLitipoEmision() {
		return litipoEmision;
	}

	public void setLitipoEmision(List<SelectItem> litipoEmision) {
		this.litipoEmision = litipoEmision;
	}
	
}// fin de la aplicacion