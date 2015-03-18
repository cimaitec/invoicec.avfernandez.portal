package com.documentos.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import com.documentos.entidades.FacDetImpuesto;
import com.documentos.entidades.FacDetMotivosdebito;
import com.documentos.entidades.FacDetMotivosdebitoPK;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.PantallaDetalleDocumentoDebito;
import com.documentos.servicios.ComprobanteNotaDebitoServicios;
import com.general.entidades.FacCliente;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacEstablecimientoPK;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacProducto;
import com.general.entidades.FacPuntoEmision;
import com.general.entidades.FacPuntoEmisionPK;
import com.general.entidades.FacTiposDocumento;

@SessionScoped
@ManagedBean
public class ComprobanteNotaDebitoControlador {
	@EJB
	private ComprobanteNotaDebitoServicios NotaDebitoServicio;
	protected String codigoTipoDocumento = "'05'";
	protected String codigoTipoDocumento2 = "05";
	private FacEmpresa empresa;
	protected String ruc; //cima
	protected String usuario;
	private String tipoDocumento;
	protected String TipoAmbiente;
	private int secuencial;
	public String Espacios = "";
	public int tamañosecuencial = 9;
	
	// variable de tipo de emision
	private String tipoEmision;
	private List<SelectItem> litipoEmision;
	private Date FechaDeEmision;
	
	//variable de tipo de impuesto
	private List<SelectItem> listTipImpuesto;

	// variable de los establecimientos
	private String codEstablecimiento;
	private List<FacEstablecimiento> listEstablecimientos;
	private List<FacEstablecimiento> filtraEstablecimiento; // variable que se encarga de filtrar la los establecimiento
	private FacEstablecimiento verCamposEstablecimiento;// variable que se encarga de recoger el registro que el usuario selecciono el establecimiento
	private FacEstablecimiento establecimiento;
	private FacEstablecimientoPK establecimientoPK;
	
	// variable de punto de emision
	private String puntoEmision;
	private List<FacPuntoEmision> listPuntosEmision;
	private FacPuntoEmision puntoEmisionObj;
	private FacPuntoEmisionPK puntoEmisionPK;
	private List<FacPuntoEmision> filtraPuntosEmision; // variable que se encarga de filtrar la los roles
	private FacPuntoEmision verCamposPE;// variable que se encarga de recoger el registro que el usuario a seleccionado el punto de emision
	private boolean visibleBotonePuntoEmision; 
	
	// variable del comprador
	private String tipoComprador;
	private List<SelectItem> tiposComprador;
	private String identificacionDelComprador;
	private boolean banderaComprador;
	private String razonSocial;
	private String direccionEstablecimiento;
	private String correoElectronico;
	private String telefono;
	private String tcRuc= "04"; 
	private String tcCed= "05";
	private String tcPas= "06";
	private String tcCof= "07";
	private FacCliente comprador;
	private boolean presenta;
	
	// variable de detalle adicional
	private List<FacDetAdicional>   listPantallaDetalleAdicional;
	private FacDetAdicional detDocumentoUI;
	
	// variable de documento que se modifica
	private String TipoDocumentoModifi;
	private List<SelectItem> listaTipoDocumentoModif;
	protected String codDocModificable = "'01'";
	private Date FecDeEmisionModif;
	private String noComprobanteModif;
	
	// variable del detalle del documento del detalle del producto con sus variables totales
	private PantallaDetalleDocumentoDebito pantallaDetalleDocumento;
	private List<PantallaDetalleDocumentoDebito> listPantallaDetalleDocumento;
	private UIData DataTableDetalle;
	private PantallaDetalleDocumentoDebito seleccionaDetalleDocumento;
	public String Evento2;
	private double subtotalSinImpuesto;
	private double subtotal12;
	private double subtotal0;
	private double subtotalNoIva;
	private double totalvalorICE;
	private double iva12;
	private double valorTotalFactura;
	
	//variable de la tabla de cod porcentaje
	private List<FacGeneral> TablaPorcentaje;
	private List<FacGeneral> filtraTablaPorcentaje;
	private FacGeneral verCamposPorcentaje;
	
	//TODO contructor que se encarga de limpiar todos los controles
	private void limpiarcontroles(){
		subtotal0 = 0;
		subtotal12 = 0;
		subtotalNoIva = 0;
		subtotalSinImpuesto = 0;
		totalvalorICE = 0;
		iva12 = 0;
		valorTotalFactura = 0;
		secuencial = 0;
		visibleBotonePuntoEmision = true;
		tipoEmision = "0";
		FechaDeEmision = new Date();
		codEstablecimiento = null;
		puntoEmision = null;
		tipoComprador = "";
		identificacionDelComprador = null;
		razonSocial = null;
		direccionEstablecimiento = null;
		correoElectronico = null;
		telefono = null;
		tiposComprador = new ArrayList<SelectItem>();
		listPuntosEmision = new ArrayList<FacPuntoEmision>();
		filtraEstablecimiento = new ArrayList<FacEstablecimiento>();
		listEstablecimientos = new ArrayList<FacEstablecimiento>();
		litipoEmision = new ArrayList<SelectItem>();
		listaTipoDocumentoModif = new ArrayList<SelectItem>();
		listTipImpuesto = new ArrayList<SelectItem>();
		listPantallaDetalleDocumento = new ArrayList<PantallaDetalleDocumentoDebito>();
		listPantallaDetalleAdicional = new ArrayList<FacDetAdicional>();
		TipoAmbiente = "";
		FecDeEmisionModif = new Date();
		noComprobanteModif = null;
		Evento2 = "";
	}
	
