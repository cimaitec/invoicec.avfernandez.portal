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
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacDetRetencionPK;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.PantallaDetalleDocumentoRetencion;
import com.documentos.servicios.ComprobanteRentencionServicios;
import com.general.entidades.FacCliente;
import com.general.entidades.FacEstablecimiento;
import com.general.entidades.FacEstablecimientoPK;
import com.general.entidades.FacGeneral;
import com.general.entidades.FacPuntoEmision;
import com.general.entidades.FacPuntoEmisionPK;
import com.general.entidades.FacTiposDocumento;

@SessionScoped
@ManagedBean
public class ComprobanteRetencionControlador {

	@EJB
	private ComprobanteRentencionServicios ComrpobanteServicios;
	
	private String codigoTipoDocumento = "07";
	private FacEmpresa empresa;
	private String codEstablecimiento;
	private int secuencial;
	private String tipoDocumento;
	protected String ruc;
	protected String usuario;
	private String periodoFiscal;
	public int TamañoSecuencial = 9;
	
	// variable de comprobantes
	private String noComprobante;
	private Date fecComprobante;
	
	
	//variable de informacion de sujeto retenido
	private String tiposujetoRetenido;
	private List<SelectItem> tipossujetoRetenido;
	private String identificacionDelsujetoRetenido;
	private boolean banderasujetoRetenido;
	private String razonSocial;
	private String direccionEstablecimiento;
	private String correoElectronico;
	private String telefono;
	private String tcRuc= "04"; 
	private String tcCed= "05";
	private String tcPas= "06";
	private String tcCof= "07";
	private FacCliente sujetoretenido;
	private boolean presenta;
	
	
	// variable que contiene el establecimiento
	private List<FacEstablecimiento> listEstablecimientos;
	private List<FacEstablecimiento> filtraEstablecimiento; // variable que se encarga de filtrar la los establecimiento
	private FacEstablecimiento verCamposEstablecimiento;// variable que se encarga de recoger el registro que el usuario selecciono el establecimiento
	private FacEstablecimiento establecimiento;
	private FacEstablecimientoPK establecimientoPK;
	private boolean visibleBotonEstablecimiento;// true para bloquear
	
	// variable de punto de emision
	private String puntoEmision;
	private List<FacPuntoEmision> listPuntosEmision;
	private FacPuntoEmision puntoEmisionObj;
	private FacPuntoEmisionPK puntoEmisionPK;
	private List<FacPuntoEmision> filtraPuntosEmision; // variable que se encarga de filtrar la los roles
	private FacPuntoEmision verCamposPE;// variable que se encarga de recoger el registro que el usuario a seleccionado el punto de emision

	// variable de tipo de emision
	private String tipoEmision;
	private List<SelectItem> litipoEmision;
	private Date FechaDeEmision;
	
	// variable de detalle adicional
	private List<FacDetAdicional>   listPantallaDetalleAdicional;
	private FacDetAdicional detDocumentoUI;
	
	//variable de detalle del impuesto
	private List<PantallaDetalleDocumentoRetencion> listPantallaDetalleDocumento;
	private PantallaDetalleDocumentoRetencion pantallaDetalleDocumento;
	private List<SelectItem> list_cod_impuesto;
	private PantallaDetalleDocumentoRetencion seleccionDetalleDocumento;
	private UIData seleccionDetalleDocumento2;
	private List<FacGeneral> datacodprocentaje;
	private List<FacGeneral> filtracodprocentaje;
	private FacGeneral verCamposporcentaje;
	private double valorTotal_impuesto;//
	
	//variable que son para bloquear los botones
	private boolean visibleBotonEmision;// true para bloquear
	public String Evento2;
	protected String tipoAmbiente;
	public String Espacios = "";

	
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
        this.cargaInformacionTributaria();
        
        //tipos Emision
        LlenarComboGeneral("8",litipoEmision,"fac_general");
        tipoEmision = "0";
        
        //tipos de sujeto retenido
        LlenarComboGeneral("16",tipossujetoRetenido,"fac_general");
        tiposujetoRetenido = "0";
        
        //lista de codigo del impuesto
        LlenarComboGeneral("29",list_cod_impuesto,"fac_general");
        
