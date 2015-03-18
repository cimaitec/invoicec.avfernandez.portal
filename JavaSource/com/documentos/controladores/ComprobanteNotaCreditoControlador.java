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
import com.documentos.entidades.FacDetDocumentoPK;
import com.documentos.entidades.FacDetImpuesto;
import com.documentos.entidades.FacDetImpuestoPK;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacDetRetencion;
import com.documentos.entidades.FacEmpresa;
import com.documentos.entidades.PantallaDetalleDocumentoCredito;
import com.documentos.servicios.ComprobanteNotaCreditoServicios;
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
public class ComprobanteNotaCreditoControlador {

	@EJB
	private ComprobanteNotaCreditoServicios NotaCreditoServicio;
	
	private String codigoTipoDocumento = "'04'";
	protected String codigoTipoDocumento2 = "04";
	private FacEmpresa empresa;
	protected String ruc; //cima
	protected String usuario;
	private String tipoDocumento;
	private int secuencial;
	protected String TipoAmbiente;
	public int TamañoSecuencial = 9;
	
	// variable de tipo de emision
	private String tipoEmision;
	private List<SelectItem> litipoEmision;
	private Date FechaDeEmision;
	
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
	private boolean flagCtrlPrecio;
	private String ambientePtoEmision;
	
	// variable de documento que se modifica
	private String TipoDocumentoModifi;
	private List<SelectItem> listaTipoDocumentoModif;
	protected String codDocModificable = "'01'";
	private Date FecDeEmisionModif;
	private String noComprobanteModif;
	private String MotivoModifi;
	
	// variable de detalle adicional
	private List<FacDetAdicional>   listPantallaDetalleAdicional;
	private FacDetAdicional detDocumentoUI;
		
	// variable del detalle del documento del detalle del producto con sus variables totales
	private PantallaDetalleDocumentoCredito pantallaDetalleDocumento;
	private List<PantallaDetalleDocumentoCredito> listPantallaDetalleDocumento;
	private UIData DataTableDetalle;
	private PantallaDetalleDocumentoCredito seleccionaDetalleDocumento;
	public String Evento2;
	private double subtotalSinImpuesto;
	private double subtotal12;
	private double subtotal0;
	private double subtotalNoIva;
	private double totalDescuento;
	private double totalvalorICE;
	private double iva12;
	private double propina10;
	private boolean propina; 
	private double valorTotalFactura;
	
	// variable de la tabla de producto
	private List<FacProducto> TablaProducto;
	private List<String> tablaProductoTipo;
	private List<FacProducto> filtraTablaProducto;	
	private FacProducto verCamposProducto;
	
	/* para generar el archivo de texto */
	String Espacios = "";
	