	//TODO contructor que se encarga de cargar los datos 
	public void CargarDatos(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession sesion = (HttpSession)context.getExternalContext().getSession(true);
		if(sesion.getAttribute("Ruc_Empresa") != null)
		{
			ruc = sesion.getAttribute("Ruc_Empresa").toString();
			usuario = sesion.getAttribute("id_usuario").toString();
		}
		else{
			ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
			String ctxPath = ((ServletContext) ctx.getContext()).getContextPath();
			try {
				ctx.redirect(ctxPath + "/paginas/Administrador/Cuenta/Login.jsf");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		limpiarcontroles();
		
		/// llamo a obtener la informacion Tributaria
		cargaInformacionTributaria();
		
		//tipos Emision
        LlenarComboGeneral("8",litipoEmision,"fac_general");
        tipoEmision = "0";
        
        // tipo de identificacion del cliente
        LlenarComboGeneral("16",tiposComprador,"fac_general");
        tipoComprador = "0";
        
        // Tipo de documento modificables
        LlenarComboGeneral(codDocModificable, listaTipoDocumentoModif, "fac_tipoDocumento");
        TipoDocumentoModifi = "0";
        
        // Tipo de documento modificables
        LlenarComboGeneral("21", listTipImpuesto, "fac_general");
        TipoDocumentoModifi = "0";
		nombreDocumento();
	}
	
	//TODO contructo que va a llenar el establecimiento para la busqueda
	public void llenaEstablecimientos(){
		try{
			listEstablecimientos = NotaDebitoServicio.buscarDatosEstablecimiento(empresa.getRuc(), usuario);		
			if (listEstablecimientos.isEmpty()){			
                establecimientoPK.setCodEstablecimiento("");
                establecimientoPK.setRuc("");
                establecimiento.setId(establecimientoPK);
				establecimiento.setCorreo("");
				establecimiento.setDireccionEstablecimiento("");
				establecimiento.setIsActive("0");
				establecimiento.setMensaje(" ");
				establecimiento.setPathAnexo("");
				listEstablecimientos.add(establecimiento);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	//TODO contructor que se encarga de validar que todo los campos esten ingresados
	private boolean validarCampos(){
		boolean valida = false;
		if (codEstablecimiento == null || puntoEmision == null || tipoComprador.equals("0") || identificacionDelComprador == null || identificacionDelComprador == "" || banderaComprador== false || FechaDeEmision == null 
				|| TipoDocumentoModifi.equals("0") || FecDeEmisionModif == null || noComprobanteModif == null){
			if(codEstablecimiento.equals("null") || codEstablecimiento == ""){
				mensajeAlerta("Mensaje del sistema","Establecimiento:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");		
				valida = true;
			}
			
			if(puntoEmision.equals("null") || puntoEmision == "" || puntoEmision.equals("0")){
				mensajeAlerta("Mensaje del sistema","El Campo Punto de Emision Se Encuentra Vacio, Porfavor ingreselo", "peligro");				
				valida = true;
			}
			
			if(tipoComprador.equals("0")){
		  		 mensajeAlerta("Mensaje del sistema","Tipo Comprador::  Se Encuentra Vacio, Porfavor ingreselo", "peligro");
		  		 valida = true;
			}
			
			if(identificacionDelComprador.equals("null") || identificacionDelComprador == "" || razonSocial == null || razonSocial.trim().equals("")){
				mensajeAlerta("Mensaje del sistema","El Campo: Identificacion del Comprador  Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				valida = true;
			}
	
			if(banderaComprador == false){
				mensajeAlerta("Mensaje del sistema","El Campo Identificacion Comprador Es incorrecto ", "peligro");
				valida = true;    
			}
	
			if(FechaDeEmision== null){
				mensajeAlerta("Mensaje del sistema","El Campo Fecha Emision Esta Vacio ", "peligro");
				valida = true;
			}
			
			if(TipoDocumentoModifi.equals("0")){
				mensajeAlerta("Mensaje del sistema","El Campo: Tipo documento que modifica no se a seleccionado, Porfavor seleccione", "peligro");
				valida = true;
			}
			
			if(FecDeEmisionModif == null){
				mensajeAlerta("Mensaje del sistema","El Campo: La fecha de Emision del documento que modifica Esta Vacio, Porfavor ingreselo", "peligro");
				valida = true;
			}
			
			if(noComprobanteModif == null || noComprobanteModif == ""){
				mensajeAlerta("Mensaje del sistema","El Campo: Nº Comprobante del documento que modifica Esta Vacio, Porfavor ingreselo", "peligro");
				valida = true;
			}
		}
		return valida;
	}
	
	//TODO contructor que se encarga de autocompletar el ruc del comprador para facilitar al usuario
	public List<String> complete_RucComprador(String parametro_ruc) throws Exception{
		List<String> resultado = new ArrayList<String>();
		resultado = NotaDebitoServicio.BuscarfitroEmpresaDocumentos(parametro_ruc.trim(), empresa.getRuc().trim(), tipoComprador.trim());	
		return resultado;
	}
	
	//TODO contructor que valida que el detalle del registro no se repita
  	private boolean ValidarRegistroDetalle(List<PantallaDetalleDocumentoDebito> puntoEmision,List<String> Contenidos){
  		Boolean validacion = false;
  		int contador = 0;
  		for (PantallaDetalleDocumentoDebito contenido : puntoEmision) 
  			if(Contenidos.get(0).toString().trim().equals(contenido.getCod_impuesto().toString().trim()) == true &&
  					Contenidos.get(0).toString().trim().equals(contenido.getCod_porcentaje().toString().trim()) == true){
  				contador ++;
  				if(contador == 2)
  					return validacion = true;
  			}
  		
  		return validacion;
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
  	
	//TODO contructor que se encarga de guardar el detalle del comprobante de retencion
  	public void insertarNotaDebito(String NombreBoton){

  		if(validarCampos())
  			return;
  		if(listPantallaDetalleDocumento.size()  == 0){
  			mensajeAlerta("Mensaje del sistema", "No hay registro de Detalle del documento","alerta");
  			return;
  		}
  		for (PantallaDetalleDocumentoDebito documento : listPantallaDetalleDocumento){
			if(ValidarRegistroDetalle(listPantallaDetalleDocumento, Arrays.asList(
						documento.getCod_impuesto(),
						documento.getCod_porcentaje()))){
					mensajeAlerta("Mensaje del sistema", "el registro ingresado ya se encuenta registrado", "peligro");  
					mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "Informacion");
					return;
			}
			if(documento.getBaseImponible() == 0){
				mensajeAlerta("Mensaje del sistema", "Por favor la linea :" + documento.getLinea() + " del detalle del documento de valor de modificacion que es igual a 0 ingrese un valor mayor a 0", "peligro");
				return;
			}
  		}
  		
  		int guardado = 0;
  		try{
  			List<FacEmpresa> listaEmpresa = new ArrayList<FacEmpresa>();
			listaEmpresa.add(empresa);
			List<FacCabDocumento> cabece_documento = new ArrayList<FacCabDocumento>();
			List<FacDetAdicional> detelle_adicional = new ArrayList<FacDetAdicional>();
			List<String> MotivoRazon = new ArrayList<String>();
			List<DetalleImpuestosentidades> detalleCabecera_impuesto = new ArrayList<DetalleImpuestosentidades>();
			List<FacDetDocumento> detalle_documento = new ArrayList<FacDetDocumento>();
			List<FacDetRetencion> detalle_retencion = new ArrayList<FacDetRetencion>();
			List<FacDetImpuesto> detalle_impuesto = new ArrayList<FacDetImpuesto>();
			List<FacProducto> DetalleAdicionalproducto = new ArrayList<FacProducto>();
			
			/** 
  			 * ingresando cabecera del documento 
  			 * **/
  			FacCabDocumento cabDocumento = new FacCabDocumento();
  			FacCabDocumentoPK id = new FacCabDocumentoPK();
  			id.setRuc(empresa.getRuc());
  			id.setCodEstablecimiento(codEstablecimiento);
  			id.setCodPuntEmision(puntoEmision);
  			id.setAmbiente(new Integer(TipoAmbiente.toString().trim().equals("D") ? 1 : 2));
  			secuencial = NotaDebitoServicio.secuencialCabecera(empresa.getRuc(), codEstablecimiento, puntoEmision, codigoTipoDocumento2);
  			id.setSecuencial(cadenaSecuencial(secuencial,tamañosecuencial));
  			id.setCodigoDocumento(codigoTipoDocumento2.trim());
  			cabDocumento.setId(id);
  			
  			
  			cabDocumento.setFechaEmision(FechaDeEmision);
			cabDocumento.setGuiaRemision(Espacios);
			cabDocumento.setRazonSocialComprador(razonSocial);
			cabDocumento.setIdentificacionComprador(identificacionDelComprador);
			cabDocumento.setTotalSinImpuesto(getSubtotalSinImpuesto());
			cabDocumento.setTotalDescuento(0);
			cabDocumento.setEmail(correoElectronico);
			cabDocumento.setPropina(0);
			cabDocumento.setInfoAdicional(Espacios);
			cabDocumento.setPeriodoFiscal(Espacios);
			cabDocumento.setMoneda("DOLAR");
			cabDocumento.setRise(Espacios);
			cabDocumento.setFechaInicioTransporte(null);
			cabDocumento.setFechaFinTransporte(null);
			cabDocumento.setPlaca(Espacios);
			cabDocumento.setFecEmisionDocSustento(FecDeEmisionModif);
			cabDocumento.setMotivoRazon(Espacios);
			cabDocumento.setIdentificacionDestinatario(Espacios);
			cabDocumento.setRazonSocialDestinatario(Espacios);
			cabDocumento.setDireccionDestinatario(Espacios);
			cabDocumento.setMotivoTraslado(Espacios);
			cabDocumento.setDocAduaneroUnico(Espacios);
			cabDocumento.setCodEstablecimientoDest(Espacios);
			cabDocumento.setRuta(Espacios);
			cabDocumento.setCodDocSustento(Espacios);
			cabDocumento.setNumDocSustento(Espacios);
			cabDocumento.setNumAutDocSustento(Espacios);
			cabDocumento.setAutorizacion(Espacios);
			cabDocumento.setFechaautorizacion(null);
			cabDocumento.setClaveAcceso(Espacios);
			cabDocumento.setImporteTotal(this.getValorTotalFactura());
			cabDocumento.setCodDocModificado(TipoDocumentoModifi);
			cabDocumento.setNumDocModificado(noComprobanteModif);
			cabDocumento.setMotivoValor(0);
			cabDocumento.setTipIdentificacionComprador(tipoComprador);
			cabDocumento.setTipoEmision(tipoEmision);
			cabDocumento.setPartida(Espacios);
			cabDocumento.setSubtotal0(subtotal0);
			cabDocumento.setSubtotal12(subtotal12);
			cabDocumento.setSubtotalNoIva(subtotalNoIva);
			cabDocumento.setTotalvalorICE(totalvalorICE);
			cabDocumento.setIva12(iva12);
  			cabDocumento.setAutorizacion(Espacios);
  			cabDocumento.setIsActive("Y");
  			cabDocumento.setEstadoTransaccion("IN");
  			guardado = NotaDebitoServicio.insertarCabeceraDocumento(cabDocumento);///SE EJECUTA EL METODO
 	        cabece_documento.add(cabDocumento);

			if (guardado == 0){
				for (PantallaDetalleDocumentoDebito documentoDetalle : listPantallaDetalleDocumento) {
					
					FacDetMotivosdebitoPK det_motivoPk = new FacDetMotivosdebitoPK();
					FacDetMotivosdebito det_motivo = new FacDetMotivosdebito();
					det_motivoPk.setRuc(cabDocumento.getId().getRuc());
					det_motivoPk.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
					det_motivoPk.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
					det_motivoPk.setSecuencial(cabDocumento.getId().getSecuencial());
					det_motivoPk.setSecuencialDetalle(documentoDetalle.getLinea());
					det_motivoPk.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
					det_motivo.setId(det_motivoPk);
					det_motivo.setCodImpuesto(Integer.parseInt(documentoDetalle.getCod_impuesto().trim()));
					det_motivo.setCodPorcentaje(Integer.parseInt(documentoDetalle.getCod_porcentaje().trim()));
					det_motivo.setBaseImponible(documentoDetalle.getBaseImponible());
					det_motivo.setRazon(documentoDetalle.getRazon());
					det_motivo.setTarifa(documentoDetalle.getTarifa());
					det_motivo.setValor(documentoDetalle.getValor());
					det_motivo.setTipoImpuestos((documentoDetalle.getCod_impuesto().trim().equals("2")) ? "IVA" : "ICE");
					guardado = NotaDebitoServicio.insertarDetalleMotivoDebito(det_motivo);///SE EJECUTA EL METODO
					if(guardado != 0)
						break;
					MotivoRazon.add(documentoDetalle.getRazon() + "," + documentoDetalle.getValor());
				}
			}
			
			/** 
  			 * ingresando detalle del impuesto de cabecera
  			 * **/
			if(guardado != 0)
				detalleCabecera_impuesto = NotaDebitoServicio.detalleFiltroimpuesto(cabDocumento.getId().getSecuencial(), cabDocumento.getId().getCodigoDocumento());
			
			/** 
  			 * ingresando detalle del documento adicional
  			 * **/
			
			if (guardado == 0){
  				int secuDa = 1;
  				for (FacDetAdicional documentoAdicional : listPantallaDetalleAdicional) {
  					FacDetAdicional detAdicional = new FacDetAdicional();
  					FacDetAdicionalPK detAdicionalPK = new FacDetAdicionalPK();
  					detAdicionalPK.setRuc(cabDocumento.getId().getRuc());
  	  				detAdicionalPK.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
  	  				detAdicionalPK.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
  	  				detAdicionalPK.setSecuencial(cabDocumento.getId().getSecuencial());
  	  				detAdicionalPK.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
  					detAdicionalPK.setSecuencialDetAdicional(secuDa);
  					detAdicional.setId(detAdicionalPK);
  					detAdicional.setNombre(documentoAdicional.getNombre());
  					guardado = NotaDebitoServicio.insertarDetalleAdicional(detAdicional);///SE EJECUTA EL METODO
  					secuDa++;
  					detelle_adicional.add(detAdicional);
  					if(guardado != 0)
  						break;
  				}
			}
			
			if(guardado != 0){
				mensajeAlerta("Nota de Debito Electronica","Se produjo un error al guardar la nota de Debito","alerta");
				return;
			}
			
			SpoolGenerarArchivoControlador generarArchivo = new SpoolGenerarArchivoControlador();
			
			generarArchivo.setEmpresa(listaEmpresa);
			generarArchivo.setCabece_documento(cabece_documento);
			generarArchivo.setDetelle_adicional(detelle_adicional);
			generarArchivo.setDetalleCabecera_impuesto(detalleCabecera_impuesto);
			generarArchivo.setMotivoRazon(MotivoRazon);
			generarArchivo.setDetalle_documento(detalle_documento);
			generarArchivo.setDetalle_retencion(detalle_retencion);
			generarArchivo.setDetalle_impuesto(detalle_impuesto);
			generarArchivo.setDetalleAdicionalproducto(DetalleAdicionalproducto);
			
			if(generarArchivo.generarDocumento() == 0){
				mensajeAlerta("Nota de Debito Electronica","Se produjo un error al generar el archivo txt","alerta");
				return;
			}
			
			mensajeAlerta("Nota de Debito Electronica","Nota de Debito Generada \n" + codEstablecimiento + "-" + puntoEmision + "-" + cadenaSecuencial(secuencial,tamañosecuencial), "Informacion");
			
			try {
	    		Thread.sleep(2000);
     		} catch (InterruptedException e) {
			e.printStackTrace();
    		}
			CargarDatos();
  		}
  		catch (Exception e) {
  			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
  	}
	
  //TODO calculando el valor total del todos los impuesto e sin impuesto
  	public void valorDocumento(){
  		subtotalSinImpuesto = 0; totalvalorICE = 0; subtotal0 = 0; subtotal12 = 0; subtotalNoIva = 0; valorTotalFactura = 0; iva12 = 0;
  		for (PantallaDetalleDocumentoDebito documento : listPantallaDetalleDocumento) {
  			subtotalSinImpuesto += documento.getBaseImponible();
  			totalvalorICE += ((documento.getCod_impuesto().trim().equals("3")) ? documento.getValor_tarifa() : 0);
  			if(documento.getCod_porcentaje().trim().equals("0") && documento.getCod_impuesto().trim().equals("2"))
				subtotal0 += documento.getBaseImponible();
			else if(documento.getCod_porcentaje().trim().equals("2") && documento.getCod_impuesto().trim().equals("2")){
				subtotal12 += documento.getBaseImponible();
				iva12 += documento.getValor_tarifa();
			}
			else if(documento.getCod_porcentaje().trim().equals("6") && documento.getCod_impuesto().trim().equals("2"))
				subtotalNoIva += documento.getBaseImponible();
		}
  		subtotalSinImpuesto = Redondear(subtotalSinImpuesto, 2);
  		totalvalorICE = Redondear(totalvalorICE, 2);
  		subtotal0 = Redondear(subtotal0, 2);
  		subtotal12 = Redondear(subtotal12, 2);
  		iva12 = Redondear(iva12, 2);
  		subtotalNoIva = Redondear(subtotalNoIva, 2);  		
  		
  		valorTotalFactura = Redondear(subtotalSinImpuesto + totalvalorICE + iva12, 2);
  	}
  	
	 //TODO 
    /**
     * contructor que se encarga de calcular y recalcular los valores del producto seleccionado, 
     * el filtrado de borrar campo lo que hace es que si se selecciona otro tipo de producto se limpia los controles de la fila seleccionada
  	**/
	public void calculaTotal_producto(String evento){
  		try{	
  			PantallaDetalleDocumentoDebito detDocumen;
  			if(evento.toString().trim().equals("llenartablaproducto")){
  				detDocumen = (PantallaDetalleDocumentoDebito) DataTableDetalle.getRowData();;
	  			TablaPorcentaje = new ArrayList<FacGeneral>();
	  			filtraTablaPorcentaje = new ArrayList<FacGeneral>();
	  			TablaPorcentaje = NotaDebitoServicio.buscarDatosPorCodigo((detDocumen.getCod_impuesto().trim().equals("2")) ? "24" : "28");

  			}
  			else if(evento.toString().trim().equals("CambioCodImpuesto")){
  				detDocumen = (PantallaDetalleDocumentoDebito) DataTableDetalle.getRowData();
  				detDocumen.setCod_porcentaje("");
  				detDocumen.setTarifa(0);
  				detDocumen.setValor(detDocumen.getBaseImponible());
  				detDocumen.setValor_tarifa(0);
  				valorDocumento();
  			}
  			else if(evento.toString().trim().equals("PorcentajeSeleccionado")){
	  			Evento2 = "ProductoSeleccionado";
	  			seleccionaDetalleDocumento.setCod_porcentaje(verCamposPorcentaje.getCodUnico());
	  			seleccionaDetalleDocumento.setTarifa(verCamposPorcentaje.getPorcentaje());
	  			calculaTotal_producto("RealizandoCalculo");
  			}
  			else if(evento.toString().trim().equals("RealizandoCalculo")){
  				 if(Evento2.equals("ProductoSeleccionado"))
  					 detDocumen = seleccionaDetalleDocumento;
  				 else
  					 detDocumen = (PantallaDetalleDocumentoDebito) DataTableDetalle.getRowData();
  				
  				double baseImponible = 0, Valor_tarifa = 0, valor = 0, porcTarifa = 0;
  				baseImponible = detDocumen.getBaseImponible();
  				porcTarifa = detDocumen.getTarifa();
  				Valor_tarifa = (Redondear(baseImponible * (porcTarifa / 100), 2));
  				valor = baseImponible + Valor_tarifa;
  				detDocumen.setValor_tarifa(Valor_tarifa);
  				detDocumen.setValor(valor);
  				valorDocumento();
  				Evento2 = "";
  			}
  		}catch (Exception e) {
  			FacesMessage mensaje=null;
  			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
  			FacesContext.getCurrentInstance().addMessage(null, mensaje);
  			e.printStackTrace();
  		}
  	}
	
  	//TODO contructor que nos sirve para redondear numeros decimales
  	private double Redondear(double numero,int digitos)
  	{
  	   int cifras=(int) Math.pow(10,digitos);
  	   return Math.rint(numero*cifras)/cifras;
  	}
	
	//TODO construcor que se encarga de crear el nuevo detalle del documento de impuesto
	public void nuevoDetalle(String evento){
		if (validarCampos())
			return;
		if(evento.toString().trim().equals("Documento"))
		{
			pantallaDetalleDocumento = new PantallaDetalleDocumentoDebito();
			pantallaDetalleDocumento.setRuc(ruc);
			pantallaDetalleDocumento.setCodEstablecimiento(codEstablecimiento);
			pantallaDetalleDocumento.setPuntoEmision(puntoEmision);
			pantallaDetalleDocumento.setSecuencial(secuencial);
			pantallaDetalleDocumento.setLinea(listPantallaDetalleDocumento.size() + 1);
			pantallaDetalleDocumento.setRazon("");
			pantallaDetalleDocumento.setCod_impuesto("0");
			pantallaDetalleDocumento.setCod_porcentaje("");
			pantallaDetalleDocumento.setBaseImponible(0.00);
			pantallaDetalleDocumento.setTarifa(0);
			pantallaDetalleDocumento.setValor_tarifa(0.00);
			pantallaDetalleDocumento.setValor(0.00);
			listPantallaDetalleDocumento.add(pantallaDetalleDocumento);
		}
		else if(evento.toString().trim().equals("Adicional"))
		{
			FacDetAdicionalPK facDetAdicionalPk = new FacDetAdicionalPK();
			FacDetAdicional facDetAdicional = new FacDetAdicional();
				 //Asigno pk
			facDetAdicionalPk.setRuc(ruc);
			facDetAdicionalPk.setCodEstablecimiento(codEstablecimiento);
			facDetAdicionalPk.setCodPuntEmision(puntoEmision);
			facDetAdicionalPk.setSecuencial(cadenaSecuencial(secuencial,tamañosecuencial));
			facDetAdicionalPk.setCodigoDocumento(codigoTipoDocumento2);
		  	//Genero Secuencial de Detalle
		   	facDetAdicionalPk.setSecuencialDetAdicional(listPantallaDetalleAdicional.size() + 1);
				 

			facDetAdicional.setId(facDetAdicionalPk);		 	
			facDetAdicional.setNombre(null);
			facDetAdicional.setValor(null);
				 
			listPantallaDetalleAdicional.add(facDetAdicional);
		}
	}
	
	//TODO contructor que se encarga de eliminar el detalle adicional
	public void BorrarLineaDetAdicional(String DetalleDocumento) {
		if(DetalleDocumento.equals("Detalleadicional"))
			listPantallaDetalleAdicional.remove(detDocumentoUI);
		else{
			listPantallaDetalleDocumento.remove(seleccionaDetalleDocumento);
			int contador = 0;
			for (PantallaDetalleDocumentoDebito detalle : listPantallaDetalleDocumento) {
				contador ++;
				detalle.setLinea(contador);
			}
		}
		valorDocumento();
   	}
	
	//TODO contructor que recoge el dato seleccionado del la busqueda de establecimiento
	public void ubicarValorEstablecimiento() {  		 
		 codEstablecimiento = verCamposEstablecimiento.getId().getCodEstablecimiento().trim();
		 if(codEstablecimiento != ""){
			 visibleBotonePuntoEmision = false;
		 }
    }
	
	//TODO contructor que se encarga de filtrar el tipo de documento
	private void nombreDocumento(){
		try{
			List<FacTiposDocumento> documento = new ArrayList<FacTiposDocumento>();
			documento = NotaDebitoServicio.buscartipoDocumento(codigoTipoDocumento);
			if(!documento.isEmpty())
				for (FacTiposDocumento facTiposDocumento : documento) {
					tipoDocumento = facTiposDocumento.getDescripcion();
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que se encarga de llenar los combo
	private void LlenarComboGeneral(String codigoGeneral, List<SelectItem> listaItems, String lista){
		try{
			if(lista.toString().trim().equals("fac_general")){
				List<FacGeneral> listGeneral = new ArrayList<FacGeneral>();
				FacGeneral generales = new FacGeneral(); 
				listGeneral = NotaDebitoServicio.buscarDatosPorCodigo(codigoGeneral);
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
			else if(lista.toString().trim().equals("fac_tipoDocumento")){
				List<FacTiposDocumento> listGeneral = new ArrayList<FacTiposDocumento>();
				FacTiposDocumento generales = new FacTiposDocumento(); 
				listGeneral = NotaDebitoServicio.buscartipoDocumento(codigoGeneral);;
				if (listGeneral.isEmpty()){		
					generales.setIdDocumento("0");
					generales.setDescripcion("NO EXISTEN DATOS");
					listGeneral.add(generales);
				}
				for(int i=0;i<listGeneral.size();i++){			
					listaItems.add(new SelectItem(listGeneral.get(i).getIdDocumento(),listGeneral.get(i).getDescripcion())); 
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que buscar el comproador por su tipo de identificacion
	public void buscarComprador(){
        banderaComprador = false;
        int longitudIdentificacion;
		try{
			if (tipoComprador.equals("0") || tipoComprador.equals(" ") || tipoComprador.equals("") || tipoComprador == null){
				this.setIdentificacionDelComprador("");
				this.setRazonSocial("");
				this.setDireccionEstablecimiento("");
				this.setCorreoElectronico("");
				this.setTelefono("");
			}else{
				if (identificacionDelComprador != null) {				
					/* "04        ";"VENTA CON RUC"
					"05        ";"VENTA CON CEDULA"
					"06        ";"VENTA CON PASAPORTE"
					"07        ";"VENTA CON CONSUMIDOR FINAL" */
					longitudIdentificacion = identificacionDelComprador.length();
					
					if (tipoComprador.trim().equals(tcRuc))
	                      if(longitudIdentificacion == 13)
							banderaComprador = true;
			
					if (tipoComprador.trim().equals(tcCed))						
	                    if(longitudIdentificacion == 10)
	                    	banderaComprador = true;
				     
					if (tipoComprador.trim().equals(tcPas))
						if(longitudIdentificacion == 10)
							banderaComprador = true;
				     
					if (tipoComprador.trim().equals(tcCof))
						if(longitudIdentificacion == 10)
							banderaComprador = true;
					
					if (banderaComprador){
						comprador = NotaDebitoServicio.buscarComprador(identificacionDelComprador);
						if (comprador != null){
							this.setRazonSocial(comprador.getRazonSocial());
							this.setDireccionEstablecimiento(comprador.getDireccion());
							this.setCorreoElectronico(comprador.getEmail());
							this.setTelefono(comprador.getTelefono());
							presenta = true;
						}else{
							mensajeAlerta("Mensaje del sistema","Comprador:: El Comprador No Existe", "informacion");
						}
					} 
					else 
						mensajeAlerta("Mensaje del sistema","Comprador:: Documento Invalido", "informacion");
				}else{
					this.setRazonSocial("");
					this.setDireccionEstablecimiento("");
					this.setCorreoElectronico("");
					this.setTelefono("");
					mensajeAlerta("Mensaje del sistema","Ingrese Identificacion Comprador", "peligro");				
				}				
			}
		}catch (Exception e) {
			String mensaje = e.getMessage();
			this.setRazonSocial("");
			this.setDireccionEstablecimiento("");
			this.setCorreoElectronico("");
			this.setTelefono("");
			banderaComprador = false;
			if (mensaje == null){
				mensaje = " ";
			}else
			mensajeAlerta("Mensaje del sistema","Error en la Identificacion del Comprador " + mensaje, "alerta");
		}
	}
	
	//TODO contuctor que llena los punto de emision dependiendo del establecimiento seleccionado
	public void llenaPuntosEmision(){
		try{
			if (codEstablecimiento == null){
				mensajeAlerta("Mensaje del sistema","Ingrese El Establecimiento antes del Punto Emision", "Informacion");
				return;
			}
				listPuntosEmision = NotaDebitoServicio.buscarDatosPuntoEmision(empresa.getRuc(),codEstablecimiento,codigoTipoDocumento2.trim());	
				if (listPuntosEmision.isEmpty()){		
	                puntoEmisionPK.setCodEstablecimiento("");
	                puntoEmisionPK.setRuc("");
	                //Verificar Cambio Hugo
	                //puntoEmisionPK.setSecuencial(null);
	                //Cambio Hugo 1 Inicio
	                puntoEmisionPK.setCaja("");
	                puntoEmisionPK.setCodPuntEmision("");
	                puntoEmisionPK.setTipoDocumento("");
	                //Cambio Hugo 1 Fin
	                puntoEmisionObj.setId(puntoEmisionPK);
	                puntoEmisionObj.setFormaEmision("");
	                puntoEmisionObj.setIsActive("0");
	                puntoEmisionObj.setTipoAmbiente("");	                
	                listPuntosEmision.add(puntoEmisionObj);				
			}
		}catch (Exception e) {
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}

	}
	//TODO contructor que recoge el dato seleccionado del la busqueda de punto de emision
	public void ubicarValorPuntoEmision() {  		 
		 puntoEmision = verCamposPE.getId().getCodPuntEmision().trim();
		 TipoAmbiente = verCamposPE.getTipoAmbiente();
    }
	
	//TODO contructor de mensaje de alerta para mostrar al usuario
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
	       FacesContext context = FacesContext.getCurrentInstance();            
	       context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	}
	
	//TODO cargar datos de informacion tributaria
	public void cargaInformacionTributaria(){
		try{
	    	empresa = new FacEmpresa();
			empresa = NotaDebitoServicio.buscarDatosPorRuc(ruc);
			empresa.setObligContabilidad((empresa.getObligContabilidad().trim().equals("S") ? "SI" : "NO"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO Contructores de get y set
	
	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public List<FacGeneral> getTablaPorcentaje() {
		return TablaPorcentaje;
	}

	public void setTablaPorcentaje(List<FacGeneral> tablaPorcentaje) {
		TablaPorcentaje = tablaPorcentaje;
	}

	public List<FacGeneral> getFiltraTablaPorcentaje() {
		return filtraTablaPorcentaje;
	}

	public void setFiltraTablaPorcentaje(List<FacGeneral> filtraTablaPorcentaje) {
		this.filtraTablaPorcentaje = filtraTablaPorcentaje;
	}

	public FacGeneral getVerCamposPorcentaje() {
		return verCamposPorcentaje;
	}

	public void setVerCamposPorcentaje(FacGeneral verCamposPorcentaje) {
		this.verCamposPorcentaje = verCamposPorcentaje;
	}

	public double getSubtotalSinImpuesto() {
		return subtotalSinImpuesto;
	}

	public void setSubtotalSinImpuesto(double subtotalSinImpuesto) {
		this.subtotalSinImpuesto = subtotalSinImpuesto;
	}

	public double getSubtotal12() {
		return subtotal12;
	}

	public void setSubtotal12(double subtotal12) {
		this.subtotal12 = subtotal12;
	}

	public double getSubtotal0() {
		return subtotal0;
	}

	public void setSubtotal0(double subtotal0) {
		this.subtotal0 = subtotal0;
	}

	public double getSubtotalNoIva() {
		return subtotalNoIva;
	}

	public void setSubtotalNoIva(double subtotalNoIva) {
		this.subtotalNoIva = subtotalNoIva;
	}

	public double getTotalvalorICE() {
		return totalvalorICE;
	}

	public void setTotalvalorICE(double totalvalorICE) {
		this.totalvalorICE = totalvalorICE;
	}

	public double getIva12() {
		return iva12;
	}

	public void setIva12(double iva12) {
		this.iva12 = iva12;
	}

	public double getValorTotalFactura() {
		return valorTotalFactura;
	}

	public void setValorTotalFactura(double valorTotalFactura) {
		this.valorTotalFactura = valorTotalFactura;
	}

	public List<SelectItem> getListTipImpuesto() {
		return listTipImpuesto;
	}

	public void setListTipImpuesto(List<SelectItem> listTipImpuesto) {
		this.listTipImpuesto = listTipImpuesto;
	}

	public Date getFecDeEmisionModif() {
		return FecDeEmisionModif;
	}

	public void setFecDeEmisionModif(Date FecDeEmisionModif) {
		FecDeEmisionModif = FecDeEmisionModif;
	}

	public String getNoComprobanteModif() {
		return noComprobanteModif;
	}

	public void setNoComprobanteModif(String noComprobanteModif) {
		this.noComprobanteModif = noComprobanteModif;
	}

	public int getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}

	public String getPuntoEmision() {
		return puntoEmision;
	}

	public void setPuntoEmision(String puntoEmision) {
		this.puntoEmision = puntoEmision;
	}

	public List<FacPuntoEmision> getListPuntosEmision() {
		return listPuntosEmision;
	}

	public void setListPuntosEmision(List<FacPuntoEmision> listPuntosEmision) {
		this.listPuntosEmision = listPuntosEmision;
	}

	public FacPuntoEmision getPuntoEmisionObj() {
		return puntoEmisionObj;
	}

	public void setPuntoEmisionObj(FacPuntoEmision puntoEmisionObj) {
		this.puntoEmisionObj = puntoEmisionObj;
	}

	public FacPuntoEmisionPK getPuntoEmisionPK() {
		return puntoEmisionPK;
	}

	public void setPuntoEmisionPK(FacPuntoEmisionPK puntoEmisionPK) {
		this.puntoEmisionPK = puntoEmisionPK;
	}

	public List<FacPuntoEmision> getFiltraPuntosEmision() {
		return filtraPuntosEmision;
	}

	public void setFiltraPuntosEmision(List<FacPuntoEmision> filtraPuntosEmision) {
		this.filtraPuntosEmision = filtraPuntosEmision;
	}

	public FacPuntoEmision getVerCamposPE() {
		return verCamposPE;
	}

	public void setVerCamposPE(FacPuntoEmision verCamposPE) {
		this.verCamposPE = verCamposPE;
	}

	public boolean isVisibleBotonePuntoEmision() {
		return visibleBotonePuntoEmision;
	}

	public void setVisibleBotonePuntoEmision(boolean visibleBotonePuntoEmision) {
		this.visibleBotonePuntoEmision = visibleBotonePuntoEmision;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoComprador() {
		return tipoComprador;
	}

	public void setTipoComprador(String tipoComprador) {
		this.tipoComprador = tipoComprador;
	}

	public List<SelectItem> getTiposComprador() {
		return tiposComprador;
	}

	public void setTiposComprador(List<SelectItem> tiposComprador) {
		this.tiposComprador = tiposComprador;
	}

	public String getIdentificacionDelComprador() {
		return identificacionDelComprador;
	}

	public void setIdentificacionDelComprador(String identificacionDelComprador) {
		this.identificacionDelComprador = identificacionDelComprador;
	}

	public boolean isBanderaComprador() {
		return banderaComprador;
	}

	public void setBanderaComprador(boolean banderaComprador) {
		this.banderaComprador = banderaComprador;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccionEstablecimiento() {
		return direccionEstablecimiento;
	}

	public void setDireccionEstablecimiento(String direccionEstablecimiento) {
		this.direccionEstablecimiento = direccionEstablecimiento;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTcRuc() {
		return tcRuc;
	}

	public void setTcRuc(String tcRuc) {
		this.tcRuc = tcRuc;
	}

	public String getTcCed() {
		return tcCed;
	}

	public void setTcCed(String tcCed) {
		this.tcCed = tcCed;
	}

	public String getTcPas() {
		return tcPas;
	}

	public void setTcPas(String tcPas) {
		this.tcPas = tcPas;
	}

	public String getTcCof() {
		return tcCof;
	}

	public void setTcCof(String tcCof) {
		this.tcCof = tcCof;
	}

	public FacCliente getComprador() {
		return comprador;
	}

	public void setComprador(FacCliente comprador) {
		this.comprador = comprador;
	}

	public boolean isPresenta() {
		return presenta;
	}

	public void setPresenta(boolean presenta) {
		this.presenta = presenta;
	}

	public String getTipoDocumentoModifi() {
		return TipoDocumentoModifi;
	}

	public void setTipoDocumentoModifi(String tipoDocumentoModifi) {
		TipoDocumentoModifi = tipoDocumentoModifi;
	}

	public List<SelectItem> getListaTipoDocumentoModif() {
		return listaTipoDocumentoModif;
	}

	public void setListaTipoDocumentoModif(List<SelectItem> listaTipoDocumentoModif) {
		this.listaTipoDocumentoModif = listaTipoDocumentoModif;
	}

	public String getCodDocModificable() {
		return codDocModificable;
	}

	public void setCodDocModificable(String codDocModificable) {
		this.codDocModificable = codDocModificable;
	}

	public String getTipoEmision() {
		return tipoEmision;
	}

	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
	}

	public List<SelectItem> getLitipoEmision() {
		return litipoEmision;
	}

	public void setLitipoEmision(List<SelectItem> litipoEmision) {
		this.litipoEmision = litipoEmision;
	}

	public Date getFechaDeEmision() {
		return FechaDeEmision;
	}

	public void setFechaDeEmision(Date fechaDeEmision) {
		FechaDeEmision = fechaDeEmision;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getCodEstablecimiento() {
		return codEstablecimiento;
	}

	public void setCodEstablecimiento(String codEstablecimiento) {
		this.codEstablecimiento = codEstablecimiento;
	}

	public List<FacEstablecimiento> getListEstablecimientos() {
		return listEstablecimientos;
	}

	public void setListEstablecimientos(
			List<FacEstablecimiento> listEstablecimientos) {
		this.listEstablecimientos = listEstablecimientos;
	}

	public List<FacEstablecimiento> getFiltraEstablecimiento() {
		return filtraEstablecimiento;
	}

	public void setFiltraEstablecimiento(
			List<FacEstablecimiento> filtraEstablecimiento) {
		this.filtraEstablecimiento = filtraEstablecimiento;
	}

	public FacEstablecimiento getVerCamposEstablecimiento() {
		return verCamposEstablecimiento;
	}

	public void setVerCamposEstablecimiento(
			FacEstablecimiento verCamposEstablecimiento) {
		this.verCamposEstablecimiento = verCamposEstablecimiento;
	}

	public FacEstablecimiento getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(FacEstablecimiento establecimiento) {
		this.establecimiento = establecimiento;
	}

	public FacEstablecimientoPK getEstablecimientoPK() {
		return establecimientoPK;
	}

	public void setEstablecimientoPK(FacEstablecimientoPK establecimientoPK) {
		this.establecimientoPK = establecimientoPK;
	}

	public List<FacDetAdicional> getListPantallaDetalleAdicional() {
		return listPantallaDetalleAdicional;
	}

	public void setListPantallaDetalleAdicional(
			List<FacDetAdicional> listPantallaDetalleAdicional) {
		this.listPantallaDetalleAdicional = listPantallaDetalleAdicional;
	}

	public FacDetAdicional getDetDocumentoUI() {
		return detDocumentoUI;
	}

	public void setDetDocumentoUI(FacDetAdicional detDocumentoUI) {
		this.detDocumentoUI = detDocumentoUI;
	}

	public PantallaDetalleDocumentoDebito getPantallaDetalleDocumento() {
		return pantallaDetalleDocumento;
	}

	public void setPantallaDetalleDocumento(
			PantallaDetalleDocumentoDebito pantallaDetalleDocumento) {
		this.pantallaDetalleDocumento = pantallaDetalleDocumento;
	}

	public List<PantallaDetalleDocumentoDebito> getListPantallaDetalleDocumento() {
		return listPantallaDetalleDocumento;
	}

	public void setListPantallaDetalleDocumento(
			List<PantallaDetalleDocumentoDebito> listPantallaDetalleDocumento) {
		this.listPantallaDetalleDocumento = listPantallaDetalleDocumento;
	}

	public UIData getDataTableDetalle() {
		return DataTableDetalle;
	}

	public void setDataTableDetalle(UIData dataTableDetalle) {
		DataTableDetalle = dataTableDetalle;
	}

	public PantallaDetalleDocumentoDebito getSeleccionaDetalleDocumento() {
		return seleccionaDetalleDocumento;
	}

	public void setSeleccionaDetalleDocumento(
			PantallaDetalleDocumentoDebito seleccionaDetalleDocumento) {
		this.seleccionaDetalleDocumento = seleccionaDetalleDocumento;
	}

}