        nombreDocumento();
        visibleBotonEmision = true;
	}
	
	//TODO contructor que se encarga de limpiar todos los controles
	private void limpiarcontroles(){
		valorTotal_impuesto = 0;
		codEstablecimiento = null;
		secuencial = 0;
		tipoDocumento = null;
		periodoFiscal = null;
		litipoEmision = new ArrayList<SelectItem>();
		tipossujetoRetenido = new ArrayList<SelectItem>();
		list_cod_impuesto = new ArrayList<SelectItem>();
        listPantallaDetalleDocumento = new ArrayList<PantallaDetalleDocumentoRetencion>();
        listPantallaDetalleAdicional = new ArrayList<FacDetAdicional>();
        FechaDeEmision = new Date();
        puntoEmision = null;
        periodoFiscal = null;
        identificacionDelsujetoRetenido = null;
        razonSocial = null;
        direccionEstablecimiento = null;
        correoElectronico = null;
        telefono = null;
        noComprobante = null;
        fecComprobante = new Date();
        Evento2 = "";
        tipoAmbiente = "";
	}
	
	//TODO contructor que se encarga de filtrar el tipo de documento
	private void nombreDocumento(){
		try{
			List<FacTiposDocumento> documento = new ArrayList<FacTiposDocumento>();
			documento = ComrpobanteServicios.buscartipoDocumento(codigoTipoDocumento);
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
				listGeneral = ComrpobanteServicios.buscarDatosPorCodigo(codigoGeneral);
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
	
	//TODO contructor que buscar al sujeto retenido por medio de la identificacion del cliente
	public void buscarSujetoRetenido(){
		banderasujetoRetenido = false;
        int longitudIdentificacion;
        try{
        	if (tiposujetoRetenido.equals("0") || tiposujetoRetenido.equals(" ") || tiposujetoRetenido.equals("") || tiposujetoRetenido == null){
				this.setIdentificacionDelsujetoRetenido("");
				this.setRazonSocial("");
				this.setDireccionEstablecimiento("");
				this.setCorreoElectronico("");
				this.setTelefono("");
				mensajeAlerta("Mensaje del sistema","Seleccione Tipo de Comprador", "Informacion");
			}else{
				if (identificacionDelsujetoRetenido != null) {
					/* "04        ";"VENTA CON RUC"
					"05        ";"VENTA CON CEDULA"
					"06        ";"VENTA CON PASAPORTE"
					"07        ";"VENTA CON CONSUMIDOR FINAL" */
					longitudIdentificacion = identificacionDelsujetoRetenido.length();
					
					if (tiposujetoRetenido.trim().equals(tcRuc)) 
                          if(longitudIdentificacion == 13)
        					banderasujetoRetenido = true;
				
					if (tiposujetoRetenido.trim().equals(tcCed))
      					if(longitudIdentificacion == 10)
      					banderasujetoRetenido = true;

					if (tiposujetoRetenido.trim().equals(tcPas))
     					if(longitudIdentificacion == 10)
      					 banderasujetoRetenido = true;

					if (tiposujetoRetenido.trim().equals(tcCof))
						if(longitudIdentificacion == 10)
							banderasujetoRetenido = true;
					
					if (banderasujetoRetenido){
						sujetoretenido = ComrpobanteServicios.buscarsujetoretenido(identificacionDelsujetoRetenido);
						if (sujetoretenido != null){
							this.setRazonSocial(sujetoretenido.getRazonSocial());
							this.setDireccionEstablecimiento(sujetoretenido.getDireccion());
							this.setCorreoElectronico(sujetoretenido.getEmail());
							this.setTelefono(sujetoretenido.getTelefono());
							presenta = true;
						}else{
							mensajeAlerta("Mensaje del sistema","Comprador:: El Comprador No Existe", "peligro");
						}
					} else {
						mensajeAlerta("Mensaje del sistema","Comprador:: Documento Invalido", "peligro");
					}
					
				}else{
					this.setRazonSocial("");
					this.setDireccionEstablecimiento("");
					this.setCorreoElectronico("");
					this.setTelefono("");
					mensajeAlerta("Mensaje del sistema","Ingrese Identificacion Comprador", "Informacion");				
				}				
			}
        }catch (Exception e) {
			String mensaje = e.getMessage();
			this.setRazonSocial("");
			this.setDireccionEstablecimiento("");
			this.setCorreoElectronico("");
			this.setTelefono("");
			banderasujetoRetenido = false;
			if (mensaje == null){
				mensaje = " ";
			}else
			mensajeAlerta("Mensaje del sistema","Error en la Identificacion del Comprador " + mensaje, "alerta");
		}
	}
	
	//TODO contructor que valida que el detalle del registro no se repita
	private boolean ValidarRegistroDetalle(List<PantallaDetalleDocumentoRetencion> puntoEmision,List<String> Contenidos){
		Boolean validacion = false;
		int contador = 0;
		for (PantallaDetalleDocumentoRetencion contenido : puntoEmision) 
			if((Contenidos.get(0).toString().trim().equals(contenido.getCod_impuesto().toString().trim()) == true) && 
					(Contenidos.get(0).toString().trim().equals(contenido.getCod_porcentaje().toString().trim()) == true)){
				contador ++;
				if(contador == 2)
					return validacion = true;
			}
		
		return validacion;
	}
	
	//TODO cargar datos de informacion tributaria
	public void cargaInformacionTributaria(){
		try{
	    	empresa = new FacEmpresa();
			empresa = ComrpobanteServicios.buscarDatosPorRuc(ruc);
			empresa.setObligContabilidad((empresa.getObligContabilidad().trim().equals("S") ? "SI" : "NO"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	  }
	
	//TODO contructo que va a llenar el establecimiento para la busqueda
	public void llenaEstablecimientos(){
		try{
			filtraEstablecimiento = new ArrayList<FacEstablecimiento>();
			listEstablecimientos = new ArrayList<FacEstablecimiento>();
			listEstablecimientos = ComrpobanteServicios.buscarDatosEstablecimiento(empresa.getRuc(), usuario);		
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
	
	//TODO contructor que recoge el dato seleccionado del la busqueda de establecimiento
	public void ubicarValorEstablecimiento() {  		 
		 codEstablecimiento = verCamposEstablecimiento.getId().getCodEstablecimiento().trim();
		 if(codEstablecimiento != ""){
			 visibleBotonEmision = false;
		 }
    } 
	
	//TODO contuctor que llena los punto de emision dependiendo del establecimiento seleccionado
	public void llenaPuntosEmision(){
		try{
			if (codEstablecimiento == null){
				mensajeAlerta("Mensaje del sistema","Ingrese El Establecimiento antes del Punto Emision", "peligro");
				return;
			}
			filtraPuntosEmision = new ArrayList<FacPuntoEmision>();
				listPuntosEmision = ComrpobanteServicios.buscarDatosPuntoEmision(empresa.getRuc(),codEstablecimiento,codigoTipoDocumento.trim());	
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
	
	//TODO contructor que se encarga de autocompletar el ruc del comprador para facilitar al usuario
	public List<String> complete_RucComprador(String parametro_ruc) throws Exception{
		List<String> resultado = new ArrayList<String>();
		resultado = ComrpobanteServicios.BuscarfitroEmpresaDocumentos(parametro_ruc.trim(), empresa.getRuc().trim(), tiposujetoRetenido.trim());	
		return resultado;
	}
	
	//TODO contructor que se encarga de validar que todo los campos esten ingresados
	private boolean validarCampos(){
		if (codEstablecimiento == null || codEstablecimiento == "" || puntoEmision == "" || puntoEmision == null || tiposujetoRetenido.equals("0") || identificacionDelsujetoRetenido == null || identificacionDelsujetoRetenido == "" || banderasujetoRetenido== false || FechaDeEmision == null){
			if (codEstablecimiento == null || codEstablecimiento == ""){
				mensajeAlerta("Mensaje del sistema","Establecimiento:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");		
				return true;
			}
			
			if(puntoEmision == "0" || puntoEmision == null || puntoEmision == ""){
				mensajeAlerta("Mensaje del sistema","Punto de Emision:: Se Encuentra Vacio, Porfavor ingreselo", "peligro");				
				return true;
			}
			
			if(tiposujetoRetenido.equals("0")){
		  		 mensajeAlerta("Mensaje del sistema","El Campo Tipo Comprador  Se Encuentra Vacio, Porfavor ingreselo", "peligro");
		  		return true;
			}
			
			if(identificacionDelsujetoRetenido.equals("null") || identificacionDelsujetoRetenido == "" || razonSocial == null || razonSocial.trim().equals("")){
				mensajeAlerta("Mensaje del sistema","El Campo: Identificacion del Comprador  Se Encuentra Vacio, Porfavor ingreselo", "peligro");
				return true;
			}
	
			if(banderasujetoRetenido== false){
				mensajeAlerta("Mensaje del sistema","El Campo Identificacion Comprador Es incorrecto ", "peligro");
				return true;    
			}
	
			if(FechaDeEmision== null){
				mensajeAlerta("Mensaje del sistema","El Campo Fecha Emision Esta Vacio ", "peligro");
				return true;
			}
		}
		return false;
	}
	
	//TODO construcor que se encarga de crear el nuevo detalle del documento de impuesto
	public void nuevoDetalle(){
		
		if (validarCampos())
			return;

		pantallaDetalleDocumento = new PantallaDetalleDocumentoRetencion();
		pantallaDetalleDocumento.setRuc(ruc);
		pantallaDetalleDocumento.setCodEstablecimiento(codEstablecimiento);
		pantallaDetalleDocumento.setPuntoEmision(puntoEmision);
		pantallaDetalleDocumento.setSecuencial(secuencial);
		pantallaDetalleDocumento.setLinea(listPantallaDetalleDocumento.size() + 1 );
		
		pantallaDetalleDocumento.setCod_impuesto(null);
		pantallaDetalleDocumento.setCod_porcentaje(null);
		pantallaDetalleDocumento.setCod_descripcion(null);
		pantallaDetalleDocumento.setVisibleBoton(true);
		pantallaDetalleDocumento.setValorBaseImp(0.00);
		pantallaDetalleDocumento.setPorcentajeRetencion(0.00);
		pantallaDetalleDocumento.setValorTotal(0.00);
		listPantallaDetalleDocumento.add(pantallaDetalleDocumento);
		
	 }
	
	//TODO construcor que se encarga de crear el nuevo detalle adicional del documento de impuesto
    public void nuevoDetalleAdicional(){
			
  	  
	  	if (validarCampos())
			return;
		
		//Lleno Combos Producto		
		FacDetAdicionalPK facDetAdicionalPk = new FacDetAdicionalPK();
		FacDetAdicional facDetAdicional = new FacDetAdicional();
			 //Asigno pk
		facDetAdicionalPk.setRuc(ruc);
		facDetAdicionalPk.setCodEstablecimiento(codEstablecimiento);
		facDetAdicionalPk.setCodPuntEmision(puntoEmision);
		facDetAdicionalPk.setSecuencial(cadenaSecuencial(secuencial,TamañoSecuencial));
		facDetAdicionalPk.setCodigoDocumento(codigoTipoDocumento);
	  	//Genero Secuencial de Detalle
	   	facDetAdicionalPk.setSecuencialDetAdicional(listPantallaDetalleAdicional.size() + 1);
			 

		facDetAdicional.setId(facDetAdicionalPk);		 	
		facDetAdicional.setNombre(null);
		facDetAdicional.setValor(null);
			 
		listPantallaDetalleAdicional.add(facDetAdicional);		
	}
    
  //TODO 
    /**
     * contructor que se encarga de calcular y recalcular los para sacar el valor del documento, 
     * el filtrado de borrar campo lo que hace es que si se selecciona otro tipo de impuesto se limpia los controles de la fila seleccionada
  	**/
  	public void calculaTotal(String evento){
  		 try{
  			PantallaDetalleDocumentoRetencion detDocumen = (PantallaDetalleDocumentoRetencion) seleccionDetalleDocumento2.getRowData();
  			
  			 if(evento.toString().trim().equals("borraCampo")){
  				 
				if(detDocumen.getCod_impuesto() == "0")
					detDocumen.setVisibleBoton(true);
				else
					detDocumen.setVisibleBoton(false);
					
				detDocumen.setCod_porcentaje(null);
				detDocumen.setCod_descripcion(null);
				detDocumen.setValorBaseImp(0.00);
				detDocumen.setPorcentajeRetencion(0.00);
				detDocumen.setValorTotal(0.00);
				
  			 }
  			 else if(evento.toString().trim().equals("llenarcodporcentaje")){
  				datacodprocentaje = new ArrayList<FacGeneral>();
  				filtracodprocentaje = new ArrayList<FacGeneral>();
  				FacGeneral generales = new FacGeneral(); 
  				datacodprocentaje = ComrpobanteServicios.buscarDatosPorCodigoporimpuesto(detDocumen.getCod_impuesto());
				if(datacodprocentaje.isEmpty()){
					generales.setCodUnico("0");
					generales.setCodTabla("0");
					generales.setDescripcion("No hay datos");
					generales.setIdGeneral(0);
					generales.setIsActive("0");
					generales.setPorcentaje(0);
					datacodprocentaje.add(generales);
				}
  			 }
  			 else if(evento.toString().trim().equals("impuestoSeleccionado")){
  				Evento2 = "impuestoSeleccionado";
  				seleccionDetalleDocumento.setCod_porcentaje(verCamposporcentaje.getCodUnico());
  				seleccionDetalleDocumento.setCod_descripcion(verCamposporcentaje.getDescripcion());
  				seleccionDetalleDocumento.setTarifa(verCamposporcentaje.getPorcentaje());
  				calculaTotal("RealizandoCalculo");
  				
  				if(ValidarRegistroDetalle(listPantallaDetalleDocumento, Arrays.asList(
  						seleccionDetalleDocumento.getCod_impuesto(), 
  						seleccionDetalleDocumento.getCod_porcentaje()))){
  					mensajeAlerta("Mensaje del sistema", "el registro seleccionado ya se encuenta registrado", "peligro");  
  					mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "peligro");  
				}
  			 }
  			 else if(evento.toString().trim().equals("RealizandoCalculo")){
  				 double baseimponible = 0, porcentaje = 0;
  				 if(Evento2.equals("impuestoSeleccionado")){
	  				 baseimponible = seleccionDetalleDocumento.getValorBaseImp();
	  				 porcentaje = seleccionDetalleDocumento.getTarifa();
	  				 seleccionDetalleDocumento.setPorcentajeRetencion(porcentaje);
	  				 seleccionDetalleDocumento.setValorTotal(Redondear((baseimponible * (porcentaje / 100)),2));
  				 }
  				 else
  				 {
  					baseimponible = detDocumen.getValorBaseImp();
	  				porcentaje = detDocumen.getTarifa();
	  				detDocumen.setPorcentajeRetencion(porcentaje);
	  				detDocumen.setValorTotal(Redondear((baseimponible * (porcentaje / 100)),2));
  				 }
  				Evento2 = "RealizandoCalculo";
  	  			valorDocumento();
  			 }
  				
  		 }catch (Exception e) {
 			FacesMessage mensaje=null;
 			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
 			FacesContext.getCurrentInstance().addMessage(null, mensaje);
 		}
  	}
  	
  	//TODO contructor que nos sirve para redondear numeros decimales
  	private double Redondear(double numero,int digitos)
  	{
  	      int cifras=(int) Math.pow(10,digitos);
  	      return Math.rint(numero*cifras)/cifras;
  	}
  	
  	//TODO contructor que se encarga de guardar el detalle del comprobante de retencion
  	public void insertarComprobanteRetencion(String NombreBoton){
  		/**
  		 *  verifcando que si preciono el boton de guardar me bloquea los botones, y si es el caso del mensaje de dialogo si es cancelar los desbloquea
  		 *  y tambien para el caso de que acepte tambien hace lo mismo
  		 **/
  		
  		if(validarCampos())
  			return;
  		if(listPantallaDetalleDocumento.size()  == 0){
  			mensajeAlerta("Mensaje del sistema", "No hay registro de retenciones", "alerta");  
  			return;
  		}
  		
		for (PantallaDetalleDocumentoRetencion documento : listPantallaDetalleDocumento) {
			if(ValidarRegistroDetalle(listPantallaDetalleDocumento, Arrays.asList(
						documento.getCod_impuesto(), 
						documento.getCod_porcentaje()))){
					mensajeAlerta("Mensaje del sistema", "el registro ingresado ya se encuenta registrado", "peligro");  
					mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "peligro");
					return;

			}
			if(documento.getValorBaseImp() == 0 && !documento.getCod_impuesto().trim().equals("0")){
				mensajeAlerta("Mensaje del sistema", "la columna + "+ documento.getLinea() + " Detalle Retenciones \n no tiene de Base Imponible", "peligro");  
				return;
			}
			if(documento.getCod_impuesto().trim().equals("0")){
				mensajeAlerta("Mensaje del sistema", "la columna + "+ documento.getLinea() + " Detalle Retenciones \n no tiene seleccionado el codigo del impuesto", "peligro");  
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
  			
  			
  			FacCabDocumento cabDocumento = new FacCabDocumento();
  			FacCabDocumentoPK id = new FacCabDocumentoPK();
  			
  			id.setRuc(empresa.getRuc());
  			id.setCodEstablecimiento(codEstablecimiento);
  			id.setCodPuntEmision(puntoEmision);
  			id.setAmbiente(new Integer(tipoAmbiente.toString().trim().equals("D") ? 1 : 2));
  			secuencial = ComrpobanteServicios.secuencialCabecera(empresa.getRuc(), codEstablecimiento, puntoEmision, codigoTipoDocumento);
  			id.setSecuencial(cadenaSecuencial(secuencial,TamañoSecuencial));
  			id.setCodigoDocumento(codigoTipoDocumento.trim());
  			cabDocumento.setId(id);
  			
  			cabDocumento.setFechaEmision(FechaDeEmision);
			cabDocumento.setGuiaRemision(Espacios);
			cabDocumento.setRazonSocialComprador(razonSocial);
			cabDocumento.setIdentificacionComprador(identificacionDelsujetoRetenido);
			double totalSinImpueso = 0;
			for (PantallaDetalleDocumentoRetencion valorDocumento : listPantallaDetalleDocumento) 
				totalSinImpueso += valorDocumento.getValorBaseImp();
			cabDocumento.setTotalSinImpuesto(totalSinImpueso);
			cabDocumento.setTotalDescuento(0);
			cabDocumento.setEmail(correoElectronico);
			cabDocumento.setPropina(0);
			cabDocumento.setInfoAdicional(Espacios);
			cabDocumento.setPeriodoFiscal(periodoFiscal);
			cabDocumento.setMoneda("DOLAR");
			cabDocumento.setRise(Espacios);
			cabDocumento.setFechaInicioTransporte(null);
			cabDocumento.setFechaFinTransporte(null);
			cabDocumento.setPlaca(Espacios);
			cabDocumento.setMotivoRazon(Espacios);
			cabDocumento.setIdentificacionDestinatario(Espacios);
			cabDocumento.setRazonSocialDestinatario(Espacios);
			cabDocumento.setDireccionDestinatario(direccionEstablecimiento);
			cabDocumento.setMotivoTraslado(Espacios);
			cabDocumento.setDocAduaneroUnico(Espacios);
			cabDocumento.setCodEstablecimientoDest(Espacios);
			cabDocumento.setRuta(Espacios);
			cabDocumento.setCodDocSustento("01");
			cabDocumento.setNumDocSustento(noComprobante.toString().trim());
			cabDocumento.setNumAutDocSustento(Espacios);
			cabDocumento.setFecEmisionDocSustento(fecComprobante);
			cabDocumento.setAutorizacion(Espacios);
			cabDocumento.setFechaautorizacion(null);
			cabDocumento.setClaveAcceso(Espacios);
			cabDocumento.setImporteTotal(valorTotal_impuesto);
			cabDocumento.setCodDocModificado(Espacios);
			cabDocumento.setNumDocModificado(Espacios);
			cabDocumento.setMotivoValor(0);
			cabDocumento.setTipIdentificacionComprador(tiposujetoRetenido);
			cabDocumento.setTipoEmision(tipoEmision);
			cabDocumento.setPartida(Espacios);
			cabDocumento.setSubtotal0(0);
			cabDocumento.setSubtotal12(0);
			cabDocumento.setSubtotalNoIva(0);
			cabDocumento.setTotalvalorICE(0);
			cabDocumento.setIva12(0);
  			cabDocumento.setAutorizacion(Espacios);
  			cabDocumento.setIsActive("Y");
  			cabDocumento.setEstadoTransaccion("IN");
  			cabDocumento.setFechaEmisionDocSustento(fecComprobante.toString());
			guardado = ComrpobanteServicios.insertarCabeceraDocumento(cabDocumento);///SE EJECUTA EL METODO

			cabece_documento.add(cabDocumento);
			
			if(guardado == 0){
				for (PantallaDetalleDocumentoRetencion Detalledocumentos : listPantallaDetalleDocumento) {
					FacDetRetencion cab_impuesto = new FacDetRetencion();
					FacDetRetencionPK cab_impuestopk = new FacDetRetencionPK();
					cab_impuestopk.setRuc(cabDocumento.getId().getRuc());
					cab_impuestopk.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
					cab_impuestopk.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
					cab_impuestopk.setSecuencial(cabDocumento.getId().getSecuencial());
					cab_impuestopk.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
					cab_impuestopk.setCodImpuesto(Integer.parseInt(Detalledocumentos.getCod_impuesto().toString().trim()));
					cab_impuestopk.setSecuencialRetencion(Detalledocumentos.getLinea());
					cab_impuesto.setId(cab_impuestopk);
					cab_impuesto.setCodPorcentaje((Detalledocumentos.getCod_porcentaje().toString().trim()));
					cab_impuesto.setBaseImponible(Detalledocumentos.getValorBaseImp());
					cab_impuesto.setPorcentajeRetencion(Detalledocumentos.getPorcentajeRetencion());
					cab_impuesto.setTarifa(Detalledocumentos.getTarifa());
					cab_impuesto.setValor(Detalledocumentos.getValorTotal());
					guardado = ComrpobanteServicios.insertarimpuesto(cab_impuesto);
					detalle_retencion.add(cab_impuesto);
				}
			}
			else
				mensajeAlerta("Comprobante de Retencion Electronica","Error al Generar la Retencion", "alerta");		
			
			int secuDa = 0;
			if (guardado == 0){
	  				for (int da= 0; da < listPantallaDetalleAdicional.size();da++){
	  					FacDetAdicional detAdicional = new FacDetAdicional();
		  			 	FacDetAdicionalPK detAdicionalPK = new FacDetAdicionalPK();
	  					detAdicionalPK.setRuc(cabDocumento.getId().getRuc());
		  				detAdicionalPK.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
		  				detAdicionalPK.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
		  				detAdicionalPK.setSecuencial(cabDocumento.getId().getSecuencial());
		  				detAdicionalPK.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
	  					guardado = 0;
	  					secuDa = da+1;
	  					detAdicionalPK.setSecuencialDetAdicional(secuDa);
	  					detAdicional.setId(detAdicionalPK);
	  					detAdicional.setNombre(listPantallaDetalleAdicional.get(da).getNombre());
	  					detAdicional.setValor(listPantallaDetalleAdicional.get(da).getValor());
	  					
 	      	  			guardado = ComrpobanteServicios.insertarDetalleAdicional(detAdicional);///SE EJECUTA EL METODO
 	      	  			detelle_adicional.add(detAdicional);
	  				}
				}else{
					mensajeAlerta("Comprobante de Retencion Electronica","Error al Generar la Retencion", "alerta");								
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
			
			if(generarArchivo.generarDocumento() == 0){
				mensajeAlerta("Mensaje del sistema","Se produjo un error al generar el archivo txt", "alerta");
				return;
			}
			try {
	    		Thread.sleep(2000);
     		} catch (InterruptedException e) {
			e.printStackTrace();
    		}
			
			CargarDatos();
			if(guardado == 0)
				mensajeAlerta("Comprobante de Retencion Electronica","Retencion Generada" + cabDocumento.getId().getCodEstablecimiento() + "-" + cabDocumento.getId().getCodPuntEmision() + "-" + cadenaSecuencial(Integer.parseInt(cabDocumento.getId().getSecuencial()),TamañoSecuencial), "Informacion");
			
		}
  		catch (Exception e) {
  			e.printStackTrace();
			FacesMessage mensaje=null;
			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
			FacesContext.getCurrentInstance().addMessage(null, mensaje);
		}
  	}

  	//TODO calculando el valor total del documento
  	public double valorDocumento (){
  		valorTotal_impuesto = 0;
  		if(listPantallaDetalleDocumento.size() > 0)
			for (PantallaDetalleDocumentoRetencion valorDocumento : listPantallaDetalleDocumento) 
				valorTotal_impuesto = valorTotal_impuesto + valorDocumento.getValorTotal();

  		else
  			valorTotal_impuesto = 0;
  		
  		valorTotal_impuesto = Redondear(valorTotal_impuesto, 2);
  		return valorTotal_impuesto;
  	}
  	
	//TODO contructor que se encarga de eliminar el detalle adicional
	public void BorrarLineaDetAdicional(String DetalleDocumento) {
		if(DetalleDocumento.toString().trim().equals("DetalleImpuesto"))
		{
			PantallaDetalleDocumentoRetencion detDocumento = (PantallaDetalleDocumentoRetencion) seleccionDetalleDocumento2.getRowData();
			listPantallaDetalleDocumento.remove(detDocumento);
			int contador = 0;
			for (PantallaDetalleDocumentoRetencion detalle : listPantallaDetalleDocumento) {
				contador++;
				detalle.setLinea(contador);
			}
			valorDocumento();
		}
		else
		{
			listPantallaDetalleAdicional.remove(detDocumentoUI);
		}
   	  
   	}
	
	//TODO contructor que recoge el dato seleccionado del la busqueda de punto de emision
	public void ubicarValorPuntoEmision() {  		 
		 puntoEmision = verCamposPE.getId().getCodPuntEmision().trim();
		 tipoAmbiente = verCamposPE.getTipoAmbiente();
    } 
	
	//TODO contructor de mensaje de alerta para mostrar al usuario
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
		FacesContext context = FacesContext.getCurrentInstance();            
	    context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
    }
	
	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
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

	public int getSecuencial() {
		return secuencial;
	}

	public void setSecuencial(int secuencial) {
		this.secuencial = secuencial;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public boolean isVisibleBotonEmision() {
		return visibleBotonEmision;
	}

	public void setVisibleBotonEmision(boolean visibleBotonEmision) {
		this.visibleBotonEmision = visibleBotonEmision;
	}

	public boolean isVisibleBotonEstablecimiento() {
		return visibleBotonEstablecimiento;
	}

	public void setVisibleBotonEstablecimiento(boolean visibleBotonEstablecimiento) {
		this.visibleBotonEstablecimiento = visibleBotonEstablecimiento;
	}

	public String getPeriodoFiscal() {
		return periodoFiscal;
	}

	public void setPeriodoFiscal(String periodoFiscal) {
		this.periodoFiscal = periodoFiscal;
	}

	public String getTiposujetoRetenido() {
		return tiposujetoRetenido;
	}

	public void setTiposujetoRetenido(String tiposujetoRetenido) {
		this.tiposujetoRetenido = tiposujetoRetenido;
	}

	public List<SelectItem> getTipossujetoRetenido() {
		return tipossujetoRetenido;
	}

	public void setTipossujetoRetenido(List<SelectItem> tipossujetoRetenido) {
		this.tipossujetoRetenido = tipossujetoRetenido;
	}

	public String getIdentificacionDelsujetoRetenido() {
		return identificacionDelsujetoRetenido;
	}

	public void setIdentificacionDelsujetoRetenido(
			String identificacionDelsujetoRetenido) {
		this.identificacionDelsujetoRetenido = identificacionDelsujetoRetenido;
	}

	public boolean isBanderasujetoRetenido() {
		return banderasujetoRetenido;
	}

	public void setBanderasujetoRetenido(boolean banderasujetoRetenido) {
		this.banderasujetoRetenido = banderasujetoRetenido;
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

	public FacCliente getSujetoretenido() {
		return sujetoretenido;
	}

	public void setSujetoretenido(FacCliente sujetoretenido) {
		this.sujetoretenido = sujetoretenido;
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

	public boolean isPresenta() {
		return presenta;
	}

	public void setPresenta(boolean presenta) {
		this.presenta = presenta;
	}

	public String getNoComprobante() {
		return noComprobante;
	}

	public void setNoComprobante(String noComprobante) {
		this.noComprobante = noComprobante;
	}

	public Date getfecComprobante() {
		return fecComprobante;
	}

	public void setfecComprobante(Date fecComprobante) {
		this.fecComprobante = fecComprobante;
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
		
	public List<PantallaDetalleDocumentoRetencion> getListPantallaDetalleDocumento() {
		return listPantallaDetalleDocumento;
	}

	public void setListPantallaDetalleDocumento(
			List<PantallaDetalleDocumentoRetencion> listPantallaDetalleDocumento) {
		this.listPantallaDetalleDocumento = listPantallaDetalleDocumento;
	}

	public PantallaDetalleDocumentoRetencion getPantallaDetalleDocumento() {
		return pantallaDetalleDocumento;
	}

	public void setPantallaDetalleDocumento(
			PantallaDetalleDocumentoRetencion pantallaDetalleDocumento) {
		this.pantallaDetalleDocumento = pantallaDetalleDocumento;
	}

	public List<SelectItem> getList_cod_impuesto() {
		return list_cod_impuesto;
	}

	public void setList_cod_impuesto(List<SelectItem> list_cod_impuesto) {
		this.list_cod_impuesto = list_cod_impuesto;
	}

	public PantallaDetalleDocumentoRetencion getSeleccionDetalleDocumento() {
		return seleccionDetalleDocumento;
	}

	public void setSeleccionDetalleDocumento(
			PantallaDetalleDocumentoRetencion seleccionDetalleDocumento) {
		this.seleccionDetalleDocumento = seleccionDetalleDocumento;
	}

	public UIData getSeleccionDetalleDocumento2() {
		return seleccionDetalleDocumento2;
	}

	public void setSeleccionDetalleDocumento2(UIData seleccionDetalleDocumento2) {
		this.seleccionDetalleDocumento2 = seleccionDetalleDocumento2;
	}

	public List<FacGeneral> getDatacodprocentaje() {
		return datacodprocentaje;
	}

	public void setDatacodprocentaje(List<FacGeneral> datacodprocentaje) {
		this.datacodprocentaje = datacodprocentaje;
	}

	public List<FacGeneral> getFiltracodprocentaje() {
		return filtracodprocentaje;
	}

	public void setFiltracodprocentaje(List<FacGeneral> filtracodprocentaje) {
		this.filtracodprocentaje = filtracodprocentaje;
	}

	public FacGeneral getVerCamposporcentaje() {
		return verCamposporcentaje;
	}

	public void setVerCamposporcentaje(FacGeneral verCamposporcentaje) {
		this.verCamposporcentaje = verCamposporcentaje;
	}

	public double getValorTotal_impuesto() {
		return valorTotal_impuesto;
	}

	public void setValorTotal_impuesto(double valorTotal_impuesto) {
		this.valorTotal_impuesto = valorTotal_impuesto;
	}
	
}