	//TODO contructor que se encarga de limpiar todos los controles
	private void limpiarcontroles(){
		subtotal0 = 0;
		subtotal12 = 0;
		subtotalNoIva = 0;
		subtotalSinImpuesto = 0;
		totalDescuento = 0;
		totalvalorICE = 0;
		iva12 = 0;
		propina10 = 0;
		propina = false;
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
		TipoDocumentoModifi = new String();
		listPantallaDetalleDocumento = new ArrayList<PantallaDetalleDocumentoCredito>();
		listPantallaDetalleAdicional = new ArrayList<FacDetAdicional>();
		listaTipoDocumentoModif = new ArrayList<SelectItem>();
		tiposComprador = new ArrayList<SelectItem>();
		listPuntosEmision = new ArrayList<FacPuntoEmision>();
		filtraEstablecimiento = new ArrayList<FacEstablecimiento>();
		listEstablecimientos = new ArrayList<FacEstablecimiento>();
		litipoEmision = new ArrayList<SelectItem>();
		Evento2 = "";
		TipoAmbiente = "";
		FecDeEmisionModif = new Date();
		noComprobanteModif = null;
		MotivoModifi = null;
		flagCtrlPrecio = true;
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
		flagCtrlPrecio = true;
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
		nombreDocumento();
	}

	
	//TODO cargar datos de informacion tributaria
	public void cargaInformacionTributaria(){
		try{
	    	empresa = new FacEmpresa();
			empresa = NotaCreditoServicio.buscarDatosPorRuc(ruc);
			empresa.setObligContabilidad((empresa.getObligContabilidad().trim().equals("S") ? "SI" : "NO"));
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
	
	//TODO contructo que va a llenar el establecimiento para la busqueda
	public void llenaEstablecimientos(){
		try{
			listEstablecimientos = NotaCreditoServicio.buscarDatosEstablecimiento(empresa.getRuc(), usuario);		
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
		 visibleBotonePuntoEmision = false;
    }
	
	//TODO contructor que recoge el dato seleccionado del la busqueda de punto de emision
	public void ubicarValorPuntoEmision() {  		 
		 puntoEmision = verCamposPE.getId().getCodPuntEmision().trim();
		 TipoAmbiente = verCamposPE.getTipoAmbiente();
		 ambientePtoEmision = verCamposPE.getTipoAmbiente();
    }
	
	//TODO construcor que se encarga de crear el nuevo detalle del documento de impuesto
	public void nuevoDetalle(String evento){
		if (validarCampos())
			return;
		if(evento.toString().trim().equals("Documento")){
			pantallaDetalleDocumento = new PantallaDetalleDocumentoCredito();
			pantallaDetalleDocumento.setRuc(ruc);
			pantallaDetalleDocumento.setCodEstablecimiento(codEstablecimiento);
			pantallaDetalleDocumento.setPuntoEmision(puntoEmision);
			pantallaDetalleDocumento.setSecuencial(secuencial);
			pantallaDetalleDocumento.setLinea(listPantallaDetalleDocumento.size() + 1);
			pantallaDetalleDocumento.setCodigoProducto("");
			pantallaDetalleDocumento.setDescripcion("");
			pantallaDetalleDocumento.setCantidad(0);
			pantallaDetalleDocumento.setPrecio(0.00);
			pantallaDetalleDocumento.setDescuento(0.00);
			pantallaDetalleDocumento.setValorTotal(0.00);
			pantallaDetalleDocumento.setValorSubtotal(0.00);
			pantallaDetalleDocumento.setValorIce(0.00);
			pantallaDetalleDocumento.setValorIVA(0.00);
			pantallaDetalleDocumento.setTipoIVA(0);
			pantallaDetalleDocumento.setTipoIce(0);
			pantallaDetalleDocumento.setDescripcionIce("");
			pantallaDetalleDocumento.setDescripcionIVA("");
			pantallaDetalleDocumento.setPorcentajeIVA(0);
			pantallaDetalleDocumento.setPorcentajeICE(0);
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
			facDetAdicionalPk.setSecuencial(cadenaSecuencial(secuencial,TamañoSecuencial));
			facDetAdicionalPk.setCodigoDocumento(codigoTipoDocumento2);
			facDetAdicionalPk.setSecuencialDetalle(listPantallaDetalleAdicional.size() + 1);
		  	//Genero Secuencial de Detalle
		   	facDetAdicionalPk.setSecuencialDetAdicional(listPantallaDetalleAdicional.size() + 1);
				 

			facDetAdicional.setId(facDetAdicionalPk);		 	
			facDetAdicional.setNombre(null);
			facDetAdicional.setValor(null);
				 
			listPantallaDetalleAdicional.add(facDetAdicional);
		}
	}
	
	//TODO contructor que se encarga de validar que todo los campos esten ingresados
	private boolean validarCampos(){
		boolean valida = false;
		if (codEstablecimiento == null || puntoEmision == null || tipoComprador.equals("0") || identificacionDelComprador == null || identificacionDelComprador.equals(" ") || banderaComprador== false || FechaDeEmision == null 
				|| TipoDocumentoModifi.equals("0") || FecDeEmisionModif == null || noComprobanteModif == null || MotivoModifi == null || MotivoModifi == ""){
			if(codEstablecimiento.equals("null") || codEstablecimiento == ""){
				mensajeAlerta("Facturacion Electronica","El Campo Codigo Establecimiento Se Encuentra Vacio, Porfavor ingreselo", "alerta");		
				valida = true;
			}
			
			if(puntoEmision.equals("null") || puntoEmision == "" || puntoEmision.equals("0")){
				mensajeAlerta("Facturacion Electronica","El Campo Punto de Emision Se Encuentra Vacio, Porfavor ingreselo", "alerta");				
				valida = true;
			}
			
			if(tipoComprador.equals("0")){
		  		 mensajeAlerta("Facturacion Electronica","El Campo Tipo Comprador  Se Encuentra Vacio, Porfavor ingreselo", "alerta");
		  		 valida = true;
			}
			
			if(identificacionDelComprador.equals("null") || identificacionDelComprador.equals("") || razonSocial == null || razonSocial.trim().equals("")){
				mensajeAlerta("Facturacion Electronica","El Campo: Identificacion del Comprador  Se Encuentra Vacio, Porfavor ingreselo", "alerta");
				valida = true;
			}
	
			if(banderaComprador == false){
				mensajeAlerta("Facturacion Electronica","El Campo Identificacion Comprador Es incorrecto ", "alerta");
				valida = true;    
			}
	
			if(FechaDeEmision== null){
				mensajeAlerta("Facturacion Electronica","El Campo Fecha Emision Esta Vacio ", "alerta");
				valida = true;
			}
			
			if(TipoDocumentoModifi.equals("0")){
				mensajeAlerta("Facturacion Electronica","El Campo: Tipo documento que modifica no se a seleccionado, Porfavor seleccione", "alerta");
				valida = true;
			}
			
			if(FecDeEmisionModif == null){
				mensajeAlerta("Facturacion Electronica","El Campo: La fecha de Emision del documento que modifica Esta Vacio, Porfavor ingreselo", "alerta");
				valida = true;
			}
			
			if(noComprobanteModif == null || noComprobanteModif.equals("")){
				mensajeAlerta("Facturacion Electronica","El Campo: Nº Comprobante del documento que modifica Esta Vacio, Porfavor ingreselo", "alerta");
				valida = true;
			}
			
			if(MotivoModifi == null || MotivoModifi == ""){
				mensajeAlerta("Facturacion Electronica","El Campo: Motivo del documento que modifica Esta Vacio, Porfavor ingreselo", "alerta");
				valida = true;
			}
		}
		return valida;
	}
	
	//TODO contuctor que llena los punto de emision dependiendo del establecimiento seleccionado
	public void llenaPuntosEmision(){
		try{
			if (codEstablecimiento == null){
				mensajeAlerta("Ingreso Campos","Ingrese El Establecimiento antes del Punto Emision", "alerta");
				return;
			}
				listPuntosEmision = NotaCreditoServicio.buscarDatosPuntoEmision(empresa.getRuc(),codEstablecimiento,codigoTipoDocumento2.trim());	
				if (listPuntosEmision.isEmpty()){				
	                puntoEmisionPK.setCodEstablecimiento("");
	                puntoEmisionPK.setRuc("");
	                //Verificar Cambio HUGO
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
		resultado = NotaCreditoServicio.BuscarfitroEmpresaDocumentos(parametro_ruc.trim(), empresa.getRuc().trim(), tipoComprador.trim());	
		return resultado;
	}
	
	 //TODO 
    /**
     * contructor que se encarga de calcular y recalcular los valores del producto seleccionado, 
     * el filtrado de borrar campo lo que hace es que si se selecciona otro tipo de producto se limpia los controles de la fila seleccionada
  	**/
  	public void calculaTotal_producto(String evento){
  		try{
  			
  			PantallaDetalleDocumentoCredito detDocumen;
	  		 if(evento.toString().trim().equals("llenartablaproducto")){
	  			TablaProducto = new ArrayList<FacProducto>();
	  			filtraTablaProducto = new ArrayList<FacProducto>();
	  			TablaProducto = NotaCreditoServicio.buscarProductos();
	  			tablaProductoTipo = new ArrayList<String>();
	  			for (FacProducto prod : TablaProducto){
	  				if (prod.getTipoProducto().equals("B"))
	  					this.tablaProductoTipo.add("Bienes");
	  				else
	  					this.tablaProductoTipo.add("Servicio");
	  			}
	  			
	  		 }
	  		 else if(evento.toString().trim().equals("ProductoSeleccionado")){

	  			Evento2 = "ProductoSeleccionado";
	  			seleccionaDetalleDocumento.setCodigoProducto(verCamposProducto.getCodPrincipal().toString());
	  			seleccionaDetalleDocumento.setCodigoAdicional(verCamposProducto.getCodAuxiliar().toString());
	  			seleccionaDetalleDocumento.setDescripcion(verCamposProducto.getDescripcion());
	  			seleccionaDetalleDocumento.setPrecio(verCamposProducto.getValorUnitario());
	  			seleccionaDetalleDocumento.setTipoIVA(verCamposProducto.getTipoIva());
	  			seleccionaDetalleDocumento.setTipoIce(verCamposProducto.getCodIce());
	  			FacGeneral general = new FacGeneral();
	  			//if (verCamposProducto.getTipoProducto().equals("B"))
	  				general = NotaCreditoServicio.valoImpuestos("24", verCamposProducto.getTipoIva().toString());
	  			//else
	  			seleccionaDetalleDocumento.setPorcentajeIVA(general.getPorcentaje());
	  			seleccionaDetalleDocumento.setDescripcionIVA(general.getDescripcion());
	  			if(!verCamposProducto.getCodIce().toString().trim().equals("-1"))
	  				if(!verCamposProducto.getCodIce().toString().trim().equals("0"))
	  					general = NotaCreditoServicio.valoImpuestos("28", verCamposProducto.getCodIce().toString());
	  			seleccionaDetalleDocumento.setPorcentajeICE((verCamposProducto.getCodIce().toString().trim().equals("-1") ? 0 : general.getPorcentaje()));
	  			seleccionaDetalleDocumento.setDescripcionIce((verCamposProducto.getCodIce().toString().trim().equals("-1") ? "" : general.getDescripcion()));
	  			seleccionaDetalleDocumento.setTipoProducto(verCamposProducto.getTipoProducto());
	  			calculaTotal_producto("RealizandoCalculo");
	  			if(ValidarRegistroDetalle(listPantallaDetalleDocumento, Arrays.asList(
	  					seleccionaDetalleDocumento.getCodigoProducto()))){
  					mensajeAlerta("Mensaje del sistema", "el registro ingresado ya se encuenta registrado", "alerta");  
  					mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "alerta");  
				}
	  		 }
	  		 else if(evento.toString().trim().equals("RealizandoCalculo")){
	  			 if(evento.equals("ProductoSeleccionado"))
	  				 detDocumen = seleccionaDetalleDocumento;
	  			 else
	  				 detDocumen = (PantallaDetalleDocumentoCredito) DataTableDetalle.getRowData();
	  			 
	  			 double total = 0 , subtotal = 0, ice = 0, iva = 0, porice = 0, poriva = 0;
	  			 System.out.println("Tipo de Producto::"+detDocumen.getTipoProducto());	  			 
	  			 //if (detDocumen.getTipoProducto().equals("B")){
		  			 total = Redondear(detDocumen.getCantidad() * detDocumen.getPrecio(), 2);
		  			 subtotal = Redondear(total - detDocumen.getDescuento(), 2);
		  			 detDocumen.setValorSubtotal(subtotal);
		  			 porice = detDocumen.getPorcentajeICE();
		  			 poriva = detDocumen.getPorcentajeIVA();
		  			 ice = Redondear(subtotal * (porice / 100), 2);
		  			 iva = Redondear(subtotal * (poriva / 100), 2);
		  			 detDocumen.setValorIVA(iva);
		  			 detDocumen.setValorIce(ice);
		  			 detDocumen.setValorTotal(Redondear(subtotal + iva + ice, 2));
	  			 //}
	  			 valorDocumento();
	  		 }
	  		if (seleccionaDetalleDocumento.getTipoProducto().equals("S"))
  				flagCtrlPrecio = false;	
  			else 
  			flagCtrlPrecio =true;
  		 }catch (Exception e) {
  			FacesMessage mensaje=null;
  			mensaje = new FacesMessage(FacesMessage.SEVERITY_INFO,e.getMessage(),null);
  			FacesContext.getCurrentInstance().addMessage(null, mensaje);
  		}
  	}
  	
  	//TODO contructor que se encarga de guardar el detalle del comprobante de retencion
  	public void insertarNotaCredito(String NombreBoton){
  		if(validarCampos())
  			return;
  		if(listPantallaDetalleDocumento.size()  == 0){
  			mensajeAlerta("Mensaje del sistema", "No hay registro de Detalle del documento","alerta");
  			return;
  		}
  		for (PantallaDetalleDocumentoCredito documento : listPantallaDetalleDocumento){
			if(ValidarRegistroDetalle(listPantallaDetalleDocumento, Arrays.asList(
						documento.getCodigoProducto()))){
					mensajeAlerta("Mensaje del sistema", "el registro ingresado ya se encuenta registrado", "alerta");  
					mensajeAlerta("Mensaje del sistema", "Por favor cambie el registro o no se procedera a guardar", "alerta");
					return;
			}
			if(documento.getCantidad() == 0){
				mensajeAlerta("Mensaje del sistema", "Por favor la linea :" + documento.getLinea() + " del detalle del documento tiene cantidad 0 ingrese un valor mayor a 0", "alerta");
				return;
			}
  		}
  		
  		int guardado = 0;
  		try{
  			List<FacEmpresa> listaEmpresa = new ArrayList<FacEmpresa>();
			listaEmpresa.add(empresa);
			List<FacCabDocumento> cabece_documento = new ArrayList<FacCabDocumento>();
			List<FacDetDocumento> detalle_documento = new ArrayList<FacDetDocumento>();
			List<FacDetRetencion> detalle_retencion = new ArrayList<FacDetRetencion>();
			List<FacDetImpuesto> detalle_impuesto = new ArrayList<FacDetImpuesto>();
			List<FacDetAdicional> detelle_adicional = new ArrayList<FacDetAdicional>();
			List<DetalleImpuestosentidades> detalleCabecera_impuesto = new ArrayList<DetalleImpuestosentidades>();
			List<String> MotivoRazon = new ArrayList<String>();
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
  			secuencial = NotaCreditoServicio.secuencialCabecera(empresa.getRuc(), codEstablecimiento, puntoEmision, codigoTipoDocumento2, ambientePtoEmision);
  			id.setSecuencial(cadenaSecuencial(secuencial,TamañoSecuencial));
  			id.setCodigoDocumento(codigoTipoDocumento2.trim());
  			cabDocumento.setId(id);
  			
  			
  			cabDocumento.setFechaEmision(FechaDeEmision);
			cabDocumento.setGuiaRemision(Espacios);
			cabDocumento.setRazonSocialComprador(razonSocial);
			cabDocumento.setIdentificacionComprador(identificacionDelComprador);
			cabDocumento.setTotalSinImpuesto(getSubtotalSinImpuesto());
			cabDocumento.setTotalDescuento(getTotalDescuento());
			cabDocumento.setEmail(correoElectronico);
			cabDocumento.setPropina(getPropina10());
			cabDocumento.setInfoAdicional(Espacios);
			cabDocumento.setPeriodoFiscal(Espacios);
			cabDocumento.setMoneda("DOLAR");
			cabDocumento.setRise(Espacios);
			cabDocumento.setFechaInicioTransporte(null);
			cabDocumento.setFechaFinTransporte(null);
			cabDocumento.setPlaca(Espacios);
			cabDocumento.setFecEmisionDocSustento(FecDeEmisionModif);
			cabDocumento.setMotivoRazon(MotivoModifi);
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
			cabDocumento.setMotivoValor(this.getValorTotalFactura());
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
  			
			
	        guardado = NotaCreditoServicio.insertarCabeceraDocumento(cabDocumento);///SE EJECUTA EL METODO
	        cabece_documento.add(cabDocumento);
			/** 
  			 * ingresando detalle del documento 
  			 * ingresando el detalle del impuesto
  			 * **/
	       
			int secu = 1;
			if (guardado == 0){
	  			for (PantallaDetalleDocumentoCredito documentoDetalle : listPantallaDetalleDocumento) {
	  				FacDetDocumento detDocumento = new FacDetDocumento();
	  				FacDetDocumentoPK detDocumentoPK = new FacDetDocumentoPK();
	  				detDocumentoPK.setRuc(cabDocumento.getId().getRuc());
		  			detDocumentoPK.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
		  			detDocumentoPK.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
		  			detDocumentoPK.setSecuencial(cabDocumento.getId().getSecuencial());
		  			detDocumentoPK.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
		  			detDocumentoPK.setSecuencialDetalle(documentoDetalle.getLinea());
  					detDocumento.setId(detDocumentoPK);
  					detDocumento.setCodPrincipal(documentoDetalle.getCodigoProducto());
  					detDocumento.setCodAuxiliar(documentoDetalle.getCodigoAdicional());
  					detDocumento.setDescripcion(documentoDetalle.getDescripcion());
  					detDocumento.setCantidad(documentoDetalle.getCantidad());
  					detDocumento.setDescuento(documentoDetalle.getDescuento());
  					detDocumento.setPrecioUnitario(documentoDetalle.getPrecio());
  					detDocumento.setValorIce(documentoDetalle.getValorIce());
  					detDocumento.setPrecioTotalSinImpuesto(documentoDetalle.getValorSubtotal());
  					guardado = NotaCreditoServicio.insertarDetalleDocumento(detDocumento);///SE EJECUTA EL METODO
  					detalle_documento.add(detDocumento);
  					if(guardado != 0)
  						break;
  					
  					FacProducto pro = new FacProducto();
  					pro = NotaCreditoServicio.buscarDatosProductofiltrando(Integer.parseInt(detDocumento.getCodPrincipal()));
  					DetalleAdicionalproducto.add(pro);
  					
  					FacDetImpuesto detImpuesto = new FacDetImpuesto();
	  				FacDetImpuestoPK detImpuestoPK = new FacDetImpuestoPK();
	  				
	  				detImpuestoPK.setRuc(cabDocumento.getId().getRuc());
					detImpuestoPK.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
					detImpuestoPK.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
					detImpuestoPK.setSecuencial(cabDocumento.getId().getSecuencial());
					detImpuestoPK.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
  					detImpuestoPK.setCodImpuesto(2);
  					detImpuestoPK.setSecuencialDetalle(documentoDetalle.getLinea());
	  				detImpuestoPK.setDetSecuencial(secu);
	  				
	  				detImpuesto.setId(detImpuestoPK);
	  				detImpuesto.setTipoImpuestos("IVA");
	  				detImpuesto.setCodPorcentaje(documentoDetalle.getTipoIVA());
	  				detImpuesto.setBaseImponible(documentoDetalle.getValorSubtotal());
	  				detImpuesto.setTarifa(documentoDetalle.getPorcentajeIVA());
	  				detImpuesto.setValor(documentoDetalle.getValorIVA());
	  				
  					guardado = NotaCreditoServicio.insertarDetalleDocumentoImpuesto(detImpuesto);///SE EJECUTA EL METODO	
  					detalle_impuesto.add(detImpuesto);
  					if(guardado != 0)
  						break;
  					/** PARA EL DATO DEL VALOR DE ICE **/
  					if(documentoDetalle.getTipoIce() >= 0){
	  					secu++;
	  					detImpuesto = new FacDetImpuesto();
		  				detImpuestoPK = new FacDetImpuestoPK();
		  				
		  				detImpuestoPK.setRuc(cabDocumento.getId().getRuc());
	  					detImpuestoPK.setCodEstablecimiento(cabDocumento.getId().getCodEstablecimiento());
						detImpuestoPK.setCodPuntEmision(cabDocumento.getId().getCodPuntEmision());
						detImpuestoPK.setSecuencial(cabDocumento.getId().getSecuencial());
						detImpuestoPK.setCodigoDocumento(cabDocumento.getId().getCodigoDocumento());
	  					detImpuestoPK.setCodImpuesto(3);
	  					detImpuestoPK.setSecuencialDetalle(documentoDetalle.getLinea());	  					
		  				detImpuestoPK.setDetSecuencial(secu);
		  				detImpuesto.setId(detImpuestoPK);
		  				detImpuesto.setTipoImpuestos("ICE");
		  				detImpuesto.setCodPorcentaje(documentoDetalle.getTipoIce());
		  				detImpuesto.setBaseImponible(documentoDetalle.getValorSubtotal());
		  				detImpuesto.setTarifa(documentoDetalle.getPorcentajeICE());
		  				detImpuesto.setValor(documentoDetalle.getValorIce());  					
	  					guardado = NotaCreditoServicio.insertarDetalleDocumentoImpuesto(detImpuesto);///SE EJECUTA EL METODO	
	  					detalle_impuesto.add(detImpuesto);
  					}
  					secu++;
  					if(guardado != 0)
  						break;
	  			}
			}

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
  	  				detAdicionalPK.setSecuencialDetalle(secuDa);
  					detAdicionalPK.setSecuencialDetAdicional(secuDa);
  					detAdicional.setId(detAdicionalPK);
  					detAdicional.setNombre(documentoAdicional.getNombre());
  					detAdicional.setValor(documentoAdicional.getValor());
  					guardado = NotaCreditoServicio.insertarDetalleAdicional(detAdicional);///SE EJECUTA EL METODO
  					secuDa++;
  					detelle_adicional.add(detAdicional);
  					if(guardado != 0)
  						break;
  				}
			}
  			if(guardado == 0)
  				detalleCabecera_impuesto = NotaCreditoServicio.detalleFiltroimpuesto(cabDocumento.getId().getSecuencial(), codigoTipoDocumento2.trim());
  			
			if(guardado != 0){
				mensajeAlerta("Nota de Credito Electronica","Se produjo un error al guardar la nota de credito","alerta");
				return;
			}
			SpoolGenerarArchivoControlador generarArchivo = new SpoolGenerarArchivoControlador();
			
			generarArchivo.setEmpresa(listaEmpresa);
			generarArchivo.setCabece_documento(cabece_documento);
			generarArchivo.setDetalle_documento(detalle_documento);
			generarArchivo.setDetalle_retencion(detalle_retencion);
			generarArchivo.setDetalle_impuesto(detalle_impuesto);
			generarArchivo.setDetelle_adicional(detelle_adicional);
			generarArchivo.setDetalleCabecera_impuesto(detalleCabecera_impuesto);
			generarArchivo.setMotivoRazon(MotivoRazon);
			generarArchivo.setDetalleAdicionalproducto(DetalleAdicionalproducto);
			
			if(generarArchivo.generarDocumento() == 0){
				mensajeAlerta("Nota de Credito Electronica","Se produjo un error al generar el archivo txt","alerta");
				return;
			}
			mensajeAlerta("Nota de Credito Electronica","Nota de Credito Generada \n" + codEstablecimiento + "-" + puntoEmision + "-" + cadenaSecuencial(secuencial,TamañoSecuencial), "Informacion");
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
  	
  //TODO contructor que valida que el detalle del registro no se repita
  	private boolean ValidarRegistroDetalle(List<PantallaDetalleDocumentoCredito> puntoEmision,List<String> Contenidos){
  		Boolean validacion = false;
  		int contador = 0;
  		for (PantallaDetalleDocumentoCredito contenido : puntoEmision) 
  			if(Contenidos.get(0).toString().trim().equals(contenido.getCodigoProducto().toString().trim()) == true){
  				contador ++;
  				if(contador == 2)
  					return validacion = true;
  			}
  		
  		return validacion;
  	}
  	
  	//TODO contructor que nos sirve para redondear numeros decimales
  	private double Redondear(double numero,int digitos)
  	{
  	   int cifras=(int) Math.pow(10,digitos);
  	   return Math.rint(numero*cifras)/cifras;
  	}
  	
	//TODO calculando el valor total del todos los impuesto e sin impuesto
  	public void valorDocumento(){
  		subtotalSinImpuesto = 0; totalvalorICE = 0; propina10 = 0; totalDescuento = 0; subtotal0 = 0; subtotal12 = 0; subtotalNoIva = 0; valorTotalFactura = 0; iva12 = 0;
  		for (PantallaDetalleDocumentoCredito documento : listPantallaDetalleDocumento) {
			subtotalSinImpuesto += documento.getValorSubtotal();
			totalvalorICE += documento.getValorIce();
			totalDescuento += documento.getDescuento();
			if(documento.getTipoIVA() == 0)
				subtotal0 += documento.getValorSubtotal();
			else if(documento.getTipoIVA() == 2){
				subtotal12 += documento.getValorSubtotal();
				iva12 += documento.getValorIVA();
			}
			else if(documento.getTipoIVA() == 6)
				subtotalNoIva += documento.getValorSubtotal();
		}
  		subtotalSinImpuesto = Redondear(subtotalSinImpuesto, 2);
  		totalvalorICE = Redondear(totalvalorICE, 2);
  		totalDescuento = Redondear(totalDescuento, 2);
  		subtotal0 = Redondear(subtotal0, 2);
  		subtotal12 = Redondear(subtotal12, 2);
  		iva12 = Redondear(iva12, 2);
  		subtotalNoIva = Redondear(subtotalNoIva, 2);
  		
  		propina10 = Redondear((propina ? (subtotalSinImpuesto * 0.1) : 0), 2);
  		valorTotalFactura = Redondear(subtotalSinImpuesto + totalvalorICE + iva12 + propina10, 2);
  	}
	
	//TODO contructor que se encarga de llenar los combo
	private void LlenarComboGeneral(String codigoGeneral, List<SelectItem> listaItems, String lista){
		try{
			if(lista.toString().trim().equals("fac_general")){
				List<FacGeneral> listGeneral = new ArrayList<FacGeneral>();
				FacGeneral generales = new FacGeneral(); 
				listGeneral = NotaCreditoServicio.buscarDatosPorCodigo(codigoGeneral);
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
				listGeneral = NotaCreditoServicio.buscartipoDocumento(codigoGeneral);;
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
						comprador = NotaCreditoServicio.buscarComprador(identificacionDelComprador);
						if (comprador != null){
							this.setRazonSocial(comprador.getRazonSocial());
							this.setDireccionEstablecimiento(comprador.getDireccion());
							this.setCorreoElectronico(comprador.getEmail());
							this.setTelefono(comprador.getTelefono());
							presenta = true;
						}else{
							mensajeAlerta("Comprador","El Comprador No Existe", "informacion");
						}
					} 
					else 
						mensajeAlerta("Comprador","Documento Invalido", "informacion");
				}else{
					this.setRazonSocial("");
					this.setDireccionEstablecimiento("");
					this.setCorreoElectronico("");
					this.setTelefono("");
					mensajeAlerta("Ingrese Datos","Ingrese Identificacion Comprador", "alerta");				
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
			mensajeAlerta("Error Comprador","Error en la Identificacion del Comprador " + mensaje, "alerta");
		}
	}
	
	//TODO contructor que se encarga de filtrar el tipo de documento
	private void nombreDocumento(){
		try{
			List<FacTiposDocumento> documento = new ArrayList<FacTiposDocumento>();
			documento = NotaCreditoServicio.buscartipoDocumento(codigoTipoDocumento);
			if(!documento.isEmpty())
				for (FacTiposDocumento facTiposDocumento : documento) {
					tipoDocumento = facTiposDocumento.getDescripcion();
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//TODO contructor que se encarga de eliminar el detalle adicional
	public void BorrarLineaDetAdicional(String DetalleDocumento) {
		if(DetalleDocumento.equals("Detalleadicional")){
			listPantallaDetalleAdicional.remove(detDocumentoUI);

		}
		else{
			listPantallaDetalleDocumento.remove(seleccionaDetalleDocumento);
			int contador = 0;
			for (PantallaDetalleDocumentoCredito detalle : listPantallaDetalleDocumento) {
				contador ++;
				detalle.setLinea(contador);
			}
			valorDocumento();
		}
		valorDocumento();
   	}
	
	//TODO contructor de mensaje de alerta para mostrar al usuario
	private void mensajeAlerta(String mensajeVentana, String mensajeDetalle, String tipo) {
	       FacesContext context = FacesContext.getCurrentInstance();            
	       context.addMessage(null, new FacesMessage((tipo.toString().trim().equals("alerta") ? FacesMessage.SEVERITY_ERROR : tipo.toString().trim().equals("peligro") ? FacesMessage.SEVERITY_WARN : FacesMessage.SEVERITY_INFO),mensajeVentana, mensajeDetalle));
	}
	
	
	// variable de metodo get y set
	
	public String getTipoComprador() {
		return tipoComprador;
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

	public double getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(double totalDescuento) {
		this.totalDescuento = totalDescuento;
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

	public double getPropina10() {
		return propina10;
	}

	public void setPropina10(double propina10) {
		this.propina10 = propina10;
	}

	public boolean isPropina() {
		return propina;
	}

	public void setPropina(boolean propina) {
		this.propina = propina;
	}

	public double getValorTotalFactura() {
		return valorTotalFactura;
	}

	public void setValorTotalFactura(double valorTotalFactura) {
		this.valorTotalFactura = valorTotalFactura;
	}

	public List<FacProducto> getTablaProducto() {
		return TablaProducto;
	}

	public void setTablaProducto(List<FacProducto> tablaProducto) {
		TablaProducto = tablaProducto;
	}

	public List<FacProducto> getFiltraTablaProducto() {
		return filtraTablaProducto;
	}

	public void setFiltraTablaProducto(List<FacProducto> filtraTablaProducto) {
		this.filtraTablaProducto = filtraTablaProducto;
	}

	public FacProducto getVerCamposProducto() {
		return verCamposProducto;
	}

	public void setVerCamposProducto(FacProducto verCamposProducto) {
		this.verCamposProducto = verCamposProducto;
	}

	public PantallaDetalleDocumentoCredito getSeleccionaDetalleDocumento() {
		return seleccionaDetalleDocumento;
	}

	public void setSeleccionaDetalleDocumento(
			PantallaDetalleDocumentoCredito seleccionaDetalleDocumento) {
		this.seleccionaDetalleDocumento = seleccionaDetalleDocumento;
	}

	public UIData getDataTableDetalle() {
		return DataTableDetalle;
	}

	public void setDataTableDetalle(UIData dataTableDetalle) {
		DataTableDetalle = dataTableDetalle;
	}

	public List<PantallaDetalleDocumentoCredito> getListPantallaDetalleDocumento() {
		return listPantallaDetalleDocumento;
	}

	public void setListPantallaDetalleDocumento(
			List<PantallaDetalleDocumentoCredito> listPantallaDetalleDocumento) {
		this.listPantallaDetalleDocumento = listPantallaDetalleDocumento;
	}

	public PantallaDetalleDocumentoCredito getPantallaDetalleDocumento() {
		return pantallaDetalleDocumento;
	}

	public void setPantallaDetalleDocumento(
			PantallaDetalleDocumentoCredito pantallaDetalleDocumento) {
		this.pantallaDetalleDocumento = pantallaDetalleDocumento;
	}

	public boolean isVisibleBotonePuntoEmision() {
		return visibleBotonePuntoEmision;
	}

	public void setVisibleBotonePuntoEmision(boolean visibleBotonePuntoEmision) {
		this.visibleBotonePuntoEmision = visibleBotonePuntoEmision;
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

	public String getMotivoModifi() {
		return MotivoModifi;
	}

	public void setMotivoModifi(String motivoModifi) {
		MotivoModifi = motivoModifi;
	}

	public String getNoComprobanteModif() {
		return noComprobanteModif;
	}

	public void setNoComprobanteModif(String noComprobanteModif) {
		this.noComprobanteModif = noComprobanteModif;
	}

	public Date getFechaDeEmisionModif() {
		return FecDeEmisionModif;
	}

	public void setFechaDeEmisionModif(Date fecDeEmisionModif) {
		FecDeEmisionModif = fecDeEmisionModif;
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

	public String getIdentificacionDelComprador() {
		return identificacionDelComprador;
	}

	public void setIdentificacionDelComprador(String identificacionDelComprador) {
		this.identificacionDelComprador = identificacionDelComprador;
	}

	public List<SelectItem> getTiposComprador() {
		return tiposComprador;
	}

	public void setTiposComprador(List<SelectItem> tiposComprador) {
		this.tiposComprador = tiposComprador;
	}

	public void setTipoComprador(String tipoComprador) {
		this.tipoComprador = tipoComprador;
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

	public String getTipoEmision() {
		return tipoEmision;
	}

	public void setTipoEmision(String tipoEmision) {
		this.tipoEmision = tipoEmision;
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
	
	public String getCodEstablecimiento() {
		return codEstablecimiento;
	}

	public void setCodEstablecimiento(String codEstablecimiento) {
		this.codEstablecimiento = codEstablecimiento;
	}

	public String getRuc() {
		return ruc;
	}
	
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	
	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}

	public String getCodigoTipoDocumento() {
		return codigoTipoDocumento;
	}

	public void setCodigoTipoDocumento(String codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getAmbientePtoEmision() {
		return ambientePtoEmision;
	}

	public void setAmbientePtoEmision(String ambientePtoEmision) {
		this.ambientePtoEmision = ambientePtoEmision;
	}

	public boolean getFlagCtrlPrecio() {
		return flagCtrlPrecio;
	}
	
	public boolean isFlagCtrlPrecio() {
		return flagCtrlPrecio;
	}

	public void setFlagCtrlPrecio(boolean flagCtrlPrecio) {
		this.flagCtrlPrecio = flagCtrlPrecio;
	}

	public List<String> getTablaProductoTipo() {
		return tablaProductoTipo;
	}

	public void setTablaProductoTipo(List<String> tablaProductoTipo) {
		this.tablaProductoTipo = tablaProductoTipo;
	}


}
